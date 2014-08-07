package ua.com.florin.flicklist.complete.async;

import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import org.json.JSONException;

import java.io.IOException;

import ua.com.florin.flicklist.complete.adapter.FlickListAdapter;

/**
 * A task that uses Flickr API to get photos urls based on search parameters,
 * and adds them to adapter.
 *
 * Created by florin on 30.07.14.
 */
public class LoadPhotoListTask extends AsyncTask<String[], Void, PhotoList> {

    /**
     * Logging tag constant
     */
    private static final String TAG = "LoadPhotoListTask";

    /**
     * Number of photos to be downloaded at a single call to Flickr API
     */
    public static final int PHOTOS_PER_PAGE = 4;

    private int page;
    private Flickr mFlickr;
    private FlickListAdapter mAdapter;

    public LoadPhotoListTask(Flickr flickr, int page, FlickListAdapter adapter) {
        this.mFlickr = flickr;
        this.page = page;
        this.mAdapter = adapter;
    }

    @Override
    protected PhotoList doInBackground(String[]... params) {
        PhotosInterface photosInterface = mFlickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setTags(params[0]);
        searchParameters.setText(params[0][0]);
        PhotoList photoList = null;
        try {
            photoList = photosInterface.search(searchParameters, PHOTOS_PER_PAGE, page);
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

    @Override
    protected void onPostExecute(PhotoList photos) {
        if (photos != null) {
            for (Photo photo : photos) {
                // here the size of photo to be downloaded is determined by the method from Flickr API
                mAdapter.add(photo.getMedium640Url());
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
