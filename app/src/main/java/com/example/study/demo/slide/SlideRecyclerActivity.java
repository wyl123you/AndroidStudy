package com.example.study.demo.slide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;
import com.example.study.demo.refreshRecyclerView.SimpleItemTouchCallBack;

import java.util.ArrayList;

public class SlideRecyclerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_recycler);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            str.add("Content " + i);
        }


        SlideAdapter adapter = new SlideAdapter(str);
        mRecyclerView.setAdapter(adapter);

        SimpleItemTouchCallBack callBack = new SimpleItemTouchCallBack(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private final Paint paint = new Paint();

            // Canvas 参数是 getItemOffsets() 函数所留下的左右上下的空白区域对应的
            // Canvas 画布对象。我们可以在这个区域中利用 Paint 画笔绘制任何图形。
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeWidth(1);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    int childLayoutPosition = parent.getChildLayoutPosition(view);
                    paint.setColor(childLayoutPosition % 2 != 0 ? Color.RED : Color.GREEN);
                    c.drawCircle(view.getWidth() >> 1, view.getTop() + 10, 10, paint);

                    String str = "onDraw的内容实在item下层";
                    Rect rect = new Rect();
                    paint.getTextBounds(str, 0, str.length(), rect);
                    paint.setTextSize(30);
                    c.drawText(str, 0, str.length(), 0, view.getTop() + rect.height(), paint);
                }
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeWidth(1);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    int childLayoutPosition = parent.getChildLayoutPosition(view);
                    paint.setColor(childLayoutPosition % 2 != 0 ? Color.RED : Color.GREEN);
                    c.drawCircle(view.getWidth() >> 1, view.getBottom() - 10, 10, paint);

                    String str = "onDrawOver的内容实在item上层";
                    Rect rect = new Rect();
                    paint.getTextBounds(str, 0, str.length(), rect);
                    paint.setTextSize(30);
                    c.drawText(str, 0, str.length(), view.getRight() - rect.width(), view.getBottom(), paint);
                }
            }

            // 主要作用是在 item 的四周留下边距，效果和 margin 类似
            // item 的四周留下边距后，我们就可以通过 onDraw() 在这个边距上绘制了。
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 20;
                outRect.top = 20;
                outRect.left = 20;
                outRect.right = 20;
            }
        });

//        ProgressItemDecoration decoration = new ProgressItemDecoration(this);
//        decoration.setDoingPosition(mRecyclerView, 3);
//        mRecyclerView.addItemDecoration(decoration);
    }
}