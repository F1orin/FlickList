package ua.com.florin.flicklist.activity;

import android.app.Activity;
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


public class MainActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.mainListView);

        List<String> mList = new ArrayList<String>();
        for (int i = 0; i < 11; i++) {
            mList.add("Test string " + i);
        }

        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mList);

        listView.setAdapter(stringArrayAdapter);

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore, page: " + page + ", totalItemsCount: " + totalItemsCount);
                for (int i = (page - 1) * 10 + 1; i < (page - 1) * 10 + 11; i++) {
                    stringArrayAdapter.add("Added string " + i);
                }
            }
        };
        listView.setOnScrollListener(endlessScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
