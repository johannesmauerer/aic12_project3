package aic12.project3.common.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tweets")
public class TweetDTO
{
	@Id
    private String twitterId;
    private String text;
    private Date date;
    private Integer sentiment;
    private List<String> companies = new ArrayList<String>();
    
    public TweetDTO() {}
    
    public TweetDTO(String twitterId, String text, Date date)
    {
        this.twitterId = twitterId;
        this.text = text;
        this.date = date;
    }

    public String getTwitterId()
    {
        return twitterId;
    }

    public void setTwitterId(String twitterId)
    {
        this.twitterId = twitterId;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Integer getSentiment()
    {
        return sentiment;
    }

    public void setSentiment(Integer sentiment)
    {
        this.sentiment = sentiment;
    }
	
	public List<String> getCompanies() {
		return companies;
	}

	public void setCompanies(List<String> companies) {
		this.companies = companies;
	}

	@Override
	public String toString(){
		return "TweetDTO[tweetId="+twitterId+", date="+date+", companies="+companies+"]";
	}
    
}
