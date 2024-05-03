package com.example.gpsdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {
    private Button btnStart;
    private Button btnStop;
    private TextView textView;
    private Location mLocation;
    private Context mContext;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        textView = (TextView) findViewById(R.id.text);
        btnStart.setOnClickListener(btnClickListener);
        btnStop.setOnClickListener(btnClickListener); // 结束定位按钮
        mContext = this;
        // if (savedInstanceState == null) {
        // getFragmentManager().beginTransaction()
        // .add(R.id.container, new PlaceholderFragment())
        // .commit();
        // }

    }

    public Button.OnClickListener btnClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Button btn = (Button) v;
            if (btn.getId() == R.id.btnStart) {
                if (!gpsIsOpen())
                    return;

                mLocation = getLocation();

                if (mLocation != null)
                    textView.setText("Button.CLICK,Latitude:" + mLocation.getLatitude() + "\nLongitude:" + mLocation.getLongitude());
                else
                    textView.setText("Button.CLICK,Can't get the data!");
            } else if (btn.getId() == R.id.btnStop) {
                if (locationListener!=null){
                    mLocationManager.removeUpdates(locationListener);
                }
                Toast.makeText(mContext,"GPS removed", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private boolean gpsIsOpen() {
        boolean bRet = true;

        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS not open", Toast.LENGTH_SHORT).show();
            bRet = false;
        } else {
            Toast.makeText(this, "GPS opened", Toast.LENGTH_SHORT).show();
        }

        return bRet;
    }

    private Location getLocation() {
        // Get the location manager
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Find related services
        Criteria criteria = new Criteria();

        // A finer location accuracy requirement
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // Not provide altitude information
        criteria.setAltitudeRequired(false);

        // Not provide bearing information
        criteria.setBearingRequired(false);

        // Provider is allowed to incur monetary cost
        criteria.setCostAllowed(true);

        // Indicates the desired maximum power level
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Returns the name of the provider that best meets the given criteria
        String provider = mLocationManager.getBestProvider(criteria, true);

        // Returns a Location indicating the data from the last known location
        // fix obtained from the given provider.


        // Register for location updates using a Criteria and pending intent.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Location location = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1000, locationListener);

            return location;
        }
        return null;
    }

    // Get the longitude and latitude when GPS changed
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if (location != null)
                textView.setText("Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude());
            else
                textView.setText("Can't get the data!");
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
