package com.lzp.grid

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.GridLayout
import android.util.AttributeSet
import android.view.View
import android.widget.BaseAdapter


/**
 * 通过GridLayout实现的网格布局
 *
 * @author li.zhipeng
 */
class GridLayoutView<T>
/**
 * 仿GridV构造方法
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GridLayout(context, attrs) {

    /**
     * 分割线
     * */
    var divider: Divider? = null
        set(value) {
            field = value
            paint.color = value?.color ?: Color.TRANSPARENT
            fillChildInLayout()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 设置该控件的适配器
     *
     */
    var adapter: BaseAdapter? = null
        set(adapter) {
            // 解绑之前的adapter的数据监听
            unregisterDataSetObserver()
            field = adapter
            this.count = field!!.count
            this.adapter!!.registerDataSetObserver(dataSetObserver)
            this.fillChildInLayout()
        }

    /**
     * 仿GridView填充的单元格总数
     */
    private var count: Int = 0
    /**
     * 仿GridView单元格单击事件
     */
    private var onCellClickListener: OnCellClickListener<T?>? = null

    private val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            count = adapter!!.count
            fillChildInLayout()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            count = adapter!!.count
            fillChildInLayout()
        }
    }

    init {
        initAttrs(context, attrs)
    }

    /***
     * 加载xml文件设置的属性值
     *
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.GridLayoutView)
        divider = Divider(
                typedArray.getDimensionPixelSize(
                        R.styleable.GridLayoutView_horizontalSpacing, 0),
                typedArray.getDimensionPixelSize(
                        R.styleable.GridLayoutView_verticalSpacing, 0),
                typedArray.getColor(
                        R.styleable.GridLayoutView_dividerColor, Color.TRANSPARENT)
        )
        typedArray.recycle()
        setWillNotDraw(false)
    }

    /**
     * 通过adapter把item填入到GridLayout中
     */
    private fun fillChildInLayout() {
        if (adapter == null || count == 0) {
            clear()
            return
        }

        post {
            // 判断如果是竖向
            if (orientation == GridLayout.VERTICAL) {
                fillChildInLayoutVertical()
            } else {
                fillChildInLayoutHorizontal()
            }

            // 如果当前child的数量比count要大，移除多余Child
            if (childCount > count) {
                removeViews(count, childCount - count)
            }
        }

    }

    private fun fillChildInLayoutVertical() {
        // 遍历adapter
        for (position in 0 until count) {
            // 得到adapter中的View
            val child = getView(position)
            // 得到布局信息
            val params = generateLayoutParams(child, position)
            // 设置水平方向的间距
            if (isLastColumn(position)) {
                params.rightMargin = 0
            } else {
                params.rightMargin = divider?.horizontalSpace ?: 0
            }// 中间的child
            // 设置竖直方向的间距,
            if (isLastRow(position)) {
                params.bottomMargin = 0
            } else {
                params.bottomMargin = divider?.verticalSpace ?: 0
            }
            // 设置点击之间
            child.setOnClickListener {
                onCellClickListener?.onCellClick(position, this.adapter?.getItem(position) as T)
            }
            child.layoutParams = params
            if (child.parent == null) {
                addView(child)
            }
        }
    }

    private fun fillChildInLayoutHorizontal() {
        // 遍历adapter
        for (position in 0 until count) {
            // 得到adapter中的View
            val child = getView(position)
            // 得到布局信息
            val params = generateLayoutParams(child, position)
            // 设置竖直方向的间距
            if (isLastRow(position)) {
                params.bottomMargin = 0
            } else {
                params.bottomMargin = divider?.verticalSpace ?: 0
            }
            // 设置水平方向的边距
            if (isLastColumn(position)) {
                params.rightMargin = 0
            } else {
                params.rightMargin = divider?.horizontalSpace ?: 0
            }
            // 设置点击之间
            child.setOnClickListener {
                if (onCellClickListener != null) {
                    onCellClickListener!!.onCellClick(position, this.adapter?.getItem(position) as T)
                }
            }
            child.layoutParams = params
            if (child.parent == null) {
                addView(child)
            }
        }
    }

    /**
     * 从cache中得到View，没有则创建
     */
    private fun getView(index: Int): View {
        // 首先遍历已经存在的child，直接更新内容
        // 这样可以节省清空再填充的性能浪费
        var view: View? = null
        if (index < count) {
            view = getChildAt(index)
        }
        // 更新数据显示
        view = this.adapter!!.getView(index, view, this)
        return view
    }

    /**
     * 生成LayoutParams
     */
    private fun generateLayoutParams(child: View, position: Int): GridLayout.LayoutParams {
        val params: GridLayout.LayoutParams = if (child.layoutParams != null) {
            child.layoutParams as GridLayout.LayoutParams
        } else {
            GridLayout.LayoutParams()
        }
        // 设置宽度
        if (orientation == VERTICAL) {
            params.width = width / 3
            // 设置所占的行数
            params.columnSpec = GridLayout.spec(position % columnCount, 1)
            params.rowSpec = GridLayout.spec(position / columnCount, 1)
        } else {
            params.height = height / 3
            // 设置所占的行数
            params.rowSpec = GridLayout.spec(position % rowCount, 1, 1f)
            params.columnSpec = GridLayout.spec(position / rowCount, 1)
        }
        return params
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterDataSetObserver()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (divider == null) {
            return
        }

        for (index in 0..count) {
            val child = getChildAt(index) ?: continue
            // 如果不是最后一列，要画右边的divider
            if (!isLastColumn(index)) {
                // 竖线
                paint.strokeWidth = divider!!.horizontalSpace.toFloat()
                val right = child.right.toFloat() + divider!!.horizontalSpace / 2
                // 竖线的长度
                // 竖线和横线的交叉部分，优先画横线，如果已经有横线，交叉部分不画竖线
                val verticalDividerLength = if (divider!!.verticalSpace == 0) {
                    child.bottom.toFloat() + divider!!.verticalSpace
                } else {
                    child.bottom.toFloat()
                }
                canvas.drawLine(right, child.top.toFloat(), right, verticalDividerLength, paint)
            }

            // 如果不是最后一行，要画下面的divider
            if (!isLastRow(index)) {
                // 横线
                paint.strokeWidth = divider!!.verticalSpace.toFloat()
                val bottom = child.bottom.toFloat() + divider!!.verticalSpace / 2
                canvas.drawLine(child.left.toFloat(), bottom, child.right.toFloat() + divider!!.horizontalSpace, bottom, paint)
            }
        }
    }

    /**
     * 是否最后一列
     * */
    private fun isLastColumn(index: Int): Boolean {
        return if (orientation == VERTICAL) {
            index % 3 == columnCount - 1
        } else {
            val columnCount = if (count % rowCount > 0) {
                count / rowCount + 1
            } else {
                count / rowCount
            }
            columnCount.times(rowCount) - index <= rowCount
        }
    }

    /**
     * 是否是最后一行
     * */
    private fun isLastRow(index: Int): Boolean {
        return if (orientation == VERTICAL) {
            val rowCount = if (count % columnCount > 0) {
                count / columnCount + 1
            } else {
                count / columnCount
            }
            rowCount.times(columnCount) - index <= columnCount
        } else {
            index % rowCount == rowCount - 1
        }
    }

    private fun unregisterDataSetObserver() {
        try {
            this.adapter?.unregisterDataSetObserver(dataSetObserver)
        } catch (ignore: Exception) {
            // 忽略异常，可能是因为dataSetObserver没有被注册导致的
        }
    }

    /**
     * 清除所有的child，并加入到缓存中
     */
    private fun clear() {
        removeAllViews()
    }

    interface OnCellClickListener<T> {
        fun onCellClick(index: Int, t: T)
    }

    /**
     * 监听响应单元格单击事件
     *
     * @param onCellClickListener 单元格单击事件接口
     */
    fun setOnCellClickListener(onCellClickListener: OnCellClickListener<T?>) {
        this.onCellClickListener = onCellClickListener
    }

    class Divider(var horizontalSpace: Int = 0, var verticalSpace: Int = 0, var color: Int)

}