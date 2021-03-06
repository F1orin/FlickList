package ua.com.florin.flicklist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.googlecode.flickrjandroid.Flickr;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.activity.ImageDetailActivity;
import ua.com.florin.flicklist.adapter.ImageAdapter;
import ua.com.florin.flicklist.async.LoadPhotoListTask;
import ua.com.florin.flicklist.util.MyConst;

/**
 * A fragment that fills the container of the main application's activity
 * and shows the grid of the loaded photos.
 * <p/>
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

    /**
     * Creates new instance of {@link ua.com.florin.flicklist.fragment.ImageGridFragment}
     * for the given search request
     *
     * @param searchRequest a search request for image category
     * @return newly created fragment
     */
    public static ImageGridFragment newInstance(String searchRequest) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle b = new Bundle();
        b.putString(MyConst.IMAGE_TAG_KEY, searchRequest);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // get tag from bundle
        if (getArguments() != null) {
            mSearchRequest = getArguments().getString(MyConst.IMAGE_TAG_KEY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Flickr instance with appropriate key
        mFlickr = new Flickr(FLICKR_API_KEY);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate tha layout of the fragment
        final View view = inflater.inflate(R.layout.fragment_image_grid, container, false);

        // find grid view and set its number of columns
        final GridView gridView = (GridView) view.findViewById(R.id.imageGridView);
        int cols = getResources().getDisplayMetrics().widthPixels /
                getResources().getDisplayMetrics().densityDpi;
        gridView.setNumColumns(cols);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                intent.putExtra(MyConst.EXTRA_IMAGE_POSITION, position);
                intent.putExtra(MyConst.EXTRA_IMAGE_URL_ARRAY, getImageAdapterElements());
                startActivity(intent);
            }
        };
        gridView.setOnItemClickListener(itemClickListener);

        // create the adapter and set it to grid view
        mAdapter = new ImageAdapter(getActivity(), mFlickr, mSearchRequest);
        gridView.setAdapter(mAdapter);

        // load the first page of photos
        new LoadPhotoListTask(mFlickr, 1, mAdapter).execute(mSearchRequest);

        return view;
    }

    private String[] getImageAdapterElements() {
        int size = mAdapter.getCount();
        String[] elements = new String[size];
        for (int i = 0; i < size; i++) {
            elements[i] = mAdapter.getItem(i);
        }
        return elements;
    }
}
