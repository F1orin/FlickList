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

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.async.LoadPhotoListTask;
import ua.com.florin.flicklist.util.EndlessScrollListener;
import ua.com.florin.flicklist.acomplete.util.ImageCache;
import ua.com.florin.flicklist.acomplete.util.ImageFetcher;
import ua.com.florin.flicklist.view.RecyclingImageView;

/**
 * Created by florin on 30.07.14.
 */
public class EndlessFlickrTestFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "EndlessFlickrTestFragment";
    private static final String IMAGE_CACHE_DIR = "imgCache";
    private static final String FLICKR_API_KEY = "ba1b93db2b69a3fe588bfb775a600f36";
    private static final String IMAGE_TAG = "Android";

    private int mImageSize;
    //reference to custom adapter for list filling
    private ua.com.florin.flicklist.adapter.ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private List<String> mImageList;
    private Flickr mFlickr;


    /**
     * Necessary empty constructor
     */
    public EndlessFlickrTestFragment() {
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
        final int longest = (height > width ? height : width) / 2;

        mImageList = loadImageList(1, IMAGE_TAG);

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
        final View view = inflater.inflate(R.layout.fragment_endless_flickr_test, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.flickrEndlessListView);
        final View footerLayout = inflater.inflate(R.layout.footer_loading, null);

        mListView.addFooterView(footerLayout);

        mAdapter = new ua.com.florin.flicklist.adapter.ImageAdapter(getActivity(), mImageList, mImageFetcher);
        mListView.setAdapter(mAdapter);

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                List<String> urlsToAdd = loadImageList(page, IMAGE_TAG);
//                Log.d(TAG, "page:" + page + ", total:" + totalItemsCount);
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

    private List<String> loadImageList(int page, String tag) {
        List<String> imageUrlList = new ArrayList<String>(LoadPhotoListTask.PHOTOS_PER_PAGE);
        LoadPhotoListTask loadPhotoListTask = new LoadPhotoListTask(mFlickr, page);
        // search tag parameters
        String[] params = {tag};
        loadPhotoListTask.execute(params);
        try {
            PhotoList photoList = loadPhotoListTask.get();
            if (photoList != null) {
                // if list is not empty add all links to our url holder
                for (Photo p : photoList) {
                    // here the size of image to be downloaded is defined
                    imageUrlList.add(p.getMedium640Url());
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return imageUrlList;
    }

    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private final List<String> images;

        private ImageAdapter(Context context, List<String> images) {
            mContext = context;
            this.images = images;
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
