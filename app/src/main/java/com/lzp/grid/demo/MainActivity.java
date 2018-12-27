package com.lzp.grid.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TestAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }

        adapter = new TestAdapter(this, list);
        GridView gridView = findViewById(R.id.grid);
        gridView.setAdapter(adapter);
        findViewById(R.id.refresh).setOnClickListener(v -> {
            list.remove(list.get(0));
            adapter.notifyDataSetChanged();
            gridView.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.testV).setOnClickListener(v-> startActivity(new Intent(MainActivity.this, TestVerticalActivity.class)));
        findViewById(R.id.testH).setOnClickListener(v-> startActivity(new Intent(MainActivity.this, TestHorizontalActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            list.remove(list.get(0));
            adapter.notifyDataSetChanged();
        }
    }
}
