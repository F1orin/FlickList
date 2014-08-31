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
import ua.com.florin.flicklist.complete.adapter.ImageAdapter;

/**
 * A task that uses Flickr API to get photos urls based on search parameters,
 * and adds them to adapter.
 *
 * Created by florin on 30.07.14.
 */
public class NewLoadPhotoListTask extends AsyncTask<String, Void, PhotoList> {

    /**
     * Logging tag constant
     */
    private static final String TAG = "NewLoadPhotoListTask";

    /**
     * Number of photos to be downloaded at a single call to Flickr API
     */
    public static final int PHOTOS_PER_PAGE = 6;

    /**
     * A search parameter constant representing photo media type
     */
    public static final String MEDIA_TYPE_PHOTOS = "photos";

    private int page;
    private Flickr mFlickr;
    private ImageAdapter mAdapter;

    public NewLoadPhotoListTask(Flickr flickr, int page, ImageAdapter adapter) {
        this.mFlickr = flickr;
        this.page = page;
        this.mAdapter = adapter;
    }

    @Override
    protected PhotoList doInBackground(String... params) {
        PhotoList photoList = null;
        try {
            // build parameters for search request
            SearchParameters searchParameters = new SearchParameters();
            // searchParameters.setTags(params[0]);
            searchParameters.setMedia(MEDIA_TYPE_PHOTOS);
            searchParameters.setText(params[0]);
            // perform search request
            PhotosInterface photosInterface = mFlickr.getPhotosInterface();
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
                mAdapter.add(photo.getLargeUrl());
            }
//            mAdapter.notifyDataSetChanged();
        }
    }
}
