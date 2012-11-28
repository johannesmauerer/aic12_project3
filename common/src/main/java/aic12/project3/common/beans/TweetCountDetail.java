package aic12.project3.common.beans;

import java.util.Date;
import java.util.HashMap;

public class TweetCountDetail
{
    private HashMap<Date,Integer> map;

	public TweetCountDetail() {}
	
	public TweetCountDetail(HashMap<Date, Integer> map) {
		super();
		this.map = map;
	}

	public HashMap<Date, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<Date, Integer> map) {
		this.map = map;
	}
}
