package com.android.tandur.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tandur.R;
import com.android.tandur.api.response.LahanTerdekatResponse;
import com.android.tandur.view.DetailLahanActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridLahanTerdekatAdapter extends RecyclerView.Adapter<GridLahanTerdekatAdapter.ViewHolder> {
    private List<LahanTerdekatResponse.LahanTerdekatModel> list;

    public GridLahanTerdekatAdapter(List<LahanTerdekatResponse.LahanTerdekatModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lahan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(list.get(position).foto1Lahan)
                .into(holder.imageView);
        holder.textViewNamaLahan.setText(list.get(position).namaLahan);
        holder.textViewHargaLahan.setText("Rp. " + list.get(position).hargaLahan + "/Bln");
        holder.textViewLuasLahan.setText("Luas: " + list.get(position).panjangLahan + " X " + list.get(position).lebarLahan + " Meter");
        holder.textViewKelurahan.setText(list.get(position).namaKelurahan);
        if (list.get(position).bintangLahan != null) {
            holder.ratingBar.setRating(Float.parseFloat(list.get(position).bintangLahan));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewNamaLahan, textViewHargaLahan, textViewLuasLahan, textViewFasilitasLahan, textViewKelurahan;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewNamaLahan = itemView.findViewById(R.id.textViewNamaLahan);
            textViewHargaLahan = itemView.findViewById(R.id.textViewHargaLahan);
            textViewLuasLahan = itemView.findViewById(R.id.textViewLuasLahan);
//            textViewFasilitasLahan = itemView.findViewById(R.id.textViewFasilitasLahan);
            textViewKelurahan = itemView.findViewById(R.id.textViewKelurahan);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailLahanActivity.class);
                    intent.putExtra("ID_LAHAN", list.get(getAdapterPosition()).idLahan);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
