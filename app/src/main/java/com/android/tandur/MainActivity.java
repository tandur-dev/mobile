package com.android.tandur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tandur.adapter.GridLahanTerdekatAdapter;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.LahanTerdekatResponse;
import com.android.tandur.preference.AppPreference;
import com.android.tandur.view.urban_farming.SewakanLahanActivity;
import com.android.tandur.view.urban_farming.UrbanFarmingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private LocationManager locationManager;

    private TextView textViewNama;
    private ImageView imageViewUrbanFarming, imageViewSewakanLahan;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        textViewNama = findViewById(R.id.textViewNama);
        imageViewUrbanFarming = findViewById(R.id.imageViewUrbanFarming);
        imageViewSewakanLahan = findViewById(R.id.imageViewSewakanLahan);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        textViewNama.setText("Haloo, " + AppPreference.getUser(this).namaUser);

        imageViewUrbanFarming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UrbanFarmingActivity.class));
            }
        });

        imageViewSewakanLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SewakanLahanActivity.class));
            }
        });

        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10, locationListenerGPS);
            isLocationEnabled();
        }

//        startActivity(new Intent(this, UrbanFarmingActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + " New Longitude: "+longitude;
//            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            Log.e("location", msg);
//            textView.setText(msg);
            getLahan(latitude, longitude);
//            MainActivity.this.latitude.setText(String.valueOf(latitude));
//            MainActivity.this.longitude.setText(String.valueOf(longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("a", "a");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("b", "b");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("c", "c");
        }
    };

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }

    public void getLahan(double latitude, double longitude) {
        apiInterface.getLahanTerdekat(
                latitude,
                longitude,
                4
        ).enqueue(new Callback<LahanTerdekatResponse>() {
            @Override
            public void onResponse(Call<LahanTerdekatResponse> call, Response<LahanTerdekatResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        recyclerView.setAdapter(new GridLahanTerdekatAdapter(response.body().data));
                    }
                }
            }

            @Override
            public void onFailure(Call<LahanTerdekatResponse> call, Throwable t) {

            }
        });
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}