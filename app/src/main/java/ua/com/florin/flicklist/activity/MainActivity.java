package ua.com.florin.flicklist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.com.florin.flicklist.R;


public class MainActivity extends Activity {

    /**
     * Logging tag constant
     */
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button endlessListButton = (Button) findViewById(R.id.endlessListButton);
        Button imageListButton = (Button) findViewById(R.id.imageListButton);
        Button flickrTestButton = (Button) findViewById(R.id.flickrTestButton);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.endlessListButton:
                        intent = new Intent(MainActivity.this, EndlessListActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.imageListButton:
                        intent = new Intent(MainActivity.this, ImageListActivity.class);
                        startActivity(intent);
                        return;
                    case R.id.flickrTestButton:
                        intent = new Intent(MainActivity.this, FlickrTestActivity.class);
                        startActivity(intent);
//                        return;
                }
            }
        };
        endlessListButton.setOnClickListener(buttonListener);
        imageListButton.setOnClickListener(buttonListener);
        flickrTestButton.setOnClickListener(buttonListener);
    }
}
