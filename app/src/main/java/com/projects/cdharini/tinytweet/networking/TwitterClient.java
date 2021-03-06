package com.projects.cdharini.tinytweet.networking;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.projects.cdharini.tinytweet.R;
import com.projects.cdharini.tinytweet.TinyTweetApplication;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";

	public static final String REST_CONSUMER_KEY = TinyTweetApplication.getContext().getString(R.string.twitter_api_key);
	public static final String REST_CONSUMER_SECRET = TinyTweetApplication.getContext().getString(R.string.twitter_client_secret);

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	public void getTweetTimeline(AsyncHttpResponseHandler handler , long maxId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		getTimeline(apiUrl, handler, null, maxId);
	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		getTimeline(apiUrl, handler, null, maxId);
	}

	public void getUserTimeline(AsyncHttpResponseHandler handler, long maxId, long userId) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		getTimeline(apiUrl, handler, params, maxId);
	}

	public void getTimeline(String url, AsyncHttpResponseHandler handler, @Nullable RequestParams params, long maxId) {
		// Can specify query string params directly or through RequestParams.
		if (params == null) {
			params = new RequestParams();
		}
		params.put("format", "json");
		params.put("count", 25);
		params.put("since_id", 1);
		if (maxId != -1) {
			params.put("max_id", maxId);
		}
		client.get(url, params, handler);
	}

	public void postTweet(AsyncHttpResponseHandler handler, String status, long in_reply_to_id) {

		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
        if (in_reply_to_id != -1) {
            params.put("in_reply_to_status_id", in_reply_to_id);
        }
		params.put("status", status);
		client.post(apiUrl, params, handler);
	}

	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

	public void retweet(AsyncHttpResponseHandler handler, long tweetId) {
		String url = getApiUrl("statuses/retweet/");
		String apiUrl = url.concat(tweetId + ".json");
		RequestParams params = new RequestParams();
		client.post(apiUrl, params, handler);
	}

	public void favorite(AsyncHttpResponseHandler handler, long tweetId) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		client.post(apiUrl, params, handler);
	}

	public void unretweet(AsyncHttpResponseHandler handler, long tweetId) {
		String url = getApiUrl("statuses/unretweet/");
		String apiUrl = url.concat(tweetId + ".json");
		RequestParams params = new RequestParams();
		client.post(apiUrl, params, handler);
	}

	public void unfavorite(AsyncHttpResponseHandler handler, long tweetId) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		client.post(apiUrl, params, handler);
	}
}
