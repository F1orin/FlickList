package ua.com.florin.flicklist.acomplete.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;

import java.util.List;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.acomplete.async.LoadPhotoListTask;
import ua.com.florin.flicklist.acomplete.util.ImageFetcher;

/**
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
        if (position == getCount() - 1) {
            Log.d(TAG, "Load new image page: " + pageCounter);
            new LoadPhotoListTask(mFlickr, pageCounter, this).execute(mImageTags);
            pageCounter++;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.test_list_row, parent, false);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.flickrImageView);

            convertView.setTag(new ViewHolder(imageView));
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        mImageFetcher.loadImage(mImages.get(position), viewHolder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        public final ImageView imageView;

        private ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
