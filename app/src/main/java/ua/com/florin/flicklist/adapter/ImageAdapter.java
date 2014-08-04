package ua.com.florin.flicklist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ua.com.florin.flicklist.acomplete.util.ImageFetcher;
import ua.com.florin.flicklist.view.RecyclingImageView;

/**
 * Created by florin on 01.08.14.
 */
public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<String> mImages;
    private final ImageFetcher mImageFetcher;

    public ImageAdapter(Context context, List<String> images, ImageFetcher fetcher) {
        this.mContext = context;
        this.mImages = images;
        this.mImageFetcher = fetcher;
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

        ImageView imageView;
        if (convertView == null) { // if it's not recycled, instantiate and initialize
            imageView = new RecyclingImageView(mContext);
        } else { // Otherwise re-use the converted view
            imageView = (ImageView) convertView;
        }

        // set image to list item
        mImageFetcher.loadImage(mImages.get(position), imageView);

        return imageView;
    }


}