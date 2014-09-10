package ua.com.florin.flicklist.complete.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.util.MyConst;

/**
 * Created by Florin Bicher on 09.09.14.
 */
public class ImageDetailFragment extends Fragment {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageDetailFragment";

    private String mImageUrl;

    private ImageView mImageView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment fragment = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString(MyConst.EXTRA_IMAGE_URL, imageUrl);
        fragment.setArguments(args);

        return fragment;
    }

    public ImageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(MyConst.EXTRA_IMAGE_URL) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Ion.with(mImageView)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.error)
                .load(mImageUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            mImageView.setImageDrawable(null);
        }
    }
}
