package com.JoelH.illanelokuvat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie> {
	XMLParser parser;
	private ArrayList<Movie> items;
	private int layoutResourceId;
	private Context context;
	MovieHolder holder = null;
	private int Count = 0;
	private Bitmap Cover;
	private Bitmap Channel;
	private float stepsize = 0.5f;
	
	public MovieAdapter(Context context, int layoutResourceId, ArrayList<Movie> items) {
		super(context,layoutResourceId);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent,false);
		
		holder = new MovieHolder();
		holder.ChannelURL = items.get(position).getChannel();
		holder.CoverURL = items.get(position).getPic();
		LoadImages im = new LoadImages();
		im.execute(holder);
		
		holder.Title = (TextView) row.findViewById(R.id.ElokuvanNimi);
		holder.Year = (TextView) row.findViewById(R.id.Vuosiluku);
		holder.Lenght = (TextView) row.findViewById(R.id.Pituus);
		holder.Channel = (ImageView) row.findViewById(R.id.Kanava);
		holder.rating = (RatingBar) row.findViewById(R.id.movierating);
		holder.Cover = (ImageView) row.findViewById(R.id.Kansi);
		holder.Date = (TextView) row.findViewById(R.id.Aika);
		
		holder.Title.setText(items.get(position).getName());
		holder.Year.setText(items.get(position).getYear());
		holder.Lenght.setText(items.get(position).getLength() + "min");
		holder.Date.setText(items.get(position).getDate().substring(items.get(position).getDate().indexOf("T")+1,items.get(position).getDate().indexOf("T")+6));
		holder.rating.setStepSize(stepsize);
		holder.rating.setRating(CountRating(position));
		holder.Cover.setImageBitmap(Cover);
		holder.Channel.setImageBitmap(Channel);
		row.setTag(holder);
		
		return row;
	} 
	
	public void CountMovies() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date tom = cal.getTime();
		Date day = new Date();
		String date = (new SimpleDateFormat("yyyy-MM-dd")).format(day);
		String tomorrow = (new SimpleDateFormat("yyyy-MM-dd")).format(tom);

		String CutDate = "";
		for (int i = 0; i < items.size(); i++) {
			CutDate = "";
			String clocktime = items.get(i).getDate().substring(items.get(i).getDate().indexOf("T")+1, items.get(i).getDate().indexOf("T")+3);
			CutDate = items.get(i).getDate().substring(0,items.get(i).getDate().indexOf("T"));
			if (date.equals(CutDate)) {
				Count++;
			}
			if (tomorrow.equals(CutDate)) {
				if (Integer.parseInt(clocktime) < 3) {
					Count++;
				}
			}
		}
	}
	private float CountRating(int position) {
		float numOfStars = 0f;
		String rating = items.get(position).getRating();
		for (int i = 0; i < rating.length(); i++) {
			if (rating.charAt(i) == '*') {
				numOfStars += 1;
			}
			if ( rating.charAt(i) == '½') {
				numOfStars += 0.5;
			}
		}
		return numOfStars;
	}
	
	public int GetCount() {
		Count = 0;
		CountMovies();
		return Count;

	}
	public static class MovieHolder {
		TextView Title;
		TextView Year;
		TextView Lenght;
		ImageView Cover;
		ImageView Channel;
		RatingBar rating;
		TextView Date;
		Bitmap cover;
		Bitmap channel;
		String ChannelURL;
		String CoverURL;
	}
	
	private class LoadImages extends AsyncTask<MovieHolder,Void,MovieHolder> {
		
		@Override
		protected MovieHolder doInBackground(MovieHolder... params) {
			MovieHolder movholder = params[0];
			try {
				 
				movholder.cover = loadBitmap(movholder.CoverURL);
				
				movholder.channel = loadBitmap(movholder.ChannelURL);
					
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return movholder;
		}
		@Override
		protected void onPostExecute(MovieHolder result) {
			result.Cover.setImageBitmap(result.cover);
			result.Channel.setImageBitmap(result.channel);

			
		}
	}
	public static Bitmap loadBitmap(String url) throws IOException {
		try {		
        HttpGet httpRequest = new HttpGet(URI.create(url) );
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
        HttpEntity entity = response.getEntity();
        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
         
        Bitmap myBitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
        httpRequest.abort();
        return myBitmap;
    } catch (IOException e) {
        e.printStackTrace();
        Log.e("Exception",e.getMessage());
        return null;

	    
	}

}
}

