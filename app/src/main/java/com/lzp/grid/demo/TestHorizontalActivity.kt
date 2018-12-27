package com.lzp.grid.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.lzp.grid.GridLayoutView

import java.util.ArrayList

class TestHorizontalActivity : BaseActivity() {

    private var adapter: TestAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_horizontal)

        val list = ArrayList<String>()
        for (i in 0..9) {
            list.add(i.toString())
        }
        adapter = TestAdapter(this, list)
        val gridView = findViewById<GridLayoutView<String>>(R.id.grid)
        gridView.setOnCellClickListener(object : GridLayoutView.OnCellClickListener<String?>{
            override fun onCellClick(index: Int, t: String?) {
                Toast.makeText(this@TestHorizontalActivity, t, Toast.LENGTH_SHORT).show()
            }

        })
        gridView.adapter = adapter
        findViewById<View>(R.id.reduce).setOnClickListener {
            list.remove(list[0])
            adapter!!.notifyDataSetChanged()
        }

        findViewById<View>(R.id.add).setOnClickListener {
            list.add((Integer.parseInt(list[list.size - 1]) + 1).toString())
            adapter!!.notifyDataSetChanged()
        }
    }
}
