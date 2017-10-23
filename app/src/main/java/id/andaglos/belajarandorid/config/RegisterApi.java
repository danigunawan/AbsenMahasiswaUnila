package id.andaglos.belajarandorid.config;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Andaglos on 25/08/17.
 */

public interface RegisterApi {

    @FormUrlEncoded
    @POST("tambah_siswa.php")
    Call<Value> daftarMahasiswa(@Field("nama_awal") String nama_awal,
                                @Field("nama_akhir") String nama_akhir);

    @FormUrlEncoded
    @POST("login_mahasiswa_android")
    Call<Value>prosesLoginMahasiswa(@Field("username") String username,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("list_jadwal_mahasiswa")
    Call<Value>listJadwal(@Field("username") String username );

    @FormUrlEncoded
    @POST("jadwal_besok")
    Call<Value>jadwalBesok(@Field("username") String username );

    @FormUrlEncoded
    @POST("jadwal_lusa")
    Call<Value>jadwalLusa(@Field("username") String username );

    @FormUrlEncoded
    @POST("search_jadwal_mahasiswa")
    Call<Value>searchJadwal(@Field("search") String search,
                            @Field("username") String username);

    @FormUrlEncoded
    @POST("search_jadwal_mahasiswa_besok")
    Call<Value>searchJadwalBesok(@Field("search") String search,
                                 @Field("username") String username);

    @FormUrlEncoded
    @POST("search_jadwal_mahasiswa_lusa")
    Call<Value>searchJadwalLusa(@Field("search") String search,
                                 @Field("username") String username);


    @FormUrlEncoded
    @POST("batal_jadwal_dosen")
    Call<Value>batalJadwalDosen(@Field("id_jadwal") String id_jadwal);

    @FormUrlEncoded
    @POST("presensi_mahasiswa")
    Call<Value>presensiMahasiswa(@Field("id_jadwal") String id_jadwal,
                             @Field("username") String username,
                             @Field("id_ruangan") String id_ruangan,
                             @Field("image") String image,
                             @Field("latitude_sekarang") String latitude_sekarang,
                             @Field("longitude_sekarang") String longitude_sekarang,
                             @Field("jarak_ke_lokasi_absen") String jarak_ke_lokasi_absen,
                             @Field("waktu_jadwal") String waktu_jadwal,
                             @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("ubah_password_mahasiswa")
    Call<Value>UbahPasswordMahasiswa(@Field("username") String username,
                            @Field("username_baru") String username_baru,
                            @Field("password_lama") String password_lama,
                            @Field("password_baru") String password_baru);


    @GET("versi-absen-mahasiswa")
    Call<Value> CekVersiAplikasi();

    @FormUrlEncoded
    @POST("cek_profil_mahasiswa")
    Call<Value>CekProfilMahasiswa(@Field("user") String user);
    @FormUrlEncoded
    @POST("update_profile_dosen")
    Call<Value>UpdateProfil(@Field("image") String image,
                             @Field("user") String user);


}


