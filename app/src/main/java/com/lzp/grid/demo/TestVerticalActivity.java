package com.lzp.grid.demo;

import android.os.Bundle;

import com.lzp.grid.GridLayoutView;

import java.util.ArrayList;
import java.util.List;

public class TestVerticalActivity extends BaseActivity {

    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i ++){
            list.add(String.valueOf(i));
        }
        adapter = new TestAdapter(this, list);
        GridLayoutView gridView = findViewById(R.id.grid);
        gridView.setAdapter(adapter);
        findViewById(R.id.reduce).setOnClickListener(v -> {
            list.remove(list.get(0));
            adapter.notifyDataSetChanged();
        });

        findViewById(R.id.add).setOnClickListener(v -> {
            list.add(String.valueOf(Integer.parseInt(list.get(list.size() - 1)) + 1));
            adapter.notifyDataSetChanged();
        });
    }
}
