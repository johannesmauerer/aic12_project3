package util;

import java.util.Date;

public class SentimentRequestStats {

	private float sentiment;
	private int tweets;
	private double intervalMin;
	private double intervalMax;
	private Date from;
	private Date to;
	
	public float getSentiment() {
		return sentiment;
	}
	public void setSentiment(float sentiment) {
		this.sentiment = sentiment;
	}
	public int getTweets() {
		return tweets;
	}
	public void setTweets(int tweets) {
		this.tweets = tweets;
	}
	public double getIntervalMin() {
		return intervalMin;
	}
	public void setIntervalMin(double intervalMin) {
		this.intervalMin = intervalMin;
	}
	public double getIntervalMax() {
		return intervalMax;
	}
	public void setIntervalMax(double intervalMax) {
		this.intervalMax = intervalMax;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
}