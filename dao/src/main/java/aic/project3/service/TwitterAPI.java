package aic.project3.service;

import java.util.List;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

public interface TwitterAPI {

	List<TweetDTO> getAllTweets(SentimentRequest req);

}
