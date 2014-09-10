package ua.com.florin.flicklist.complete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.koushikdutta.ion.Ion;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.async.LoadPhotoListTask;
import ua.com.florin.flicklist.complete.view.SquaredImageView;

/**
 * Created by Florin Bicher on 31.08.14.
 */
public class ImageAdapter extends ArrayAdapter<String> {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageAdapter";

    /**
     * Counter of pages to load the next when user scrolls to the bottom of the screen
     */
    private int pageCounter = 2;

    /**
     * Application's context
     */
    private final Context mContext;

    /**
     * Flickr instance
     */
    private final Flickr mFlickr;

    /**
     * A search request for photos to be performed in Flickr API
     */
    private final String mSearchRequest;


    public ImageAdapter(Context context, Flickr flickr, String searchRequest) {
        super(context, 0);
        this.mContext = context;
        this.mFlickr = flickr;
        this.mSearchRequest = searchRequest;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // if the last item is shown on the screen, load the next pack of photos
        if (position > getCount() - 4) {
            new LoadPhotoListTask(mFlickr, pageCounter, this).execute(mSearchRequest);
            pageCounter++;
        }

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.flickr_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ImageView imageView = holder.squaredImageView;

        Ion.with(imageView)
                .resize(512, 512)
                .centerCrop()
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.error)
                .load(getItem(position));

        return convertView;
    }

    /**
     * A holder for implementing the ViewHolder pattern
     */
    static class ViewHolder {
        /**
         * Image view to be filled with downloaded from FLickr photo
         */
        @InjectView(R.id.flickrSquaredImageView)
        SquaredImageView squaredImageView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
