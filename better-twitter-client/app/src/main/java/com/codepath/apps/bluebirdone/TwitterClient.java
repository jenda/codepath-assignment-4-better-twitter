package com.codepath.apps.bluebirdone;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.util.function.Consumer;

import static com.codepath.apps.bluebirdone.models.User_Table.screenName;


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
	// Test app 2
//	public static final String REST_CONSUMER_KEY = "dK2804IPPTsmoIrpiS7LItZ8I";
//	public static final String REST_CONSUMER_SECRET = "MNQXIijfqUKFW8jQlIBzfLGL0ACpw5LxbEg80MMg9B0XotFyq0";

	// Test app 3
	public static final String REST_CONSUMER_KEY = "ACPhnmmbdm19MrsdCQE0AU4Za";
    public static final String REST_CONSUMER_SECRET = "S7JJQ4RQY06JvITXMoyOiYFlQQCctSpVecd6fp08kbGqfBx3Or";

    // Test app 4
//    public static final String REST_CONSUMER_KEY = "s4GbOr9BsCjf0DFbTt0WcrJIN";
//    public static final String REST_CONSUMER_SECRET = "bZ6BLhdtateFOeUaetlBJsFrYXIjvoJSxAj9WJ30Xm8Fpgvvl7";



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
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}


	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		RequestHandle requestHandle = getClient().get(apiUrl, params, handler);
	}

    public void getUserMentions(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        RequestHandle requestHandle = getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(int page, AsyncHttpResponseHandler handler, Long userId) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // add user_id
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        if (userId != null && userId != 0) {
            params.put("user_id", userId);
        }
        RequestHandle requestHandle = getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, @Nullable Long inReplyToStatusId,
                          AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);

        if (inReplyToStatusId != null) {
            params.put("in_reply_to_status_id", inReplyToStatusId);
        }

        getClient().post(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

    public void follow(Long userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friendships/create.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        getClient().post(apiUrl, params, handler);
    }

    public void unfollow(Long userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friendships/destroy.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        getClient().post(apiUrl, params, handler);
    }

    public void getUserInfo(Long userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/lookup.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/lookup.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void retweet(Long tweetId, AsyncHttpResponseHandler handler) {
        Log.d("jenda", "url " + String.format("statuses/retweet/%s.json", tweetId));
        String apiUrl = getApiUrl(String.format("statuses/retweet/%s.json", tweetId));
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }
    public void unretweet(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("statuses/unretweet/%s.json", tweetId));
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    public void fav(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }
    public void unfav(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or text)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
