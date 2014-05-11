package com.example.comp4985androidgpsassignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {
    Button clientButton;
    Button serverButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locate the button in activity_main.xml
        clientButton = (Button) findViewById(R.id.bClient);
        serverButton = (Button) findViewById(R.id.bServer);

        clientButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this,ClientActivity.class);
                startActivity(myIntent);
            }
        });

        serverButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start ServerActivity.class
                Intent myIntent = new Intent(MainActivity.this, ServerActivity.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


