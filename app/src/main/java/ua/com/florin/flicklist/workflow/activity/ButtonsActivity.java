package ua.com.florin.flicklist.workflow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.com.florin.flicklist.R;
import ua.com.florin.flicklist.complete.activity.FlickListActivity;

/**
 * An Activity with buttons to run testing components of the application
 */
public class ButtonsActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "ButtonsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        // find buttons
        Button endlessListButton = (Button) findViewById(R.id.endlessListButton);
        Button imageListButton = (Button) findViewById(R.id.imageListButton);
        Button flickrTestButton = (Button) findViewById(R.id.flickrTestButton);
        Button endlessImageListTestButton = (Button) findViewById(R.id.endlessImageListTestButton);
        Button endlessFlickrTestButton = (Button) findViewById(R.id.endlessFlickrTestButton);
        Button flickListButton = (Button) findViewById(R.id.flickListButton);

        // create listener for buttons
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.endlessListButton:
                        intent = new Intent(ButtonsActivity.this, EndlessListActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.imageListButton:
                        intent = new Intent(ButtonsActivity.this, ImageListActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.flickrTestButton:
                        intent = new Intent(ButtonsActivity.this, FlickrTestActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.endlessImageListTestButton:
                        intent = new Intent(ButtonsActivity.this, EndlessImageListActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.endlessFlickrTestButton:
                        intent = new Intent(ButtonsActivity.this, EndlessFlickrActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.flickListButton:
                        intent = new Intent(ButtonsActivity.this, FlickListActivity.class);
                        startActivity(intent);
//                        return;
                }
            }
        };

        // set listener to buttons
        endlessListButton.setOnClickListener(buttonListener);
        imageListButton.setOnClickListener(buttonListener);
        flickrTestButton.setOnClickListener(buttonListener);
        endlessImageListTestButton.setOnClickListener(buttonListener);
        endlessFlickrTestButton.setOnClickListener(buttonListener);
        flickListButton.setOnClickListener(buttonListener);
    }
}