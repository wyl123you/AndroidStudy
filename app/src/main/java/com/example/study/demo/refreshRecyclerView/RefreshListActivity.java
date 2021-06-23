package com.example.study.demo.refreshRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityRefreshListBinding;

import java.util.ArrayList;
import java.util.Random;

public class RefreshListActivity extends BaseActivity<ActivityRefreshListBinding> {

    private static final String TAG = "LoginActivity";
    private LinearLayoutManager manager;
    private int totalY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setClick(new ClickHandler());
        mBinding.textView.setText(TAG);

        final ListAdapter adapter = new ListAdapter(this, getData());

        mBinding.mRecyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        mBinding.mRecyclerView.setLayoutManager(manager);
//        mBinding.mRecyclerView.smoothScrollToPosition(2);

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
                Log.d(TAG, "水平:" + dx + "  垂直:" + dy);
                Log.d(TAG, "是否能向上滑" + recyclerView.canScrollVertically(-1));
                Log.d(TAG, "是否能向下滑: " + recyclerView.canScrollVertically(1));
                totalY += dy;
                mBinding.textView.setTranslationY((float) -totalY);
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

//        //添加系统给的分割线
//        mBinding.mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//
//        //通过使用drawable资源的定制的分割线
//        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.divider_recycler_view);
//
//        assert drawable != null;
//        divider.setDrawable(drawable);
//        mBinding.mRecyclerView.addItemDecoration(divider);

        mBinding.mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
//                if (parent.getChildAdapterPosition(view) % 2 == 0) {
                outRect.bottom = 70;
                outRect.top = 70;
                outRect.left = 70;
                outRect.right = 70;
//                }
            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);

                Paint mPaint = new Paint();
                mPaint.setAntiAlias(true);

                // recyclerView是否设置了paddingLeft和paddingRight
                final int left = parent.getPaddingLeft();
                final int right = parent.getWidth() - parent.getPaddingRight();
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    mPaint.setColor(i % 2 == 0 ? Color.BLACK : Color.RED);
                    mPaint.setTextSize(30);
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    // divider的top 应该是 item的bottom 加上 marginBottom 再加上 Y方向上的位移
                    final int top = child.getBottom() + params.bottomMargin +
                            Math.round(ViewCompat.getTranslationY(child));
                    Log.d(TAG, "child.getBottom(): " + child.getBottom());
                    Log.d(TAG, "params.bottomMargin: " + params.bottomMargin);
                    Log.d(TAG, " Math.round(ViewCompat.getTranslationY(child): " + Math.round(ViewCompat.getTranslationY(child)));
                    //divider的bottom就是top加上divider的高度了
                    final int bottom = top + 140;
                    c.drawRect(left, top, right, bottom, mPaint);
                    c.drawText("" + i, left, top, mPaint);
                }
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);

            }
        });
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
}