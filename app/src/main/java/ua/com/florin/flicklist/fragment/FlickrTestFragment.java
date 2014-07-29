package ua.com.florin.flicklist.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.googlecode.flickrjandroid.test.TestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import ua.com.florin.flicklist.R;

/**
 * Created by florin on 26.07.14.
 */
public class FlickrTestFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickrTestFragment";

    private static final String FLICKR_API_KEY = "ba1b93db2b69a3fe588bfb775a600f36";
    private static final String FLICKR_API_SECRET = "bf1831b39c53245b";

    private ImageView mImageView;
    private Flickr mFlickr;

    /**
     * Necessary empty constructor
     */
    public FlickrTestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mFlickr = new Flickr(FLICKR_API_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.flickr_test_fragment, container, false);
        mImageView = (ImageView) view.findViewById(R.id.flickrImageView);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.flickr_test_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                // TODO remove test piece of code
                // load new image here
                String url = "https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg";
                new DownloadImageTask(mImageView).execute(url);
                return true;
            case R.id.flickrTest:
                Log.d(TAG, "flickrTest started");
                LoadPhotoTask loadPhotoTask = new LoadPhotoTask();
                loadPhotoTask.execute(new String[]{"Nature"});
                try {
                    PhotoList photoList = loadPhotoTask.get();
                    Photo photo = photoList.get(0);
                    String photoUrl = photo.getLargeUrl();
                    Log.d(TAG, "photoUrl=" + photoUrl);
                    new DownloadImageTask(mImageView).execute(photoUrl);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.e(TAG, "ExecutionException");
                    e.printStackTrace();
                }
                return true;
/*            case R.id.flickrTest:
                Log.d(TAG, "flickrTest started");
                TestFlickrConnectionTask task = new TestFlickrConnectionTask();
                task.execute();
                try {
                    JSONObject jsonObject = task.get();
                    Log.d(TAG, "object: " + jsonObject);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.e(TAG, "ExecutionException");
                    e.printStackTrace();
                }
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadPhotoTask extends AsyncTask<String[], Void, PhotoList> {

        @Override
        protected PhotoList doInBackground(String[]... params) {
            PhotosInterface photosInterface = mFlickr.getPhotosInterface();
            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setTags(params[0]);
            PhotoList photoList = null;
            try {
                photoList = photosInterface.search(searchParameters, 1, 1);
            } catch (IOException e) {
                Log.e(TAG, "IOException");
                e.printStackTrace();
            } catch (FlickrException e) {
                Log.e(TAG, "FlickrException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "JSONException");
                e.printStackTrace();
            }
            return photoList;
        }
    }

    // test class to load single image from url
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String sourceUrl = urls[0];
            Bitmap mImage = null;
            try {
                InputStream inputStream = new URL(sourceUrl).openStream();
                mImage = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mImage;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private class TestFlickrConnectionTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            TestInterface testInterface = mFlickr.getTestInterface();
            JSONObject jsonObject = null;
            try {
                jsonObject = testInterface.echo(Collections.EMPTY_LIST);
            } catch (IOException e) {
                Log.e(TAG, "IOException");
                e.printStackTrace();
            } catch (FlickrException e) {
                Log.e(TAG, "FlickrException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "JSONException");
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
}
