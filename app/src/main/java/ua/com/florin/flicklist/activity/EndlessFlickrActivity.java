package ua.com.florin.flicklist.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.EndlessFlickrTestFragment;

/**
 * Created by florin on 30.07.14.
 */
public class EndlessFlickrActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "EndlessFlickrActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, new EndlessFlickrTestFragment(), TAG);
            fragmentTransaction.commit();
        }
    }
}
