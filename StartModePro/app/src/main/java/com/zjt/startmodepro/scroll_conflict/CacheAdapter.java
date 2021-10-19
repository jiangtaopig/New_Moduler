package com.zjt.startmodepro.scroll_conflict;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.startmodepro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/12/25
 */
public class CacheAdapter extends RecyclerView.Adapter<CacheAdapter.CacheHolder> {

    private List<String> dataList;

    public CacheAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CacheHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("CacheAdapter", "onCreateViewHolder ");
        return new CacheHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cache_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CacheHolder cacheHolder, int position) {
        Log.e("CacheAdapter", " ----------- onBindViewHolder position = " + position);
        cacheHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CacheHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public CacheHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_tv);
        }

        public void bindData(int position) {
            textView.setText(dataList.get(position));
            itemView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), dataList.get(position), Toast.LENGTH_LONG).show();
            });
        }
    }
}
