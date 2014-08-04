package ua.com.florin.flicklist.acomplete.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.acomplete.adapter.FlickListAdapter;
import ua.com.florin.flicklist.acomplete.async.LoadPhotoListTask;
import ua.com.florin.flicklist.acomplete.util.ImageCache;
import ua.com.florin.flicklist.acomplete.util.ImageFetcher;
import ua.com.florin.flicklist.acomplete.util.MyConst;

/**
 * Created by florin on 03.08.14.
 */
public class FlickListFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickListFragment";

    private static final String IMAGE_CACHE_DIR = "imgCache";
    private static final String FLICKR_API_KEY = "ba1b93db2b69a3fe588bfb775a600f36";

    private String[] mImageTags;
    //reference to custom adapter for list filling
    private FlickListAdapter mAdapter;
    //    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private List<String> mImageList;
    private Flickr mFlickr;


    /**
     * Necessary empty constructor
     */
    public FlickListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // notify that the fragment wants to add options in action bar
        setHasOptionsMenu(true);

        mFlickr = new Flickr(FLICKR_API_KEY);

        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher loads images to ListView asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), longest);
        mImageFetcher.addImageCache(getActivity().getFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flicklist, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.flickListListView);
        final View footerLayout = inflater.inflate(R.layout.footer_loading, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mImageTags = bundle.getStringArray(MyConst.IMAGE_TAGS_KEY);
            Log.d(TAG, mImageTags[0]);
        }

        mImageList = new ArrayList<String>();

        mListView.addFooterView(footerLayout);

        mAdapter = new FlickListAdapter(getActivity(), mFlickr, mImageList, mImageFetcher, mImageTags);
        mListView.setAdapter(mAdapter);

        new LoadPhotoListTask(mFlickr, 1, mAdapter).execute(mImageTags);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.flicklist_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}