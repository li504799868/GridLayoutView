package com.lzp.grid.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author lizhipeng on 2018.12.25
 * <p>
 * 设置高度的的GridView, 如果需要频繁更新adapter，请不要使用此类
 */
public class MyGridView extends GridView {

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}