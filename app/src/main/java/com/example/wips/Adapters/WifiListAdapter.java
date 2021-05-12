package com.example.wips.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wips.Activities.Admin.WifiScanner;
import com.example.wips.Models.WifiListModel;
import com.example.wips.R;

import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {
    private List<WifiListModel> mModelList;
    private WifiScanner context;
    private boolean selectAll = false;


    public WifiListAdapter(List<WifiListModel> modelList, WifiScanner context) {
        mModelList = modelList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final WifiListModel model = mModelList.get(position);
        holder.wifi_name.setText(model.getWifi_name());
        holder.rss.setText(model.getRss());
        holder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
        holder.wifi_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if (!(model.getWifi_name().equals("No Wifi Available"))) {
                    model.setSelected(!model.isSelected());
                    holder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
                   // isAnyItemSelected();
               // }
            }
        });
        if (selectAll){
            for (WifiListModel item : mModelList) {
               item.setSelected(true);
                holder.view.setBackgroundColor(Color.CYAN);
            }
            selectAll = false;
        }
      //  boolean flag = isAnyItemSelected();
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public boolean isAnyItemSelected() {
        boolean flagdemo = false;
        for (WifiListModel item : mModelList) {
            if (item.isSelected()) {
                flagdemo = true;
            }
        }
        return flagdemo;
    }
    public void setSelectAll(){
        selectAll = true;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView wifi_name;
        private TextView rss;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            wifi_name = (TextView) itemView.findViewById(R.id.wifi_name);
            rss = (TextView) itemView.findViewById(R.id.Rss);
        }
    }
}
