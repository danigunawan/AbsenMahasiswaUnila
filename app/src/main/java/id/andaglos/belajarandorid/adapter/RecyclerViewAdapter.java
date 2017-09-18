package id.andaglos.belajarandorid.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.andaglos.belajarandorid.BukaCameraActivity;
import id.andaglos.belajarandorid.R;
import id.andaglos.belajarandorid.config.Result;

import static android.content.Context.MODE_PRIVATE;
import static id.andaglos.belajarandorid.R.id.textTanggal;

/**
 * Created by Andaglos on 29/08/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //declarasi variable
    private Context context;
    private List<Result> results;


    public RecyclerViewAdapter(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Result result = results.get(position);
        holder.txtTanggal.setText(result.getTanggal());
        holder.txtWaktu.setText(result.getWaktu());
        holder.txtMataKuliah.setText(result.getMataKuliah());
        holder.txtNamaRuangan.setText(result.getNamaRuangan());
        holder.txtIdJadwal.setText(result.getIdJadwal());
        holder.txtIdRuangan.setText(result.getIdRuangan());
        holder.txtLatitude.setText(result.getLatitude());
        holder.txtLongitude.setText(result.getLongitude());
        holder.txtBatasJarakAbsen.setText(result.getBatasJarakAbsen());
        holder.txtTipeJadwal.setText(result.getTipeJadwal());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_jadwal, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    // untuk mendapatkan jumlah data yang akan ditampilkan
    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtTanggal, txtWaktu, txtMataKuliah,txtNamaRuangan, txtIdJadwal, txtIdRuangan,
                txtLatitude, txtLongitude, txtBatasJarakAbsen, txtTipeJadwal;

        SharedPreferences sharedpreferences;
        public static final String MyPREFERENCES = "login" ;

        public String id_jadwal;
        public String id_ruangan;
        public String latitude;
        public String longitude;
        public String batas_jarak_absen;
        public String nama_ruangan;
        public String tanggal;
        public String waktu_jadwal;
        public String tipe_jadwal;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
//inisiasi variabel
            txtTanggal = itemView.findViewById(textTanggal);
            txtWaktu = itemView.findViewById(R.id.textWaktu);
            txtMataKuliah = itemView.findViewById(R.id.textMataKuliah);
            txtNamaRuangan = itemView.findViewById(R.id.textNamaRuangan);
            txtLongitude = itemView.findViewById(R.id.textLongitude);
            txtLongitude.setVisibility(View.GONE);
            txtLatitude = itemView.findViewById(R.id.textLatitude);
            txtLatitude.setVisibility(View.GONE);
            txtBatasJarakAbsen = itemView.findViewById(R.id.textBatasJarakAbsen);
            txtBatasJarakAbsen.setVisibility(View.GONE);
            txtIdJadwal = itemView.findViewById(R.id.textIdJadwal);
            txtIdJadwal.setVisibility(View.GONE);// hidden id jadwal
            txtIdRuangan = itemView.findViewById(R.id.textIdRuangan);
            txtIdRuangan.setVisibility(View.GONE);// hidden id ruangan
            txtTipeJadwal = itemView.findViewById(R.id.textTipeJadwal);

            SharedPreferences shared = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String username = shared.getString("usernameKey", "");
        }

        // fungsi ketika card view di klik
        @Override
        public void onClick(View view) {

            // buat variable id jadwal
            id_jadwal = txtIdJadwal.getText().toString();
            // buat variable id jadwal
            id_ruangan = txtIdRuangan.getText().toString();
            // / buat variable id jadwal
            latitude = txtLatitude.getText().toString();
            // buat variable id jadwal
            longitude = txtLongitude.getText().toString();            // buat variable id jadwal
            batas_jarak_absen = txtBatasJarakAbsen.getText().toString();
            nama_ruangan = txtNamaRuangan.getText().toString();
            tanggal = txtTanggal.getText().toString();
            waktu_jadwal = txtWaktu.getText().toString();
            waktu_jadwal = txtWaktu.getText().toString();
            tipe_jadwal = txtTipeJadwal.getText().toString();

            // show Alertdialog
            showDialog(id_jadwal, id_ruangan,latitude, longitude, batas_jarak_absen,nama_ruangan,waktu_jadwal,tanggal, tipe_jadwal);

        }
    }

    // tampilkan AlertDialog
    private void showDialog(final String id_jadwal, final String id_ruangan, final String latitude,
                            final String longitude, final String batas_jarak_absen, final String nama_ruangan, final String waktu_jadwal, final String tanggal, final String tipe_jadwal){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title dialog
        alertDialogBuilder.setTitle("Konfirmasi Jadwal");


        // set pesan dialog
        alertDialogBuilder.setIcon(R.drawable.logofinish);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Absen", new DialogInterface.OnClickListener() {
            // tombol absen
            public void onClick(DialogInterface dialog, int id) {
                //jika tombol Absen di klik maka akan OTOMATIS MEMBUKA KAMRE
                if (latitude == "" && longitude == ""){
                    AlertDialog.Builder pesan_alert = new AlertDialog.Builder(context);
                    // set title dialog
                    pesan_alert.setTitle("Pemberitahuan");
                    pesan_alert.setIcon(R.drawable.logofinish);
                    pesan_alert.setMessage("Lokasi Ruangan "+nama_ruangan+" Belum Ditentukan, Silakan Tentukan!");
                    pesan_alert.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                        // tombol tutup
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // jika tombol ini diklik, akan menutup dialog
                            // dan tidak terjadi apa2
                            dialog.cancel();
                        }
                    });

                    // membuat alert dialog dari builder
                    AlertDialog alertPesan = pesan_alert.create();

                    // menampilkan alert dialog
                    alertPesan.show();
                }
                else{
                    // PROSES UNTUK MEMBUKA KAMERA
                    prosesambilFoto(id_jadwal, id_ruangan, latitude, longitude,batas_jarak_absen, nama_ruangan, waktu_jadwal, tanggal, tipe_jadwal);
                }

            }
        });

        alertDialogBuilder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
            // tombol tutup
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // jika tombol ini diklik, akan menutup dialog
                // dan tidak terjadi apa2
                dialog.cancel();
            }
        });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    // PROSES MEMBUKA KAMERA
    private void prosesambilFoto(String id_jadwal, String id_ruangan, String latitude,
                                 String longitude, String batas_jarak_absen, String nama_ruangan, String waktu_jadwal, String tanggal, String tipe_jadwal){


        // UNTUK MEMBUKA KAMERA , KITA PINDAH ACTIVITY KE BukaCameraActivity
        Intent intent = new Intent(context, BukaCameraActivity.class);
        // KITA TARUH ID JADWAL,ID RUANGAN, UNTUK DI KIRIM KE BukaCameraActivity
        intent.putExtra("id_jadwal", id_jadwal);
        intent.putExtra("id_ruangan", id_ruangan);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("batas_jarak_absen", batas_jarak_absen);
        intent.putExtra("nama_ruangan", nama_ruangan);
        intent.putExtra("waktu_jadwal", waktu_jadwal);
        intent.putExtra("tanggal", tanggal);
        intent.putExtra("tipe_jadwal", tipe_jadwal);
        context.startActivity(intent);
    }


}
