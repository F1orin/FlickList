package ua.com.florin.flicklist.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ua.com.florin.flicklist.BuildConfig;
import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.ImageGridFragment;
import ua.com.florin.flicklist.util.MyConst;

/**
 * Main activity of the application.
 * Sets up a NavigationDrawer.
 * Handles the clicks on NavigationDrawer's items
 * and replaces the corresponding fragments.
 */
public class ImageGridActivity extends FragmentActivity {

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageGridActivity";

    /**
     * A list of categories which is displayed in spinner
     */
    private List<String> mCategories;

    /**
     * An adapter for the ViewPager
     */
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        ButterKnife.inject(this);

        // get categories from preferences
        mCategories = loadCategoriesFromPrefs();
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(MyConst.PREF_NAME_GENERAL, MODE_PRIVATE);
        int currentPosition = prefs.getInt(MyConst.KEY_CURRENT_PAGER_POS, 0);
        mViewPager.setCurrentItem(currentPosition);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveArrayToPrefs(mCategories.toArray(new String[mCategories.size()]),
                MyConst.CATEGORY_ARRAY_NAME,
                MyConst.PREF_NAME_CATEGORY,
                this);
        savePagerPositionToPrefs(mViewPager.getCurrentItem(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                addNewCategory();
                return true;
            case R.id.action_delete:
                deleteCategory();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows alert dialog for adding new category to pager. Manages the adding process.
     */
    private void addNewCategory() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.add_category_string);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setHint(R.string.add_category_hint);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // get the category that user has inserted
                String value = input.getText().toString();
                // add it to the list of categories
                mCategories.add(value);
                Collections.sort(mCategories);
                // notify the adapter that data has changed
                mViewPagerAdapter.notifyDataSetChanged();
                // select the category that user has added
                mViewPager.setCurrentItem(mCategories.indexOf(value));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    /**
     * Deletes category of images from pager and performs actions to show the next category to user
     */
    private void deleteCategory() {
        // identify the position and title of the selected category
        int position = mViewPager.getCurrentItem();
        String title = mCategories.get(position);
        // remove the category from the list
        mCategories.remove(position);
        // notify the adapter that list has changed
        mViewPagerAdapter.notifyDataSetChanged();
        // make toast notification
        Toast.makeText(this, title + " deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads image categories from shared preferences if there are any, otherwise fills
     * them with default values defined in resources
     *
     * @return a list of strings with categories
     */
    private List<String> loadCategoriesFromPrefs() {
        List<String> mList;
        String[] categories = loadArrayFromPrefs(MyConst.CATEGORY_ARRAY_NAME, MyConst.PREF_NAME_CATEGORY, this);
        if (categories.length != 0) {
            mList = new ArrayList<String>(Arrays.asList(categories));
        } else {
            mList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.default_categories)));
        }
        Collections.sort(mList);
        return mList;
    }

    /**
     * Saves given array of strings to Shared Preferences
     *
     * @param array     an array that should be saved
     * @param arrayName a name that will be used as array name in preferences
     * @param prefName  a name of the shared preference file
     * @param context   application's context
     * @return true if the new values were successfully written to persistent storage
     */
    private boolean saveArrayToPrefs(String[] array, String arrayName, String prefName, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // clear the previous array in preferences
        editor.clear();
        // write new array to preferences
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    /**
     * Loads a string array from shared preferences
     *
     * @param arrayName a name of the array that has been saved in preferences
     * @param prefName  a name of the shared preference file
     * @param mContext  application's context
     * @return an array of strings loaded from shared preferences
     */
    private String[] loadArrayFromPrefs(String arrayName, String prefName, Context mContext) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "loadArrayFromPrefs");
        }
        SharedPreferences prefs = mContext.getSharedPreferences(prefName, MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    /**
     * Saves current selected position of ViewPager to shared preferences
     *
     * @param pagerPos selected position
     * @param context  application's context
     * @return true on operation success
     */
    private boolean savePagerPositionToPrefs(int pagerPos, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyConst.PREF_NAME_GENERAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(MyConst.KEY_CURRENT_PAGER_POS, pagerPos);
        return editor.commit();
    }

    /**
     * An adapter for the view pager that
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            return ImageGridFragment.newInstance(mCategories.get(i));
        }

        @Override
        public int getCount() {
            return mCategories.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCategories.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
