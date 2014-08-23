package ua.com.florin.flicklist.complete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.async.LoadPhotoListTask;
import ua.com.florin.flicklist.complete.util.ImageFetcher;

/**
 * Custom adapter that holds image URLs and calls ImageFetcher method
 * to load images to their views asynchronously.
 * <p/>
 * It is also responsible for loading new packs of images when
 * the last view in adapter is shown on the screen.
 * <p/>
 * Created by florin on 04.08.14.
 */
public class FlickListAdapter extends BaseAdapter {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickListAdapter";

    private final Context mContext;
    private final Flickr mFlickr;
    private final List<String> mImages;
    private final ImageFetcher mImageFetcher;
    private final String mImageTag;

    /**
     * Counter of pages to load the next when user scrolls to the bottom of the screen
     */
    private int pageCounter = 2;

    public FlickListAdapter(Context context, Flickr flickr,
                            List<String> images, ImageFetcher fetcher, String tag) {
        this.mContext = context;
        this.mFlickr = flickr;
        this.mImages = images;
        this.mImageFetcher = fetcher;
        this.mImageTag = tag;
    }

    public boolean add(String s) {
        return mImages.add(s);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
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

        // if the last item is shown on the screen, load the next pack of photos
        if (position == getCount() - 1) {
            new LoadPhotoListTask(mFlickr, pageCounter, this).execute(mImageTag);
            pageCounter++;
        }

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ImageView imageView = holder.imageView;

//        mImageFetcher.loadImage(mImages.get(position), imageView);
        Picasso.with(mContext).setIndicatorsEnabled(true);
        Picasso.with(mContext).load(mImages.get(position)).placeholder(R.drawable.empty_photo).into(imageView);

        return convertView;
    }

    /**
     * A holder for implementing the ViewHolder pattern
     */
    static class ViewHolder {
        /**
         * Image view to be filled with downloaded from FLickr photo
         */
        @InjectView(R.id.flickrImageView) ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
