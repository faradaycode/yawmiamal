package com.magentamedia.yaumiamal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.magentamedia.yaumiamal.fragments.OpenMapsDialog;
import com.magentamedia.yaumiamal.models.ToolbarTitle;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.receivers.amalDatas;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profil extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient gac;
    private FusedLocationProviderClient flc;
    private int cf = 0;
    private final static int REQUEST_CHECK_SETTINGS = 411;
    private final String TAG = Profil.class.getSimpleName();
    private amalDatas amalDatas;
    private static String TAG_RECEIVER = "LOADAZANDATA";

    @BindView(R.id.profil_parent)
    CoordinatorLayout cparent;

    @BindView(R.id.aptx_set_city)
    TextView tx_kota;

    @OnClick(R.id.bt_riwayat)
    void riwayatClick(View v) {
        changePage(RiwayatKegiatan.class);
    }

    @OnClick(R.id.btn_grafik_me)
    void onGrapik() {
        changePage(Grafik.class);
    }

    @OnClick(R.id.bt_aploc)
    void clickLoc() {

        if (checkGooglePlay()) {
            displayLocationSettingsRequest();
        } else {
            me.onToast(this, "Google Play Tidak Ditemukan");
        }
    }

    @OnClick(R.id.bt_profil)
    void clickProfil() {
        changePage(UserProfil.class);
    }

    private final YawmiMethodes me = new YawmiMethodes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);

        //google api
        gac = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        ToolbarTitle toolbarTitle = new ToolbarTitle();
        TextView Titles = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle.setToolbarTitle("Pengaturan");
        toolbar.setNavigationIcon(R.drawable.ic_back_ios);
        Titles.setText(toolbarTitle.getToolbarTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent data = new Intent();
        data.putExtra("location_set", true);
        setResult(Activity.RESULT_OK, data);

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (gac != null) {
            gac.connect();
        }

        AppStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(amalDatas);
    }

    //GPS enable dialogue
    private void displayLocationSettingsRequest() {

        int INTERVAL_GET = 10000;
        int FAST_INTERVAL = INTERVAL_GET / 2;
        float DISPLACE = 100;

        LocationRequest mLocationRequestBalancedPowerAccuracy = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAL_GET)
                .setFastestInterval(FAST_INTERVAL)
                .setSmallestDisplacement(DISPLACE);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequestBalancedPowerAccuracy);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here...

                    getMyLocation();

                    Log.wtf(TAG + ".response_prompt", "" + response.getLocationSettingsStates()
                            .isGpsPresent());

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.

                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(Profil.this,
                                        REQUEST_CHECK_SETTINGS);

                                Log.d("resolvable", resolvable.getLocalizedMessage());

                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog....
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        getMyLocation();

                        Log.wtf(TAG + ".act_res_OK", "this is after user click ok on google " +
                                "prompt location dialog");
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        Log.wtf(TAG + ".act_res_CANCEL", "this is after user click cancel on google " +
                                "prompt location dialog");
                        break;

                    default:
                        break;
                }
                break;
        }
    }

    private void changePage(Class classname) {
        Intent intent = new Intent(this, classname);
        startActivity(intent);
    }

    //check if google play app installed
    private boolean checkGooglePlay() {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();

        int rc = gaa.isGooglePlayServicesAvailable(this);

        if (rc != ConnectionResult.SUCCESS) {
            if (gaa.isUserResolvableError(rc)) {
                gaa.getErrorDialog(this, rc, 5000);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    //get current city name and save to
    private void insertCity(List<Address> fCity) {

        final String city = fCity.get(0).getSubAdminArea();
        double lat = fCity.get(0).getLatitude();
        double lon = fCity.get(0).getLongitude();
        String regex = ".*?\\b(kota)\\b\\s.*?";
        final String after_city = city.toLowerCase().replaceAll(regex,"");
        Handler handler = new Handler();

        sharedPreferenceSingleton.getInstance(Profil.this)
                .put(sharedPreferenceSingleton.Key.USER_LATITUDE_KEY, lat);

        sharedPreferenceSingleton.getInstance(Profil.this)
                .put(sharedPreferenceSingleton.Key.USER_LONGTITUDE_KEY, lon);

        sharedPreferenceSingleton.getInstance(Profil.this)
                .put(sharedPreferenceSingleton.Key.USER_CITY_KEY, after_city);
        

        me.onToast(getApplicationContext(), "Lokasi Berhasil Diperbarui");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tx_kota.setText(me.capitalizem(after_city));

                if (sharedPreferenceSingleton.getInstance(Profil.this)
                        .getBoolean(sharedPreferenceSingleton.Key.FIRST_INIT_KEY, true))
                {
                    sharedPreferenceSingleton.getInstance(Profil.this)
                            .put(sharedPreferenceSingleton.Key.FIRST_INIT_KEY, false);
                }
            }
        }, Toast.LENGTH_SHORT);
    }

    //set alarm state for azan
    private void setStatusForAzan() {
        sharedPreferenceSingleton pref = sharedPreferenceSingleton.getInstance(Profil.this);

        pref.put(sharedPreferenceSingleton.Key.IS_FAJR_ACTIVE, true);
        pref.put(sharedPreferenceSingleton.Key.IS_ZUHR_ACTIVE, true);
        pref.put(sharedPreferenceSingleton.Key.IS_ASR_ACTIVE, true);
        pref.put(sharedPreferenceSingleton.Key.IS_MAGHRIB_ACTIVE, true);
        pref.put(sharedPreferenceSingleton.Key.IS_ISHA_ACTIVE, true);
    }

    private void AppStarted() {
        tx_kota.setText(me.capitalizem(sharedPreferenceSingleton.getInstance(Profil.this)
                .getString(sharedPreferenceSingleton.Key.USER_CITY_KEY, "-----")));
    }

    //from https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    private void getMyLocation() {
        flc = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        flc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    onLocationChanged(location);
                } else {
                    cf++;

                    if (cf < 3) {
                        me.onToast(getApplicationContext(), "Coba Beberapa Saat Lagi");
                    }

                    if (cf >= 3) {
                        DialogFragment df = new OpenMapsDialog();
                        df.show(getSupportFragmentManager(), "opengmaps");
                        cf *= 0; //reset
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("errl", "" + e.getMessage());
            }
        });
    }

    //location listener method implementation
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        List<Address> addresses = me.getCityName(this, location.getLatitude(),
                location.getLongitude());

        insertCity(addresses);
        setStatusForAzan();

        Log.wtf(TAG + ".ONLINE_GPS_RESULT", TextUtils.join(", ", addresses));
    }

}
