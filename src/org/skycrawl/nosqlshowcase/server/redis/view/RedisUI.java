package org.skycrawl.nosqlshowcase.server.redis.view;

import org.skycrawl.nosqlshowcase.server.redis.RedisConnection;
import org.skycrawl.nosqlshowcase.server.redis.controller.RedisDataController;
import org.skycrawl.nosqlshowcase.server.root.ui.AbstractDatabaseUI;

import redis.clients.jedis.JedisPool;

import com.vaadin.annotations.Title;

@Title("Redis mini-app")
public class RedisUI extends AbstractDatabaseUI<JedisPool, RedisDataController, RedisConnection>
{
	private static final long	serialVersionUID	= 2343206316209127417L;
}