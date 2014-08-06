package ua.com.florin.flicklist.workflow.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.workflow.fragment.EndlessListTestFragment;

/**
 * Created by florin on 26.07.14.
 */
public class EndlessListActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "EndlessListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endless_list);

        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, new EndlessListTestFragment(), TAG);
            fragmentTransaction.commit();
        }
    }
}
