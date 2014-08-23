package ua.com.florin.flicklist.complete.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.fragment.FlickListFragment;
import ua.com.florin.flicklist.complete.util.MyConst;

/**
 * Main activity of the application.
 * Sets up a NavigationDrawer.
 * Handles the clicks on NavigationDrawer's items
 * and replaces the corresponding fragments.
 */
public class FlickListActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickListActivity";

    /**
     * Instance for persistent storing of app preferences
     */
    private SharedPreferences mPreferences;

    private String[] mDefaultCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flicklist);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mDefaultCategories = getResources().getStringArray(R.array.default_categories);

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.default_categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(spinnerAdapter);

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // define the selected category
                String selectedCategory = (String) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString(MyConst.IMAGE_TAG_KEY, selectedCategory);
                // replace fragments
                Fragment fragment = new FlickListFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().
                        replace(R.id.container, fragment)
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        categorySpinner.setOnItemSelectedListener(spinnerListener);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
