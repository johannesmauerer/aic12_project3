package aic12.project3.service;

import java.util.List;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.common.dto.TweetDTO;

public interface TwitterAPI {

	List<TweetDTO> getAllTweets(String company);

	void registerForTwitterStream(String company);

}
