package ua.com.florin.flicklist.workflow.async;

import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by florin on 30.07.14.
 */
public class LoadPhotoListTask extends AsyncTask<String[], Void, PhotoList> {

    /**
     * Logging tag constant
     */
    private static final String TAG = "LoadPhotoListTask";

    public static final int PHOTOS_PER_PAGE = 4;

    private int page;
    private Flickr mFlickr;

    public LoadPhotoListTask(Flickr flickr, int page) {
        this.mFlickr = flickr;
        this.page = page;
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
}
