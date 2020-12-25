package com.example.wips.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wips.R;
import com.example.wips.Models.CalModel;

import java.util.ArrayList;

public class CalAdapter extends RecyclerView.Adapter<CalAdapter.ViewHolder> {
    private ArrayList<CalModel> dataSet;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.item_title);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.item_detail);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.item_image);
        }

    }
    public CalAdapter(ArrayList<CalModel> data) {
        this.dataSet = data;
    }

    @NonNull
    @Override
    public CalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        textViewName.setText(dataSet.get(position).getName());
        textViewVersion.setText(dataSet.get(position).getVersion());
        imageView.setImageResource(dataSet.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
