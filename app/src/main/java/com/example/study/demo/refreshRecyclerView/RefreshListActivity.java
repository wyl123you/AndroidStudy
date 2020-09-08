package com.example.study.demo.refreshRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityRefreshListBinding;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

public class RefreshListActivity extends BaseActivity<ActivityRefreshListBinding> {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.textView)
    public TextView textView;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setClick(new ClickHandler());
        mBinding.textView.setText(TAG);

        final ListAdapter adapter = new ListAdapter(this, getData());

        mBinding.mRecyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        mBinding.mRecyclerView.setLayoutManager(manager);
        //mBinding.mRecyclerView.smoothScrollToPosition();

        mBinding.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //滚动状态变化时回调  recyclerView : 当前在滚动的RecyclerView   //newState : 当前滚动状态.
            //newState==0  静止  RecyclerView.SCROLL_STATE_IDLE
            //newState==1  手指拖动滑  SCROLL_STATE_DRAGGING
            //newState==2  松手后滑动  SCROLL_STATE_SETTLING
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {

                    Log.d(TAG, "静止" + newState);
                } else if (newState == 1) {
                    Log.d(TAG, "手指拖动滑" + newState);
                } else if (newState == 2) {
                    Log.d(TAG, "松手后滑动" + newState);
                } else {
                    Log.d(TAG, "" + newState);
                }
            }

            //滚动时回调  recyclerView : 当前滚动的view
            //dx : 水平滚动距离
            //dy : 垂直滚动距离
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mBinding.textView.setVisibility(dy > 0 ? View.GONE : View.VISIBLE);
                Log.d(TAG, "水平:" + dx + "  垂直:" + dy);
                Log.d(TAG, "是否能向上滑" + recyclerView.canScrollVertically(-1));
                Log.d(TAG, "是否能向下滑: " + recyclerView.canScrollVertically(1));
                showTitle();
            }
        });


        mBinding.refresh.setOnRefreshListener(refreshLayout -> {
            adapter.refreshData(getData());
            refreshLayout.finishRefresh(2000);
            Log.d(TAG, "上拉刷新");
        });

        mBinding.refresh.setOnLoadMoreListener(refreshLayout -> {
            adapter.addData(getData());
            refreshLayout.finishLoadMore(2000);
            Log.d(TAG, "下拉加载");
        });

        // 拖拽移动和左滑删除
        SimpleItemTouchCallBack simpleItemTouchCallBack = new SimpleItemTouchCallBack(adapter);
        // 要实现侧滑删除条目，把 false 改成 true 就可以了
        simpleItemTouchCallBack.setSwipeEnable(true);
        ItemTouchHelper helper = new ItemTouchHelper(simpleItemTouchCallBack);
        helper.attachToRecyclerView(mBinding.mRecyclerView);

        //添加系统给的分割线
        mBinding.mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //通过使用drawable资源的定制的分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.divider_recycler_view));
        mBinding.mRecyclerView.addItemDecoration(divider);
    }

    @Override
    protected void initViews() {

    }

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int a = random.nextInt(200);
            data.add("数据" + a);
            Log.d(TAG, "数据" + a);
        }
        return data;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refresh_list;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    protected static void newInstance(Context context) {
        context.startActivity(new Intent(context, RefreshListActivity.class));
    }


    public static class ClickHandler {
        public void onLogin() {
            Log.d(TAG, "onLogin");
        }

        public void onWeChatLogin() {
            Log.d(TAG, "onWeChatLogin");
        }
    }

    public int getScrollYDistance() {
        int position = manager.findFirstVisibleItemPosition();
        View firstVisibleChildView = manager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }

    private void showTitle() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.hybrid);
        mBinding.textView.startAnimation(animation);
    }
}