package ua.com.florin.flicklist.complete.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.fragment.FlickListFragment;
import ua.com.florin.flicklist.complete.fragment.NavigationDrawerFragment;
import ua.com.florin.flicklist.complete.util.MyConst;

/**
 * Main activity of the application.
 * Sets up a NavigationDrawer.
 * Handles the clicks on NavigationDrawer's items
 * and replaces the corresponding fragments.
 */
public class FlickListActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Logging tag constant
     */
    private static final String TAG = "FlickListActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Instance for persistent storing of app preferences
     */
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flicklist);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // initialize reference to NavigationDrawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_drawer0);
                bundle.putString(MyConst.IMAGE_TAG_KEY, MyConst.IMAGE_TAG_FLOWERS);
                break;
            case 1:
                mTitle = getString(R.string.title_drawer1);
                bundle.putString(MyConst.IMAGE_TAG_KEY, MyConst.IMAGE_TAG_NATURE);
                break;
            case 2:
                mTitle = getString(R.string.title_drawer2);
                bundle.putString(MyConst.IMAGE_TAG_KEY, MyConst.IMAGE_TAG_SPACE);
                break;
        }
        // replace fragments
        Fragment fragment = new FlickListFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().
                replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
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
