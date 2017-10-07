package com.projects.cdharini.tinytweet;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.projects.cdharini.tinytweet.models.User;
import com.projects.cdharini.tinytweet.networking.TwitterClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TinyTweetApplication extends Application {
	private static final String TAG = TinyTweetApplication.class.getSimpleName();
	private static Context context;
	private static User user;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TinyTweetApplication.context = this;

		getRestClient().getCurrentUser(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					user = User.fromJson(response);
				} catch (JSONException e) {
					Log.e(TAG, "Could not parse logged in uesr!");
				}
			}


			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

		});
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TinyTweetApplication.context);
	}

	public static Context getContext(){
		return context;
	}

	public static User getLoggedInUser() {
		return user;
	}

}