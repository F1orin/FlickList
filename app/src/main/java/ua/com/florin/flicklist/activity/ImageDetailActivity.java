package ua.com.florin.flicklist.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.ImageDetailFragment;
import ua.com.florin.flicklist.util.MyConst;

/**
 * Created by Florin Bicher on 31.08.14.
 */
public class ImageDetailActivity extends FragmentActivity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageDetailActivity";

    private ImagePagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private String[] mImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_image_detail);

        mImageUrls = getIntent().getExtras().getStringArray(MyConst.EXTRA_IMAGE_URL_ARRAY);

        mPagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mImageUrls.length);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);

        // Set up activity to go full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the current item based on the extra passed in to this activity
        final int extraCurrentItem = getIntent().getIntExtra(MyConst.EXTRA_IMAGE_POSITION, -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(mImageUrls[position]);
        }
    }
}
