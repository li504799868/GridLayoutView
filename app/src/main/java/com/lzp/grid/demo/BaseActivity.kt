package com.lzp.grid.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle



/**
 * Created by li.zhipeng on 2018/12/25.
 */
abstract class BaseActivity :AppCompatActivity() {

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set content view, etc.
        ViewServer.get(this).addWindow(this)
    }

    protected override fun onDestroy() {
        super.onDestroy()
        ViewServer.get(this).removeWindow(this)
    }

    protected override fun onResume() {
        super.onResume()
        ViewServer.get(this).setFocusedWindow(this)
    }
}