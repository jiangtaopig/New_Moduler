package com.zjt.startmodepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.base.BaseActivity;
import com.zjt.startmodepro.recycleview.TimeLineItemDecoration;
import com.zjt.startmodepro.recycleview.TraceAdapter;
import com.zjt.startmodepro.recycleview.TraceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TraceActivity extends BaseActivity {

    public static void enter(Context context) {
        Intent intent = new Intent(context, TraceActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private TraceAdapter traceAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_layout);

        recyclerView = findViewById(R.id.trace_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new TraceItemDecoration(this));

        List<String> dateList = new ArrayList<>();
        for (int i = 0; i< 20; i++) {
            dateList.add("2023-11-"+i);
        }

        recyclerView.addItemDecoration(new TimeLineItemDecoration(this, dateList));
        traceAdapter = new TraceAdapter();

        recyclerView.setAdapter(traceAdapter);

        List<String> stringList = new ArrayList<>();
        stringList.add("审批驳回");
        stringList.add("通过");
        stringList.add("你好啊");
        stringList.add("中国万岁");
        stringList.add("哈哈哈哈");
        stringList.add("搞啥呢");
        stringList.add("你好啊");
        stringList.add("瞎写一通");
        stringList.add("审批驳回");
        stringList.add("通过");
        stringList.add("猪猪真棒");
        stringList.add("中国万岁");
        traceAdapter.setDataList(stringList);
    }
}
