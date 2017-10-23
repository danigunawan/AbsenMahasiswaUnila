package id.andaglos.belajarandorid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.util.ArrayList;
import java.util.List;

import id.andaglos.belajarandorid.adapter.JadwalAdapter;
import id.andaglos.belajarandorid.config.CrudService;
import id.andaglos.belajarandorid.config.Result;
import id.andaglos.belajarandorid.config.Value;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalBesokActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, PermissionCallback, ErrorCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // declrasikan variable yang dibutuhkan
    private static final int REQUEST_PERMISSIONS = 20;

    private List<Result> results = new ArrayList<>();// result
    private JadwalAdapter viewAdapter;// viewAdapter
    private ImageView imageView;// image view
    private TextView jadwal_kosong; // text view jadwal kosong

    private static final String TAG = UbahPasswordActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;


    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    SearchView search;// search/ pencarian
    RecyclerView recyclerView;// recyclerView
    ProgressBar progressBar;// progresabar

    // untuk menyimpan data username yang sedang login
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "login" ;
    public static final String username = "usernameKey";
    public static final String loginusername = "usernameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_besok);

        // inisiasi variable
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        search = (SearchView) findViewById(R.id.search);
        imageView = (ImageView) findViewById(R.id.ImageKosong);
        jadwal_kosong = (TextView) findViewById(R.id.jadwal_kosong);

        viewAdapter = new JadwalAdapter(this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String login_username = shared.getString(username, "");

        // passing varibel username
        loadDataJadwalBesok(login_username);
        reqPermission();

        // First we need to check availability of play services
        if (checkPlayServices()) {

            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    // proses menampilkan list jadwal dosen
    public void loadDataJadwalBesok(String username) {

        imageView.setVisibility(View.GONE);// hidden Image View
        jadwal_kosong.setVisibility(View.GONE);// hidden Text Jadwal Kosong

        CrudService crud = new CrudService();
        crud.jadwalBesok(username,new Callback <Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {

                String value = response.body().getValue();// ambil vallue

                progressBar.setVisibility(View.GONE);// hidden progressbar

                if (value.equals("0")) {// jika value bernilai nol,
                    // maka
                    imageView.setVisibility(View.VISIBLE);// Show Image View
                    jadwal_kosong.setVisibility(View.VISIBLE);// Hidden Text Jadwal Kosong

                }else{// jika tidak
                    // tampilkan lsit jadwal
                    results = response.body().getResult();
                    viewAdapter = new JadwalAdapter(JadwalBesokActivity.this, results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {// jika terjadi throwble

                progressBar.setVisibility(View.GONE);// hidden progressbar
                Toast.makeText(JadwalBesokActivity.this, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                // toast terjadi KEsalahan
                t.printStackTrace();

            }
        });
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // fitur search jadwal/ pencarian jadwa;
    @Override
    public boolean onQueryTextChange(String newText) {// keyup

        recyclerView.setVisibility(View.GONE);// hidden recycle view
        progressBar.setVisibility(View.VISIBLE);// munculkan progressbar

        // untuk menda[takn session login
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String username = (shared.getString(loginusername, ""));

        CrudService crud = new CrudService();
        crud.searchJadwalBesok(newText,username, new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {

                String value = response.body().getValue();
                progressBar.setVisibility(View.GONE); //hidden progressBar
                recyclerView.setVisibility(View.VISIBLE);// tampilkan recycle view

                // jika value bernilai 1
                if (value.equals("1")) {
                    // maka akan tampil jadwal yang dicari
                    results = response.body().getResult();
                    viewAdapter = new JadwalAdapter(JadwalBesokActivity.this, results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                progressBar.setVisibility(View.GONE); //hidden progressBar
                Toast.makeText(JadwalBesokActivity.this, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }

        });

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari Jadwal...");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // logut
        if (item.getItemId() ==  R.id.logout) {
            logout();
            finish();
            startActivity( new Intent(JadwalBesokActivity.this, LoginActivity.class));
        }
        else if (item.getItemId() ==  R.id.list_jadwal_dosen){
            startActivity( new Intent(JadwalBesokActivity.this, ListJadwalActivity.class));
        }
        else if (item.getItemId() ==  R.id.jadwal_hari_ini){
            startActivity( new Intent(JadwalBesokActivity.this, JadwalBesokActivity.class));
        }
        else if (item.getItemId() ==  R.id.jadwal_lusa){
            startActivity( new Intent(JadwalBesokActivity.this, JadwalLusaActivity.class));
        }
        else if (item.getItemId() ==  R.id.ubah_password){
            startActivity( new Intent(JadwalBesokActivity.this, UbahPasswordActivity.class));
        }else if (item.getItemId() == R.id.foto_profile) {
            startActivity(new Intent(JadwalBesokActivity.this, ProfilActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }// jika user tekan tombol back

    // logout
    public void logout (){

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// edit sessionlogin
        editor.clear();// bersihkan session login
        editor.commit();// simpan
    }


    public void reqPermission() {
        new AskPermission.Builder(this).setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .setCallback(this)
                .setErrorCallback(this)
                .request(REQUEST_PERMISSIONS);
    }


    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Perizinan Tidak Diterima", Toast.LENGTH_LONG).show();
        reqPermission();
    }


    //UPDATE LOKASI LATITUDE LONGITUDE

    /**
     * VERIFIKASI KE GOOGLE PLAY SERVICES
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    // untuk menampilkan otomatis data terbaru.
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String login_username = (shared.getString(username, ""));

        loadDataJadwalBesok(login_username);

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle arg0) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location Changed!",
                Toast.LENGTH_SHORT).show();

    }

}
