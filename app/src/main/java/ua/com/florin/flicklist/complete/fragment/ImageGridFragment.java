package ua.com.florin.flicklist.complete.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.googlecode.flickrjandroid.Flickr;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.adapter.ImageAdapter;
import ua.com.florin.flicklist.complete.async.NewLoadPhotoListTask;
import ua.com.florin.flicklist.complete.util.MyConst;

/**
 * Created by Florin Bicher on 31.08.14.
 */
public class ImageGridFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageGridFragment";

    /**
     * Flickr API key
     */
    private static final String FLICKR_API_KEY = "ba1b93db2b69a3fe588bfb775a600f36";

    /**
     * A search request for photos to be performed in Flickr API
     */
    private String mSearchRequest;

    /**
     * Flickr instance to be used with Flickr API
     */
    private Flickr mFlickr;

    /**
     * Custom adapter for grid view filling
     */
    private ImageAdapter mAdapter;

    /**
     * Necessary empty constructor
     */
    public ImageGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Flickr instance with appropriate key
        mFlickr = new Flickr(FLICKR_API_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate tha layout of the fragment
        final View view = inflater.inflate(R.layout.fragment_image_grid, container, false);

        // find grid view and set its number of columns
        final GridView gridView = (GridView) view.findViewById(R.id.imageGridView);
        int cols = getResources().getDisplayMetrics().widthPixels /
                getResources().getDisplayMetrics().densityDpi;
        gridView.setNumColumns(cols);

        // get tag from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSearchRequest = bundle.getString(MyConst.IMAGE_TAG_KEY);
        }

        // create the adapter and set it to grid view
        mAdapter = new ImageAdapter(getActivity(), mFlickr, mSearchRequest);
        gridView.setAdapter(mAdapter);

        // load the first page of photos
        new NewLoadPhotoListTask(mFlickr, 1, mAdapter).execute(mSearchRequest);

        return view;
    }
}
