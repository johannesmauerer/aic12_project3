package aic12.project3.dto;
import java.util.Date;


public class TweetDTO{

	private Long twitterId;
	private String text;
	private Date date;
	private Integer sentiment;
	
	public TweetDTO(){
		
	}
	
	public TweetDTO(Long twitterId, String text, Date date){
		this.twitterId = twitterId;
		this.text = text;
		this.date = date;
		this.sentiment = null;
	}

	public Long getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
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
		return "TweetDTO [id="+twitterId+", text=" + text + ", date=" + date + ", sentiment=" + (sentiment!=null?sentiment:"null") +"]";
	}
}