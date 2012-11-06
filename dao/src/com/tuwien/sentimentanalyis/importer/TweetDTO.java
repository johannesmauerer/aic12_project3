package com.tuwien.sentimentanalyis.importer;
import java.util.Date;


public class TweetDTO{

	private String text;
	private Date date;
	
	public TweetDTO(String text, Date date){
		this.text = text;
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "TweetDTO [text=" + text + ", date=" + date + "]";
	}
}
