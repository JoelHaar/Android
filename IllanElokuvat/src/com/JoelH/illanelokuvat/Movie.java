package com.JoelH.illanelokuvat;

public class Movie {

	private String name;
	private String year;
	private String rating;
	private String lenght;
	private String pic;
	private String channel;
	private String date;
	private String link;
	
	public Movie() {
		
	}
	
	public Movie(String name, String date, String year, String rating, String lenght, String pic, String channel, String link) {
		this.name = name;
		this.year = year;
		this.rating = rating;
		this.lenght = lenght;
		this.pic = pic;
		this.channel = channel;
		this.date = date;
		this.link = link;
	}
	
	public String getName() {
		return name;
	}
	public String getYear() {
		return year;
	}
	public String getRating() {
		return rating;
	}
	public String getLength() {
		return lenght;
	}
	public String getPic() {
		return pic;
	}
	public String getChannel() {
		return channel;
	}
	public String getDate() {
		return date;
	}
	public String getLink() {
		return link;
	}
}
