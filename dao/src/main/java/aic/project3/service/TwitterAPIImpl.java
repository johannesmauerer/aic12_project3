package aic.project3.service;

import java.util.List;

import org.springframework.stereotype.Component;

import aic12.project3.common.beans.SentimentRequest;
import aic12.project3.dto.TweetDTO;

@Component
public class TwitterAPIImpl implements TwitterAPI {

	@Override
	public List<TweetDTO> getAllTweets(SentimentRequest req) {
		return null; // TODO implement
	}
}
