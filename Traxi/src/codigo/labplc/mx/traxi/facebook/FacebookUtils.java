package codigo.labplc.mx.traxi.facebook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import codigo.labplc.mx.traxi.R;

/**
 * 
 * @author zace3d
 *
 */
public class FacebookUtils {
	
	/**
	 * Fetch an image from url
	 * 
	 * @param imageProfileFriend
	 * @param urlImage
	 */
	public static void fetchImageUrlToImageView(ImageView imageProfileFriend, String urlImage) {
		DownloadImageTask task = new FacebookUtils().new DownloadImageTask(imageProfileFriend);
		task.execute(urlImage);
	}
	
	/**
	 * Thread to download an image from an url and change the data to an ImageView
	 * 
	 * @author zace3d
	 *
	 */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    private ImageView imageView;

	    /**
	     * Constructor
	     * 
	     * @param imageView
	     */
	    public DownloadImageTask(ImageView imageView) {
	        this.imageView = imageView;
	    }

	    @Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			
			InputStream inputStream = retrieveStream(urldisplay);
			if(inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		        Log.e("Bitmap","returned");
		        
		        return bitmap;
			} else {
				return null;
			}
	    }

		@Override
	    protected void onPostExecute(Bitmap result) {
	    	if(result != null) {
	    		imageView.setScaleType(ScaleType.CENTER_CROP);
	    		imageView.setImageBitmap(getRoundedShape(result)); //Rounded ImageView
	    		//imageView.setImageBitmap(result); //Normal ImageView
	    	} else {
	    		imageView.setImageResource(R.drawable.ic_launcher);
	    	}
	    }
	}
	
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = scaleBitmapImage.getWidth() < 200 ? 200 : scaleBitmapImage.getWidth();
		int targetHeight = scaleBitmapImage.getHeight() < 200 ? 200 : scaleBitmapImage.getHeight();
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
	
	/**
	 * Retrieve an InputStream from an url.
	 * 
	 * @param url Location from source
	 * @return InputStream data of the source
	 */
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(url);

		try {
			HttpResponse httpResponse = client.execute(httpRequest);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error => " + statusCode + " => for URL " + url);
				return null;
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			return httpEntity.getContent();

		} catch (IOException e) {
			httpRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL =>" + url, e);
		}

		return null;
	}
}
