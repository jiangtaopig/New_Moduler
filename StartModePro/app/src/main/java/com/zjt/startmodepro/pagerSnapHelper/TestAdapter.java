package com.zjt.startmodepro.pagerSnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.startmodepro.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_STL = TYPE_TEXT + 1;

    private List<String> dataList;

    public TestAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_holder_item_view, parent, false));
        } else  {
            STLView stlView = new STLView(parent.getContext());
            stlView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new STLHolder(stlView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TestHolder) {
            ((TestHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dataList.size()) {
            return TYPE_TEXT;
        } else {
            return TYPE_STL;
        }
    }

    @Override
    public int getItemCount() {
        int cnt = hasData() ? 1 : 0;
        cnt += dataList.size();
        return cnt;
    }

    private boolean hasData() {
        return dataList.size() > 0;
    }

    class TestHolder extends RecyclerView.ViewHolder {

        private TextView tv ;

        public TestHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.test_tv);
        }

        public void bindView(int position) {
            tv.setText(dataList.get(position));
        }
    }

    class STLHolder extends RecyclerView.ViewHolder {

        public STLHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
