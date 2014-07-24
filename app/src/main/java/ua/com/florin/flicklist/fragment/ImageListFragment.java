package ua.com.florin.flicklist.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.util.ImageCache;
import ua.com.florin.flicklist.util.ImageResizer;
import ua.com.florin.flicklist.util.ImageWorker;
import ua.com.florin.flicklist.util.MyConst;
import ua.com.florin.flicklist.view.RecyclingImageView;

/**
 * Created by florin on 22.07.14.
 */
public class ImageListFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageListFragment";
    private static final String IMAGE_CACHE_DIR = "imgCache";

    private int mImageSize;
    //reference to custom adapter for list filling
    private ImageAdapter mAdapter;
    private ImageResizer mImageResizer;

    /**
     * Necessary empty constructor
     */
    public ImageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // notify that the fragment wants to add options in action bar
        setHasOptionsMenu(true);

        // set image size from resources
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);

        // initialize adapter
        mAdapter = new ImageAdapter(getActivity(), MyConst.imageSet);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageWorker loads images to ListView asynchronously
        mImageResizer = new ImageResizer(getActivity(), mImageSize);
        mImageResizer.setLoadingImage(R.drawable.empty_photo);
        mImageResizer.addImageCache(getActivity().getFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.image_list_fragment, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageResizer.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageResizer.setPauseWork(false);
        mImageResizer.setExitTasksEarly(true);
        mImageResizer.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageResizer.closeCache();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageResizer.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private final int[] images;

        private ImageAdapter(Context context, int[] images) {
            mContext = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
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
            mImageResizer.loadImage(MyConst.imageSet[position], imageView);

            return imageView;
        }


    }
}
