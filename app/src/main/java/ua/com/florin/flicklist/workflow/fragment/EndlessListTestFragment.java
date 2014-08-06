package ua.com.florin.flicklist.workflow.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.workflow.util.EndlessScrollListener;

/**
 * Created by florin on 26.07.14.
 */
public class EndlessListTestFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "EndlessListTestFragment";

    private EntryAdapter mAdapter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_endless_list_test, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.endlessListView);

        final View footerLautout = inflater.inflate(R.layout.footer_loading, null);
        mListView.addFooterView(footerLautout);

        mAdapter = new EntryAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        new LoaderTask().execute();

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
//        mListView.setOnScrollListener(endlessScrollListener);

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
            list.add(fetch() + i);
        }
    }

    private String fetch() {
        synchronized (this) {
            try {
                wait(400);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return "Test entry ";
    }

    private class EntryAdapter extends ArrayAdapter<String> {

        Context mContext;

        public EntryAdapter(Context context) {
            super(context, android.R.layout.two_line_list_item);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == getCount() - 1) {
                LoaderTask task = new LoaderTask();
                task.execute();
            }

            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_list_row, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.testTextView);
                holder = new Holder(textView);
                convertView.setTag(holder);
            }

            String s = getItem(position);
            holder = (Holder) convertView.getTag();
            holder.textView.setText(s);

            return convertView;
        }


    }

    private static class Holder {
        public final TextView textView;

        private Holder(TextView textView) {
            this.textView = textView;
        }
    }

    private class LoaderTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> mList = new ArrayList<String>();
            fillList(mList);
            return mList;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);

            for (String s : strings) {
                mAdapter.add(s);
            }

            mAdapter.notifyDataSetChanged();
        }
    }
}
