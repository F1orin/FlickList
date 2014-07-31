package ua.com.florin.flicklist.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.com.florin.flicklist.util.EndlessScrollListener;
import ua.com.florin.flicklist.R;

/**
 * Created by florin on 26.07.14.
 */
public class EndlessListTestFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickrTestFragment";

    private ArrayAdapter<String> mAdapter;
    private List<String> mList;

    /**
     * Necessary empty constructor
     */
    public EndlessListTestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mList = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mList);

        fillList(mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_endless_list_test, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.endlessListView);

        mListView.setAdapter(mAdapter);

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore, page: " + page + ", totalItemsCount: " + totalItemsCount);
                for (int i = 0; i < 10; i++) {
                    mList.add("Test entry " + (i + 10 * (page - 1)));
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mListView.setOnScrollListener(endlessScrollListener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.endless_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fill_list:
                fillList(mList);
                return true;
            case R.id.clear_list:
                clearList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearList();
    }

    private void clearList() {
        mList.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void fillList(List<String> list) {
        for (int i = 0; i < 10; i++) {
            list.add("Test entry " + i);
            mAdapter.notifyDataSetChanged();
        }
    }
}
