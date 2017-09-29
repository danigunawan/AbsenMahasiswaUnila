package id.andaglos.belajarandorid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import id.andaglos.belajarandorid.config.CrudService;
import id.andaglos.belajarandorid.config.Value;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahPasswordActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    EditText EdtUsernameBaru, EdtPasswordLama, EdtPasswordBaru, EdtKonfirmasiPassword;
    Button BtnUbahPassword;

    SharedPreferences sharedpreferences; // untuk menyimpan data username yang sedang login

    public static final String MyPREFERENCES = "login" ;
    public static final String username = "usernameKey";
    private ProgressDialog progress;// progress

    private static final String TAG = UbahPasswordActivity.class.getSimpleName();
    public double latitude_sekarang,longitude_sekarang;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;


    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        EdtUsernameBaru = (EditText) findViewById(R.id.username_baru);
        EdtPasswordBaru = (EditText) findViewById(R.id.password_baru);
        EdtPasswordLama = (EditText) findViewById(R.id.password_lama);
        EdtKonfirmasiPassword = (EditText) findViewById(R.id.konfirmasi_password);
        BtnUbahPassword = (Button) findViewById(R.id.btn_ubah_password);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String username_edit = shared.getString(username, "");

        EdtUsernameBaru.setText(username_edit);

        BtnUbahPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if (vaidate_form() == true){

                    SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    String username_edit = shared.getString(username, "");

                    //JALANKAN PROSES EDIT PASSWORD
                    ProsesEditPassword(username_edit);
                }

            }
        });

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        EdtPasswordLama.requestFocus();// focus ke input pasword lama
    }

    //PROSES EDIT PASSWORD
    private void ProsesEditPassword(String username_edit){
        /* progress */
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");// set message/pesan progress
        progress.show();// menampilkan progress

        CrudService crud = new CrudService();
        crud.UbahPasswordMahasiswa(username_edit,EdtUsernameBaru.getText().toString(), EdtPasswordLama.getText().toString(), EdtPasswordBaru.getText().toString(), new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progress.dismiss();// progress ditutup

                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("1")){

                    //Edit Session login
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    // untuk menyimpan data username
                    editor.putString(username, EdtUsernameBaru.getText().toString());
                    editor.commit();

                    BerhasilUbahPassword();
                }else{

                    Toast.makeText(UbahPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                    if(value.equals("0")){
                        EdtPasswordLama.requestFocus();// focus ke input pasword lama
                    }
                    else{
                        EdtUsernameBaru.requestFocus();// focus ke input username
                    }
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progress.dismiss();// progress ditutup

                Toast.makeText(UbahPasswordActivity.this, "Terjadi Kesalahan!", Toast.LENGTH_LONG).show();
                // munculkan toast Terjadi kesalahan
                t.printStackTrace();
            }
        });

    }

    //VALIDASI FORM UBAH USER DAN PASSWORD
    private boolean vaidate_form(){
        // jika username belum di isi
        if (EdtUsernameBaru.getText().toString().equals("")){

            // maka muncul required
            EdtUsernameBaru.setError("Silahkan Isi Username Baru Anda!");
            EdtUsernameBaru.requestFocus();// focus ke input username

            return false;// return false
        }else if (EdtPasswordLama.getText().toString().equals("")){

            // maka muncul required
            EdtPasswordLama.setError("Silahkan Isi Password Lama Anda!");
            EdtPasswordLama.requestFocus();// focus ke input pasword lama

            return false;// return false
        }else if (EdtPasswordBaru.getText().toString().equals("")){

            // maka muncul required
            EdtPasswordBaru.setError("Silahkan Isi Password Baru Anda!");
            EdtPasswordBaru.requestFocus();// focus ke input pasword baru

            return false;// return false
        }else if (EdtKonfirmasiPassword.getText().toString().equals("")){

            // maka muncul required
            EdtKonfirmasiPassword.setError("Silahkan Isi Konfirmasi Password Anda!");
            EdtKonfirmasiPassword.requestFocus();// focus ke input konfirmasi pasword

            return false;// return false
        }else if (!EdtPasswordBaru.getText().toString().equals(EdtKonfirmasiPassword.getText().toString()) ) {
            // maka muncul required
            EdtKonfirmasiPassword.setError("Password Baru dan Konfirmasi Password Harus Sama!");
            EdtKonfirmasiPassword.requestFocus();
            return  false;// return true
        } else {


            return true;// return false
        }

    }

    //MEMBUAT MENU DI SAMPING
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    private void BerhasilUbahPassword(){
        AlertDialog.Builder Alert = new AlertDialog.Builder(UbahPasswordActivity.this);
        // set title dialog
        Alert.setTitle("Username dan Password Berhasil Diubah");


        // set pesan dialog
        Alert.setIcon(R.drawable.logofinish);
        Alert.setCancelable(false);
        Alert.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            // tombol Ya
            public void onClick(DialogInterface dialog, int id) {
                //jika tombol Ya di klik maka akan akan menjalankan proses batal absen
                // KEMBALI KE ACTIVITY ListJadwalActivity
                dialog.cancel();
                startActivity(new Intent(UbahPasswordActivity.this,ListJadwalActivity.class));

            }
        });
        // membuat alert dialog dari builder
        AlertDialog alertDialog = Alert.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // logut
        if (item.getItemId() ==  R.id.logout) {
            logout();
            finish();
            startActivity( new Intent(UbahPasswordActivity.this, LoginActivity.class));
        }
        else if (item.getItemId() ==  R.id.list_jadwal_dosen){
            startActivity( new Intent(UbahPasswordActivity.this, ListJadwalActivity.class));
        }
        else if (item.getItemId() ==  R.id.jadwal_hari_ini){
            startActivity( new Intent(UbahPasswordActivity.this, JadwalBesokActivity.class));
        }
        else if (item.getItemId() ==  R.id.jadwal_lusa){
            startActivity( new Intent(UbahPasswordActivity.this, JadwalLusaActivity.class));
        }
        else if (item.getItemId() ==  R.id.ubah_password){
            startActivity( new Intent(UbahPasswordActivity.this, UbahPasswordActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    //PROSES LOGOUT
    public void logout (){

        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// edit sessionlogin
        editor.clear();// bersihkan session login
        editor.commit();// simpan
    }
    // untuk menampilkan otomatis data terbaru.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            latitude_sekarang =  mLastLocation.getLatitude();
            longitude_sekarang = mLastLocation.getLongitude();
        }
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
     * Method to verify google play services on the devicedfhdfhknvk
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }



    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
}
