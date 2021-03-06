package id.andaglos.belajarandorid.config;

import java.util.concurrent.TimeUnit;

import id.andaglos.belajarandorid.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static id.andaglos.belajarandorid.R.id.password_baru;
import static id.andaglos.belajarandorid.R.id.password_lama;
import static id.andaglos.belajarandorid.R.id.username_baru;

/**
 * Created by Andaglos on 25/08/17.
 */

public class CrudService {

    private RegisterApi registerApi;

    public CrudService() {

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        registerApi = retrofit.create(RegisterApi.class);

    }

    public void daftarMahasiswa(String nama_awal, String nama_akhir, Callback callback){
        registerApi.daftarMahasiswa(nama_awal , nama_akhir).enqueue(callback);
    }

    public void prosesLoginMahasiswa(String username, String password, Callback callback){
        registerApi.prosesLoginMahasiswa(username , password).enqueue(callback);
    }

    public void listJadwal(String username, Callback callback){
        registerApi.listJadwal(username).enqueue(callback);
    }

    public void jadwalBesok(String username, Callback callback){
        registerApi.jadwalBesok(username).enqueue(callback);
    }

    public void jadwalLusa(String username, Callback callback){
        registerApi.jadwalLusa(username).enqueue(callback);
    }

    public void searchJadwal(String search , String username,Callback callback){
        registerApi.searchJadwal(search,username).enqueue(callback);
    }

    public void searchJadwalBesok(String search , String username,Callback callback){
        registerApi.searchJadwalBesok(search,username).enqueue(callback);
    }

    public void searchJadwalLusa(String search , String username,Callback callback){
        registerApi.searchJadwalLusa(search,username).enqueue(callback);
    }

    public void batalJadwalDosen(String id_jadwal,Callback callback){
        registerApi.batalJadwalDosen(id_jadwal).enqueue(callback);
    }

    public void presensiMahasiswa(String id_jadwal, String username, String id_ruangan , String image ,
                              String latitude_sekarang, String longitude_sekarang, String jarak_ke_lokasi_absen,
                              String waktu_jadwal, String tanggal, Callback callback){
        registerApi.presensiMahasiswa(id_jadwal,username, id_ruangan,image, latitude_sekarang,
                longitude_sekarang, jarak_ke_lokasi_absen,waktu_jadwal, tanggal).enqueue(callback);
    }

    public void UbahPasswordMahasiswa(String username ,String username_baru , String password_lama, String password_baru, Callback callback){
        registerApi.UbahPasswordMahasiswa(username,username_baru, password_lama, password_baru).enqueue(callback);
    }

    public void CekVersiAplikasi(Callback callback){
        registerApi.CekVersiAplikasi().enqueue(callback);
    }

    public void CekProfilMahasiswa(String user, Callback callback){
        registerApi.CekProfilMahasiswa(user).enqueue(callback);
    }

    public void UpdateProfil(String image, String user, Callback callback){
        registerApi.UpdateProfil(image,user).enqueue(callback);
    }

}
