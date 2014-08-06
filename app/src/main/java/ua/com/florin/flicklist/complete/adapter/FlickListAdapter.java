package ua.com.florin.flicklist.complete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;

import java.util.List;

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
    private final String[] mImageTags;

    /**
     * Counter of pages to load the next when user scrolls to the bottom of the screen
     */
    private int pageCounter = 2;

    public FlickListAdapter(Context context, Flickr flickr,
                            List<String> images, ImageFetcher fetcher, String[] tags) {
        this.mContext = context;
        this.mFlickr = flickr;
        this.mImages = images;
        this.mImageFetcher = fetcher;
        this.mImageTags = tags;
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
            new LoadPhotoListTask(mFlickr, pageCounter, this).execute(mImageTags);
            pageCounter++;
        }

        ViewHolder viewHolder;
        ImageView imageView;
        if (convertView == null) {
            // inflate the new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.test_list_row, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.flickrImageView);
            viewHolder = new ViewHolder(imageView);
            convertView.setTag(viewHolder);
        } else {
            // get view from the existing view holder
            viewHolder = (ViewHolder) convertView.getTag();
            imageView = viewHolder.imageView;
        }

        mImageFetcher.loadImage(mImages.get(position), imageView);

        return convertView;
    }

    /**
     * A holder for implementing the ViewHolder pattern
     */
    private static class ViewHolder {
        /**
         * Image view to be filled with downloaded from FLickr photo
         */
        public final ImageView imageView;

        private ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
