package com.redis.test.service;

import java.util.List;

import org.springframework.messaging.support.GenericMessage;
import org.springframework.social.twitter.api.Tweet;


public interface TweetService {
	boolean postTweet(String tweet);
	List<GenericMessage<Tweet>> getTimelineUpdates();
}
