package com.vasudevrb.wifishare;

import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasudevrb.wifishare.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WiFiListAdapter extends RecyclerView.Adapter<WiFiListAdapter.ViewHolder> {

    List<WifiConfiguration> configurations;

    OnItemClickListener onItemClickListener;

    public WiFiListAdapter(OnItemClickListener listener) {
        configurations = new ArrayList<>();
        onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_list, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WifiConfiguration wifiConfiguration = configurations.get(position);
        String ssid = Utils.getSSID(wifiConfiguration);
        String security = Utils.getSecurity(wifiConfiguration);

        int drawable = security.equals("OPEN") ? R.drawable.ic_signal_wifi_4_bar_white_24px : R.drawable.ic_signal_wifi_4_bar_lock_white_24px;

        holder.textSSID.setText(ssid);
        holder.textSecurity.setText(security);
        holder.imageWiFiIcon.setImageResource(drawable);
    }

    @Override
    public int getItemCount() {
        return configurations.size();
    }

    public void update(List<WifiConfiguration> c) {
        configurations = c;
        notifyDataSetChanged();
    }

    public WifiConfiguration getItemAt(int position) {
        return configurations.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_wifi_item_wifi_list)
        ImageView imageWiFiIcon;

        @BindView(R.id.text_wifi_ssid_item_wifi_list)
        TextView textSSID;

        @BindView(R.id.text_wifi_security_item_wifi_list)
        TextView textSecurity;

        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            onItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
