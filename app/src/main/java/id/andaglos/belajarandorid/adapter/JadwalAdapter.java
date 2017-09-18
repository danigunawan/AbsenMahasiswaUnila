package id.andaglos.belajarandorid.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import id.andaglos.belajarandorid.R;
import id.andaglos.belajarandorid.config.Result;

import static android.content.Context.MODE_PRIVATE;
import static id.andaglos.belajarandorid.R.id.textTanggal;

/**
 * Created by haidar on 16/09/17.
 */

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder>{

    //declarasi variable
    private Context context;
    private List<Result> results;


    public JadwalAdapter(Context context, List<Result> results){
        this.context = context;
        this.results = results;
    }
    @Override
    public JadwalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jadwal_view, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(JadwalAdapter.ViewHolder holder, int position) {

        Result result = results.get(position);
        holder.txtTanggal.setText(result.getTanggal());
        holder.txtWaktu.setText(result.getWaktu());
        holder.txtMataKuliah.setText(result.getMataKuliah());
        holder.txtNamaRuangan.setText(result.getNamaRuangan());
        holder.txtIdJadwal.setText(result.getIdJadwal());
        holder.txtIdRuangan.setText(result.getIdRuangan());
        holder.txtTipeJadwal.setText(result.getTipeJadwal());

    }

    // untuk mendapatkan jumlah data yang akan ditampilkan
    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTanggal, txtWaktu, txtMataKuliah,txtNamaRuangan, txtIdJadwal, txtIdRuangan,
                txtTipeJadwal;

        public ViewHolder(View itemView) {
            super(itemView);
//inisiasi variabel
            txtTanggal = itemView.findViewById(textTanggal);
            txtWaktu = itemView.findViewById(R.id.textWaktu);
            txtMataKuliah = itemView.findViewById(R.id.textMataKuliah);
            txtNamaRuangan = itemView.findViewById(R.id.textNamaRuangan);
            txtIdJadwal = itemView.findViewById(R.id.textIdJadwal);
            txtIdJadwal.setVisibility(View.GONE);// hidden id jadwal
            txtIdRuangan = itemView.findViewById(R.id.textIdRuangan);
            txtIdRuangan.setVisibility(View.GONE);// hidden id ruangan
            txtTipeJadwal = itemView.findViewById(R.id.textTipeJadwal);

        }
    }
}
