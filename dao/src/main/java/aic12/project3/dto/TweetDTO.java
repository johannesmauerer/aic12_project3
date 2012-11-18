package aic12.project3.dto;
import java.util.Date;


public class TweetDTO{

	private String text;
	private Date date;
	private Integer sentiment;
	
	public TweetDTO(){
		
	}
	
	public TweetDTO(String text, Date date){
		this.text = text;
		this.date = date;
		this.sentiment = null;
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
	public Integer getSentiment() {
		return sentiment;
	}

	public void setSentiment(Integer sentiment) {
		this.sentiment = sentiment;
	}

	@Override
	public String toString() {
		return "TweetDTO [text=" + text + ", date=" + date + ", sentiment=" + (sentiment==null?sentiment:"null") +"]";
	}
}
