package com.example.wips.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wips.Interfaces.OnRecyclerItemClickListener;
import com.example.wips.R;
import com.example.wips.Models.CalModel;

import java.util.ArrayList;

public class CalAdapter extends RecyclerView.Adapter<CalAdapter.ViewHolder> {
    private ArrayList<CalModel> dataSet;
    private OnRecyclerItemClickListener mlistener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        OnRecyclerItemClickListener listener;

        public ViewHolder(View itemView,OnRecyclerItemClickListener listener) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.item_title);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.item_detail);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.item_image);
            this.listener=listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(itemView,getAdapterPosition());
        }
    }
    public CalAdapter(ArrayList<CalModel> data,OnRecyclerItemClickListener listener) {
        this.dataSet = data;
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public CalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view,mlistener);
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
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
