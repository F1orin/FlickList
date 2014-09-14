package ua.com.florin.flicklist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.fragment.ImageGridFragment;
import ua.com.florin.flicklist.util.MyConst;

/**
 * Main activity of the application.
 * Sets up a NavigationDrawer.
 * Handles the clicks on NavigationDrawer's items
 * and replaces the corresponding fragments.
 */
public class ImageGridActivity extends Activity {
    @InjectView(R.id.categorySpinner)
    Spinner mCategorySpinner;

    /**
     * Logging tag constant
     */
    private static final String TAG = "ImageGridActivity";

    /**
     * An adapter that is used in spinner with categories
     */
    private ArrayAdapter<String> mSpinnerAdapter;

    /**
     * A list of categories which is displayed in spinner
     */
    private List<String> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        ButterKnife.inject(this);

        // get categories from preferences
        mCategories = loadCategoriesFromPrefs();

        // Create an ArrayAdapter using the string array and a default spinner layout
        mSpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategories);
        // Specify the layout to use when the list of choices appears
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(mSpinnerAdapter);

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // define the selected category
                String selectedCategory = (String) parent.getItemAtPosition(position);
                loadFragment(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mCategorySpinner.setOnItemSelectedListener(spinnerListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveArrayToPrefs(mCategories.toArray(new String[mCategories.size()]),
                MyConst.CATEGORY_ARRAY_NAME,
                MyConst.CATEGORY_PREF_NAME,
                this);
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
        switch (item.getItemId()) {
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
     * Loads fragment for the selected category into container
     *
     * @param category a string of selected category for which search will be performed
     */
    private void loadFragment(String category) {
        Bundle bundle = new Bundle();
        bundle.putString(MyConst.IMAGE_TAG_KEY, category);
        // replace fragments
        Fragment fragment = new ImageGridFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().
                replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Shows alert dialog for adding new category to spinner. Manages the adding process.
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
                // notify the adapter that data has changed
                mSpinnerAdapter.notifyDataSetChanged();
                // sort the categories in spinner
                Collections.sort(mCategories);
                // select the category that user has added
                mCategorySpinner.setSelection(mSpinnerAdapter.getPosition(value));
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
     * Deletes category of images from spinner and performs actions to show the next category to user
     */
    private void deleteCategory() {
        // identify the position of selected category
        int position = mCategorySpinner.getSelectedItemPosition();
        // remove the category from the list
        mCategories.remove(position);
        // notify the adapter that list has changed
        mSpinnerAdapter.notifyDataSetChanged();
        // if the deleted item is the last avoid IndexOutOfBoundsException by setting the last
        // item selected manually
        if (position == mCategorySpinner.getCount()) {
            mCategorySpinner.setSelection(position - 1);
        }
        // identify the next selected category
        String category = (String) mCategorySpinner.getSelectedItem();
        // show the images of the next selected category to the user
        loadFragment(category);
    }

    /**
     * Loads image categories from shared preferences if there are any, otherwise fills
     * them with default values defined in resources
     *
     * @return a list of strings with categories
     */
    private List<String> loadCategoriesFromPrefs() {
        List<String> mList;
        String[] categories = loadArrayFromPrefs(MyConst.CATEGORY_ARRAY_NAME, MyConst.CATEGORY_PREF_NAME, this);
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
     * @param mContext  application's context
     * @return true if the new values were successfully written to persistent storage
     */
    private boolean saveArrayToPrefs(String[] array, String arrayName, String prefName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(prefName, MODE_PRIVATE);
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
        Log.d(TAG, "loadArrayFromPrefs");
        SharedPreferences prefs = mContext.getSharedPreferences(prefName, MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }
}
