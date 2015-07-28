package com.redis.test.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;

public class TweetConverter implements MessageConverter {

	@Override
	public String fromMessage(Message<?> arg0, Class<?> arg1) {
		return null;
	}

	@Override
	public Message<?> toMessage(Object arg0, MessageHeaders arg1) {
		return null;
	}

}
