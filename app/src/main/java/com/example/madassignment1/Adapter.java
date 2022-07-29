package com.example.madassignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<Weather> list;
    Context context;

    public Adapter(List<Weather> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.countryTv.setText(list.get(position).getCountry());
        holder.weatherTv.setText(list.get(position).getWeather());
        holder.tempTv.setText(list.get(position).getTemp());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tempTv;
        TextView countryTv, weatherTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tempTv = itemView.findViewById(R.id.tempTv);
            countryTv = itemView.findViewById(R.id.countryTv);
            weatherTv = itemView.findViewById(R.id.weatherTv);
        }
    }
}
