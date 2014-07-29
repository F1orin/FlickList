package ua.com.florin.flicklist.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.FlickrTestFragment;
import ua.com.florin.flicklist.fragment.ImageListFragment;

/**
 * Created by florin on 27.07.14.
 */
public class FlickrTestActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickrTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_test);

        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, new FlickrTestFragment(), TAG);
            fragmentTransaction.commit();
        }
    }
}
