package com.example.priyanka.road;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.value;
import static com.example.priyanka.road.R.id.map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    Handler handler;
    GoogleMap googlemap;

    double latitude, longitude;
    AppLocationService appLocationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);  //splash screen changed theme so changing here back again.
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();




            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         }
    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googlemap = googleMap;
        LatLng sydney = new LatLng(78.50, 221);
        googlemap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googlemap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                appLocationService = new AppLocationService(
                        MainActivity.this);
                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                Location nwLocation = appLocationService
                        .getLocation(LocationManager.NETWORK_PROVIDER);
                if (gpsLocation != null) {
                    latitude = gpsLocation.getLatitude();
                    longitude = gpsLocation.getLongitude();
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (GPS): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                }else if (nwLocation != null) {
                     latitude = nwLocation.getLatitude();
                     longitude = nwLocation.getLongitude();
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (NW): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    showSettingsAlert("NETWORK & GPS");
                }
                LatLng sydney = new LatLng(latitude, longitude);
                googlemap.addMarker(new MarkerOptions().position(sydney)
                        .title("Developer Location"));

                googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent myIntent = new Intent(this, Aboutus.class);
            this.startActivity(myIntent);

        } else if (id == R.id.nav_gallery) {

            Intent myIntent = new Intent(this, Gallery1.class);
            this.startActivity(myIntent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            this.startActivity(myIntent);

        } else if (id == R.id.faq) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
