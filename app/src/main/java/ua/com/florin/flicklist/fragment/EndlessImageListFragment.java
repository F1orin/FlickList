package ua.com.florin.flicklist.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.util.EndlessScrollListener;
import ua.com.florin.flicklist.acomplete.util.ImageCache;
import ua.com.florin.flicklist.acomplete.util.ImageFetcher;
import ua.com.florin.flicklist.util.MyConst;
import ua.com.florin.flicklist.view.RecyclingImageView;

/**
 * Created by florin on 29.07.14.
 */
public class EndlessImageListFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "EndlessImageListFragment";
    private static final String IMAGE_CACHE_DIR = "imgCache";

    private int mImageSize;
    //reference to custom adapter for list filling
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private List<String> mImageList;

    /**
     * Necessary empty constructor
     */
    public EndlessImageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // notify that the fragment wants to add options in action bar
        setHasOptionsMenu(true);

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
        final int longest = (height > width ? height : width) / 2;

        mImageList = new LinkedList<String>(Arrays.asList(MyConst.imageUrlsShort));

        // initialize adapter
        mAdapter = new ImageAdapter(getActivity(), mImageList);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageResizer loads images to ListView asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), longest);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.imageListView);
        mListView.setAdapter(mAdapter);

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                List<String> urlsToAdd = Arrays.asList(MyConst.imageUrlsShort);
                Log.d(TAG, "page:" + page + ", total:" + totalItemsCount);
                mImageList.addAll(urlsToAdd);
                mAdapter.notifyDataSetChanged();
            }
        };
        mListView.setOnScrollListener(endlessScrollListener);

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
        inflater.inflate(R.menu.main_menu, menu);
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

    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private final List<String> images;
        private LayoutInflater mInflater;

        private ImageAdapter(Context context, List<String> images) {
            mContext = context;
            this.images = images;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Now handle the main ImageView thumbnails
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(mContext);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            // set image to list item
            mImageFetcher.loadImage(images.get(position), imageView);

            return imageView;
        }


    }
}
