package com.lzp.grid;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridLayout;


/**
 * 通过GridLayout实现的网格布局
 *
 * @author li.zhipeng
 */
public class GridLayoutView extends GridLayout {

    /**
     * 布局水平分割线宽度
     */
    private int horizontalSpace = 0;
    /**
     * 布局垂直分割线宽度
     */
    private int verticalSpace = 0;

    /**
     * 仿GridView单元格适配器
     */
    private BaseAdapter adapter;

    /**
     * 仿GridView填充的单元格总数
     */
    private int count;
    /**
     * 仿GridView单元格单击事件
     */
    private OnCellClickListener onCellClickListener;

    /**
     * 仿GridView构造方法
     */
    public GridLayoutView(Context context) {
        this(context, null);
    }

    /**
     * 仿GridV构造方法
     */
    public GridLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    /***
     * 加载xml文件设置的属性值
     *
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.GridLayoutView);
        horizontalSpace = typedArray.getDimensionPixelSize(
                R.styleable.GridLayoutView_horizontalSpacing, 0);
        verticalSpace = typedArray.getDimensionPixelSize(
                R.styleable.GridLayoutView_verticalSpacing, 0);
        typedArray.recycle();
    }

    /**
     * 设置该控件的适配器
     *
     * @param adapter 自定义适配器
     */
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        this.count = adapter.getCount();
        this.adapter.registerDataSetObserver(dataSetObserver);
        post(this::fillChildInLayout);

    }

    /**
     * 通过adapter把item填入到GridLayout中
     */
    private void fillChildInLayout() {
        // 判断如果是竖向
        if (getOrientation() == VERTICAL) {
            fillChildInLayoutVertical();
        } else {
            fillChildInLayoutHorizontal();
        }

        // 如果当前child的数量比count要大，移除多余Child
        if (getChildCount() > count) {
            removeViews(count, getChildCount() - count);
        }

    }

    private void fillChildInLayoutVertical() {
        int columnCount = getColumnCount();
        // 计算宽度
        int width = getWidth();
        // 减去水平间距
        int childWidth = (width - horizontalSpace * (columnCount - 1)) / columnCount;
        // 遍历adapter
        for (int position = 0; position < count; position++) {
            // 得到adapter中的View
            View child = getView(position);
            // 得到布局信息
            MarginLayoutParams params = generateLayoutParams(child, childWidth, -1, position);
            // 设置水平方向的间距
            if (position % columnCount == columnCount - 1) {
                params.rightMargin = 0;
            }
            // 中间的child
            else {
                params.rightMargin = horizontalSpace;
            }
            // 设置竖直方向的间距,
            if (position > count - 1 - columnCount) {
                params.bottomMargin = 0;
            } else {
                params.bottomMargin = verticalSpace;
            }
            // 设置点击之间
            int finalPosition = position;
            child.setOnClickListener(v -> {
                if (onCellClickListener != null) {
                    onCellClickListener.onCellClick(finalPosition, adapter.getItem(finalPosition));
                }
            });
            child.setLayoutParams(params);
            if (child.getParent() == null) {
                addView(child);
            }
        }
    }

    private void fillChildInLayoutHorizontal() {
        int rowCount = getRowCount();
        // 计算宽度
        int height = getHeight();
        // 减去水平间距
        int childHeight = (height - verticalSpace * (rowCount - 1)) / rowCount;
        // 遍历adapter
        for (int position = 0; position < count; position++) {
            // 得到adapter中的View
            View child = getView(position);
            // 得到布局信息
            LayoutParams params = generateLayoutParams(child, -1, childHeight, position);
            // 设置竖直方向的间距
            if (position % rowCount == rowCount - 1) {
                params.bottomMargin = 0;
            } else {
                params.bottomMargin = verticalSpace;
            }
            // 设置水平方向的边距
            if (position > count - 1 - rowCount) {
                params.rightMargin = 0;
            } else {
                params.rightMargin = horizontalSpace;
            }
            // 设置点击之间
            int finalPosition = position;
            child.setOnClickListener(v -> {
                if (onCellClickListener != null) {
                    onCellClickListener.onCellClick(finalPosition, adapter.getItem(finalPosition));
                }
            });
            child.setLayoutParams(params);
            if (child.getParent() == null) {
                addView(child);
            }
        }
    }

    /**
     * 从cache中得到View，没有则创建
     */
    private View getView(int index) {
        // 首先遍历已经存在的child，直接更新内容
        // 这样可以节省清空再填充的性能浪费
        View view = null;
        if (index < count) {
            view = getChildAt(index);
        }
        // 更新数据显示
        view = adapter.getView(index, view, this);
        return view;
    }

    /**
     * 生成LayoutParams
     */
    private LayoutParams generateLayoutParams(View child, int width, int height, int position) {
        LayoutParams params;
        if (child.getLayoutParams() != null) {
            params = (LayoutParams) child.getLayoutParams();
        } else {
            params = new LayoutParams();
        }
        // 设置宽度
        if (width != -1) {
            params.width = width;
            // 设置所占的行数
            params.columnSpec = GridLayout.spec(position % getColumnCount(), 1);
            params.rowSpec = GridLayout.spec(position / getColumnCount(), 1);
        }
        // 设置高度
        if (height != -1) {
            params.height = height;
            // 设置所占的行数
            params.rowSpec = GridLayout.spec(position % getRowCount(), 1);
            params.columnSpec = GridLayout.spec(position / getRowCount(), 1);
        }
        return params;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adapter != null) {
            try {
                adapter.unregisterDataSetObserver(dataSetObserver);
            } catch (Exception ignore) {
                // 忽略异常，可能是因为dataSetObserver没有被注册导致的
            }
        }
    }

    /***
     * 返回该控件的适配器
     *
     */
    public BaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 返回该控件各单元格水平间距
     */
    public int getHorizontalSpace() {
        return horizontalSpace;
    }

    /**
     * 设置该控件各单元格水平间距
     */
    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    /**
     * 返回该控件各单元格垂直间距
     */
    public int getVerticalSpace() {
        return verticalSpace;
    }

    /**
     * 设置该控件各单元格垂直间距
     */
    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    /**
     * 清除所有的child，并加入到缓存中
     */
    private void clear() {
        removeAllViews();
    }

    interface OnCellClickListener<T> {
        void onCellClick(int index, T t);
    }

    /**
     * 监听响应单元格单击事件
     *
     * @param onCellClickListener 单元格单击事件接口
     */
    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            count = adapter.getCount();
            fillChildInLayout();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            count = adapter.getCount();
            fillChildInLayout();
        }
    };


}