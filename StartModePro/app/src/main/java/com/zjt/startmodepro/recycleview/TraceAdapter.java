package com.zjt.startmodepro.recycleview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.startmodepro.R;

import java.util.ArrayList;
import java.util.List;

public class TraceAdapter extends RecyclerView.Adapter<TraceAdapter.TraceHolder> {


    private List<String> dataList;

    public TraceAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<String> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TraceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trace_item_view, parent, false);
//        itemView.getLayoutParams().height = DisplayUtil.dip2px(180);
//        Log.e("TraceAdapter","onCreateViewHolder >>>  itemView height = " + itemView.getHeight());
        return new TraceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TraceHolder holder, int position) {
//        Log.e("TraceAdapter","onBindViewHolder >>>  itemView height = " + holder.itemView.getLayoutParams().height);
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
//        Log.e("TraceAdapter","getItemCount >>> count = "+ dataList.size());
        return dataList.size();
    }

    class TraceHolder extends RecyclerView.ViewHolder{

        TextView statusTv ;

        public TraceHolder(@NonNull View itemView) {
            super(itemView);
            statusTv = itemView.findViewById(R.id.status_txt);
//            Log.e("TraceAdapter","item height  = "+ itemView.getHeight());

        }

        public void bindView(int pos) {
            statusTv.setText(dataList.get(pos));
        }
    }
}
