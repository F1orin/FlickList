package ua.com.florin.flicklist.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.com.florin.flicklist.EndlessScrollListener;
import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.ImageListFragment;


public class MainActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create new fragment only if it doesn't exist
        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, new ImageListFragment(), TAG);
            fragmentTransaction.commit();
        }

//        setContentView(R.layout.activity_main);
//
//        ListView listView = (ListView) findViewById(R.id.mainListView);
//
//        List<String> mList = new ArrayList<String>();
//        for (int i = 0; i < 11; i++) {
//            mList.add("Test string " + i);
//        }
//
//        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                mList);
//
//        listView.setAdapter(stringArrayAdapter);
//
//        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                Log.d(TAG, "onLoadMore, page: " + page + ", totalItemsCount: " + totalItemsCount);
//                for (int i = (page - 1) * 10 + 1; i < (page - 1) * 10 + 11; i++) {
//                    stringArrayAdapter.add("Added string " + i);
//                }
//            }
//        };
//        listView.setOnScrollListener(endlessScrollListener);

    }
}
