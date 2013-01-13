package util;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SentimentRequestStats implements Serializable {

	private double sentiment;
	private long tweets;
	private double intervalMin;
	private double intervalMax;
	private Date from;
	private Date to;
	
	public double getSentiment() {
		return sentiment;
	}
	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}
	public long getTweets() {
		return tweets;
	}
	public void setTweets(long tweets) {
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
