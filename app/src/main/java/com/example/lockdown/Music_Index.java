package com.example.lockdown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.lockdown.ui.RecyclerAdapter;

public class Music_Index extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] musicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.music_index);
        customizedTitle_init();
        bottom_navigation_init();

        recyclerView = (RecyclerView) findViewById(R.id.music_index_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        musicList = new String[]{"童年", "稻香", "公路之歌"};
        mAdapter = new RecyclerAdapter(musicList);
        recyclerView.setAdapter(mAdapter);
    }

    public void customizedTitle_init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText("Music");
    }

    private void bottom_navigation_init() {
        Intent intent_toFp = new Intent(Music_Index.this, Yourfootprint_Index.class);
        Intent intent_toMap = new Intent(Music_Index.this, Maps_Activity.class);
        BottomNavigationView bottomNV = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch ( menuItem.getItemId()) {
                    case R.id.navigation_map:
                        startActivity(intent_toMap);
                        return true;
                    case R.id.navigation_explore:
                        return true;
                    case R.id.navigation_yourfootprint:
                        startActivity(intent_toFp);
                        return true;
                }
                return false;
            }
        });
        //bottomNV.setSelectedItemId(R.id.navigation_yourfootprint);
    }


}
