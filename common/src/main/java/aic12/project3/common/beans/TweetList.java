package aic12.project3.common.beans;

import java.util.List;

import aic12.project3.common.dto.TweetDTO;

public class TweetList
{
    private List<TweetDTO> list;
    
    public TweetList() {}
    
    public TweetList(List<TweetDTO> list)
    {
        this.list = list;
    }

    public List<TweetDTO> getList()
    {
        return list;
    }

    public void setList(List<TweetDTO> list)
    {
        this.list = list;
    }
}
