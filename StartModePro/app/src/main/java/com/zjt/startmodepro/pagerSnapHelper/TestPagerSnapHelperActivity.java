package com.zjt.startmodepro.pagerSnapHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.zjt.startmodepro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * PagerSnapHelper 加上 RecycleView 实现类似 ViewPager 的效果
 */
public class TestPagerSnapHelperActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestAdapter testAdapter;
    private STLView mSTLView;
    private boolean mOpenAll;

    public static void enter(Context context) {
        Intent intent = new Intent(context, TestPagerSnapHelperActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager_snaphelper_layout);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        testAdapter = new TestAdapter();
        recyclerView.setAdapter(testAdapter);

        SnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        List<String> dataList = new ArrayList<>(5);
        dataList.add("安徽");
        dataList.add("浙江");
        dataList.add("上海");
        dataList.add("北京");
        dataList.add("江苏");
        testAdapter.setDataList(dataList);

        // add 1
        // 添加无用注释

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                if (view instanceof STLView) {
                    mSTLView = (STLView) view;
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (view instanceof STLView) {
                    mSTLView = null;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mOpenAll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.e("pagerHelper", "open");
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOpenAll = !canScrollMore();
                Log.e("pagerHelper", "mOpenAll = " + mOpenAll);
                if (mSTLView != null) {
                    mSTLView.setPullStatus(!mOpenAll);
                }
            }
        });
    }

    private boolean canScrollMore() {
        // 正数表示向左的滑动检测，负数表示向右的滑动检测
        return recyclerView.canScrollHorizontally(10);
    }
}
