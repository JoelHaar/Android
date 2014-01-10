package com.JoelH.illanelokuvat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XMLParser {
	
	private static final String LINK = "http://www.leffatykki.com/xml/ilta/tvssa";
	private InputStream in;
	private static final String ns = null;
	private ArrayList<Movie> movies = new ArrayList<Movie>();
	
	public XMLParser() {
		//Get InputStream from connection
		try {
			in = new URL(LINK).openConnection().getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Parse() throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			readFeed(parser); }
			
		finally {
			in.close();
		}
		
	}
	
	private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
		
		parser.require(XmlPullParser.START_TAG, ns, "today");
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("movie")) {
				readEntry(parser);
			}
			else {
				skip(parser);
			}
		}
	}

	private void readEntry(XmlPullParser parser) throws XmlPullParserException, IOException{
		parser.require(XmlPullParser.START_TAG, ns, "movie");
		String name = null;
		String year = null;
		String rating = null;
		String lenght = null;
		String pic = null;
		String channel = null;
		String date = null;
		String link = parser.getText();
		
		//Get all the data needed
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String nametemp = parser.getName();
			
			if (nametemp.equals("name")) {
				name = readName(parser);
			}
			if (nametemp.equals("cover")) {
				pic = readCover(parser);
			}
			if (nametemp.equals("channel_logo")) {
				channel = readChannel(parser);
			}
			if (nametemp.equals("date")) {
				date = readDate(parser);
			}
			if (nametemp.equals("year")) {
				year = readYear(parser);
			}
			if (nametemp.equals("length")) {
				lenght = readLength(parser);
			}
			if (nametemp.equals("rating")) {
				rating = readRating(parser);
				//Create a new object of Movie class and add it to the ArrayList
				movies.add(new Movie(name,date,year,rating,lenght,pic,channel,link));

			}
			
		}
		
		
	}

	private String readName(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "name");
	    String name = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "name");
	    return name;
	}
	
	private String readChannel(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "channel_logo");
	    String channel = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "channel_logo");

	    return channel;
	}
	private String readDate(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "date");
	    String date = readTextInt(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "date");
	    return date;
	}
	private String readYear(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "year");
	    String year = readTextInt(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "year");
	    return year;
	}
	private String readLength(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "length");
	    String length = readTextInt(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "length");
	    return length;
	}
	private String readRating(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "rating");
	    String rating = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "rating");
	    return rating;
	}
	
	
	private String readCover(XmlPullParser parser)  throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "cover");
	    String cover = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "cover");
	    return cover;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
}
	private String readTextInt(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = String.valueOf(parser.getText());
	        parser.nextTag();
	    }
	    return result;
}
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	public ArrayList<Movie> GetList() {
		return movies;
	}
}
