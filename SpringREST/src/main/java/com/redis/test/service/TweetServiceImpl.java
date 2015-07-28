package com.redis.test.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.social.twitter.api.Tweet;

@Named
public class TweetServiceImpl implements TweetService {

	@Inject @Named("twitterOutbound")
	private MessageChannel outChannel;
	
	@Inject @Named("timelineChannel")
	private PollableChannel inChannel;
	
	@Override
	public boolean postTweet(String message) throws IllegalArgumentException {
		if(message.length()>140)
			throw new IllegalArgumentException("Tweet too long");
		return outChannel.send(new GenericMessage<String>(message));
	}

	@Override @SuppressWarnings("unchecked")
	public List<GenericMessage<Tweet>> getTimelineUpdates() {
		List<GenericMessage<Tweet>> messages = new ArrayList<>();
		GenericMessage<Tweet> obj =  null;
		int i=0;
		while(i<10 && (obj = (GenericMessage<Tweet>) inChannel.receive(100))!=null){
			i++;
			messages.add(obj);
		}

		return messages;
		//return message.getPayload().getFromUser() message.getPayload().getText();
	}
	
	/*private String convert(GenericMessage<Tweet> message){
		String.format("%s: %s", args)
		return "";
	}*/

}
