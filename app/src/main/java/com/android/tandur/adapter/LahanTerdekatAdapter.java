package com.android.tandur.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tandur.R;
import com.android.tandur.api.response.LahanTerdekatResponse;

import java.util.List;

public class LahanTerdekatAdapter extends RecyclerView.Adapter<LahanTerdekatAdapter.ViewHolder> {
    private List<LahanTerdekatResponse.LahanTerdekatModel> list;

    public LahanTerdekatAdapter(List<LahanTerdekatResponse.LahanTerdekatModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lahan_terdekat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewNamaLahan.setText(list.get(position).namaLahan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNamaLahan, textViewHarga, textViewLuas, textViewFasilitas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaLahan = itemView.findViewById(R.id.textViewNamaLahan);
            textViewHarga = itemView.findViewById(R.id.textViewHarga);
            textViewLuas = itemView.findViewById(R.id.textViewLuas);
            textViewFasilitas = itemView.findViewById(R.id.textViewFasilitas);
        }
    }
}
