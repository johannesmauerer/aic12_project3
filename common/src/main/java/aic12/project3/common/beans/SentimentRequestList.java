package aic12.project3.common.beans;

import java.util.List;

public class SentimentRequestList
{
    private List<SentimentRequest> list;
    
    public SentimentRequestList() {}
    
    public SentimentRequestList(List<SentimentRequest> list)
    {
        this.list = list;
    }

    public List<SentimentRequest> getList()
    {
        return list;
    }

    public void setList(List<SentimentRequest> list)
    {
        this.list = list;
    }
}
