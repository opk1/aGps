package com.example.comp4985androidgpsassignment3;

/**
 * Created by Robin Hsieh on 2014-03-01.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientActivity extends Activity implements LocationListener {
    Button mConnectButton;
    Button mSendLocation;
    EditText mIPEditText;
    EditText mPortEditText;
    TextView mCoordinateText;
    LocationManager locationManager;
    String provider;
    public static double latitude;
    public static double longitude;

    Socket client;

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName("96.49.182.104");

                client = new Socket(serverAddr, 7700);
                Log.d("Client", client.toString());

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from client_activity.xml
        setContentView(R.layout.client_activity);

        mConnectButton = (Button) findViewById(R.id.bConnect);
        mSendLocation = (Button) findViewById(R.id.bSendLocation);
        mIPEditText = (EditText) findViewById(R.id.ipText);
        mPortEditText = (EditText) findViewById(R.id.portText);
        mCoordinateText = (TextView) findViewById(R.id.coordinates);

        mCoordinateText.setMovementMethod(new ScrollingMovementMethod());

        mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String ipAddress = mIPEditText.getText().toString();
                String portNumberString = mPortEditText.getText().toString();
                int portNumber = 0;

                // If IP address input edit text is empty
                if (ipAddress.matches("")) {
                    Toast.makeText(ClientActivity.this, "Please enter an IP address.", Toast.LENGTH_LONG).show();
                }
                // If port number input edit text is empty
                else if (portNumberString.matches("")) {
                    Toast.makeText(ClientActivity.this, "Please enter a port number.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        portNumber = Integer.parseInt(portNumberString);
                        new Thread(new ClientThread()).start();
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    // Connect to server
                    //Socket client = new Socket(ipAddress, portNumber);
                    mConnectButton.setEnabled(false);
                    mSendLocation.setEnabled(true);

                    // Send client coordinates to server
                }

            }
        });



        mSendLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Start ClientActivity.class
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // check if enabled and if not send user to the GSP settings
                // Better solution would be to display a dialog and suggesting to
                // go to the settings
                if (!enabled) {
                    Toast.makeText(ClientActivity.this, "Please enable GPS to find your location.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                LocationListener mlocListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        location.getLatitude();
                        location.getLongitude();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };

                // Get the location manager
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (latitude > 0) {
                        mCoordinateText.append("Latitude: " + latitude + '\n');
                        mCoordinateText.append("Longitude: " + longitude + '\n');

                        String coordinatesS = ("Latitude: " + latitude + " Longitude: " + longitude);
                        Log.e("Coordinate", coordinatesS);
                        try {

                            OutputStream outToServer = client.getOutputStream();
                            DataOutputStream out = new DataOutputStream(outToServer);
                            out.writeUTF(coordinatesS);
                            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
                            //out.println(coordinatesS);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCoordinateText.append("GPS in progress, please wait\n");

                        Log.d("Coordinate", "GPS in progress, please wait");
                        try {

                            OutputStream outToServer = client.getOutputStream();
                            DataOutputStream out = new DataOutputStream(outToServer);
                            out.writeUTF("GPS in progress, please wait");
                            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
                            //out.println(coordinatesS);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //PrintWriter out = new PrintW
                    }
                } else {
                    mCoordinateText.setText("GPS is not turned on...");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    public void onLocationChanged(Location location) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
