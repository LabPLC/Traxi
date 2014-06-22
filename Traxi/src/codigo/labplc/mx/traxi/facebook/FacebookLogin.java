package codigo.labplc.mx.traxi.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.model.GraphUser;

/**
 * 
 * @author zace3d
 *
 */
@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class FacebookLogin {
	public static final String TAG = FacebookLogin.class.getName();


	private Activity activity;
	private SharedPreferences sharedPrefs;

	private Facebook facebook;
	private AsyncFacebookRunner asyncRunner;
	private static final String[] PERMS = new String[] { "read_stream" };
	private static  final String APP_ID = "622752297805514";
	private OnLoginFacebookListener onLoginFacebookListener;
	private OnGetFriendsFacebookListener onGetFriendsFacebookListener;

	protected String userId;
	protected String userName;

	/**
	 * Constructor
	 * 
	 * @param activity
	 */
	public FacebookLogin(Activity activity) {
		this.activity = activity;

		facebook = new Facebook(APP_ID);
		asyncRunner = new AsyncFacebookRunner(facebook);
	}

	/**
	 * Login in to Facebook
	 */
	public void loginFacebook() {
		if (isSession()) {
			asyncRunner.request("me", new IDRequestListener());
		} else {
			// no logged in, so relogin
			facebook.authorize(activity, PERMS, new LoginDialogListener());
		}
	}

	/**
	 * Return a Facebook session state
	 * 
	 * @return true if a session is open, false otherwise
	 */
	public boolean isSession() {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		String access_token = sharedPrefs.getString("access_token", "x");
		Long expires = sharedPrefs.getLong("access_expires", -1);
		Log.d(TAG, access_token);

		if (access_token != null && expires != -1) {
			facebook.setAccessToken(access_token);
			facebook.setAccessExpires(expires);
		}
		return facebook.isSessionValid();
	}

	private class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			String token = facebook.getAccessToken();
			long token_expires = facebook.getAccessExpires();
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
			sharedPrefs.edit().putLong("access_expires", token_expires).commit();
			sharedPrefs.edit().putString("access_token", token).commit();
			asyncRunner.request("me", new IDRequestListener());
		}

		@Override
		public void onFacebookError(FacebookError e) {
			Log.d(TAG, "FacebookError: " + e.getMessage());
		}

		@Override
		public void onError(DialogError e) {
			Log.d(TAG, "Error: " + e.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d(TAG, "OnCancel");
		}
	}

	private class IDRequestListener implements RequestListener {
		@Override
		public void onComplete(String response, Object state) {
			Message message = new Message();

			try {
				JSONObject json = Util.parseJson(response);

				userId = json.getString("id"); // Get userId
				userName = json.getString("name"); // Get userName

				message.obj = true;
				HandlerLoginFacebook.sendMessage(message);

			} catch (JSONException e) {
				Log.d(TAG, "JSONException: " + e.getMessage());
				message.obj = false;
				HandlerLoginFacebook.sendMessage(message);

			} catch (FacebookError e) {
				Log.d(TAG, "FacebookError: " + e.getMessage());
				message.obj = false;
				HandlerLoginFacebook.sendMessage(message);
			}
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Log.d(TAG, "IOException: " + e.getMessage());
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			Log.d(TAG, "FileNotFoundException: " + e.getMessage());
		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			Log.d(TAG, "MalformedURLException: " + e.getMessage());
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Log.d(TAG, "FacebookError: " + e.getMessage());
		}
	}


	public Handler HandlerLoginFacebook = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			boolean status = (Boolean) msg.obj;
			setOnLoginFacebook(status);
		}
	};

	/**
	 * Get a friends list of the current Facebook session
	 */
	public void getListOfFriends() {
		Session session = facebook.getSession();
		Request.executeMyFriendsRequestAsync(session, new GraphUserListCallback() {
			@Override
			public void onCompleted(List<GraphUser> users, Response response) {
				if (response.getError() == null) {
					setOnGetFriendsFacebook(users, response);
				} else {
					setOnGetFriendsFacebook(null, response);
				}
			}
		});
	}

	/**
	 * Load an image profile from an user id, into an ImageView
	 * 
	 * @param userId
	 * @param imageView
	 */
	public void loadImageProfileToImageView(String userId, ImageView imageView) {
		String urlImage = "http://graph.facebook.com/" + userId + "/picture?type=large";
		FacebookUtils.fetchImageUrlToImageView(imageView, urlImage);
	}

	/**
	 * Get an user id from the open session of Facebook
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Get an user name from the open session of Facebook
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Get the instance of the current session of Facebook
	 * 
	 * @return
	 */
	public Facebook getFacebook() {
		return facebook;
	}

	/**
	 * 
	 * @param users
	 * @param response
	 */
	private void setOnGetFriendsFacebook(List<GraphUser> users, Response response) {
		if(onGetFriendsFacebookListener != null) {
			onGetFriendsFacebookListener.onGetFriendsFacebook(users, response);
		}
	}

	/**
	 * 
	 * @param onGetFriendsFacebookListener
	 */
	public void setOnGetFriendsFacebookListener(OnGetFriendsFacebookListener onGetFriendsFacebookListener) {
		this.onGetFriendsFacebookListener = onGetFriendsFacebookListener;
	}

	/**
	 * Interface OnGetFriendsFacebookListener
	 * 
	 * @author zace3d
	 *
	 */
	public interface OnGetFriendsFacebookListener {
		public void onGetFriendsFacebook(List<GraphUser> users, Response response);
	}

	/**
	 * 
	 * @param status
	 */
	private void setOnLoginFacebook(boolean status) {
		if(onLoginFacebookListener != null) {
			onLoginFacebookListener.onLoginFacebook(status);
		}
	}

	/**
	 * 
	 * @param onLoginFacebookListener
	 */
	public void setOnLoginFacebookListener(OnLoginFacebookListener onLoginFacebookListener) {
		this.onLoginFacebookListener = onLoginFacebookListener;
	}

	/**
	 * Interface OnLoginFacebookListener
	 * 
	 * @author zace3d
	 *
	 */
	public interface OnLoginFacebookListener {
		public void onLoginFacebook(boolean status);
	}
}