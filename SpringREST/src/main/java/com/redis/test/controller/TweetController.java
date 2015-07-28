package com.redis.test.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.support.GenericMessage;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redis.test.domain.MyTweet;
import com.redis.test.service.TweetService;

/**
 * Handles requests for the application home page.
 * 
 * @author ketav
 */

@Controller
@RequestMapping("/tweet")
public class TweetController {

	@Inject
	private TweetService tweetService;
	
	@RequestMapping(method=RequestMethod.POST, consumes={"application/json"}, produces={"application/json"})
	public @ResponseBody Boolean tweet(@RequestBody MyTweet tweet){
		return tweetService.postTweet(tweet.getMessage());
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<GenericMessage<Tweet>> getTimelineUpdates(){
		return tweetService.getTimelineUpdates();
	}
	
}
