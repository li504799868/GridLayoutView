package com.lzp.grid.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * Created by li.zhipeng on 2018/12/25.
 */
class TestAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

    override fun getItem(position: Int): String = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        }
        view!!.findViewById<TextView>(R.id.text).text = list[position]
        return view
    }


}