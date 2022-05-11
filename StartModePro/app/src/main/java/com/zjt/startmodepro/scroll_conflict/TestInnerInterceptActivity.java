package com.zjt.startmodepro.scroll_conflict;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.startmodepro.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/1/7
 * 测试 ScrollView 中嵌套 RecycleView 同向滑动的滑动冲突；
 * ScrollView 默认是拦截 MOVE 事件的
 */
public class TestInnerInterceptActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_intercept_layout);
        recyclerView = findViewById(R.id.recycle_view);
        onInitLogic();

        Executors.newFixedThreadPool(3);
    }


    private void onInitLogic() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CacheAdapter cacheAdapter = new CacheAdapter();
        recyclerView.setAdapter(cacheAdapter);

        List<String> dataList = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            dataList.add("数据" + i);
        }

        cacheAdapter.setDataList(dataList);
    }
}
