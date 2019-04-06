package com.example.lockdown;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.lockdown.database.AppDatabase;
import com.example.lockdown.database.DataInitializer;

public class Yourfootprint_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String S =bundle.getString("Loc");

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_management);
        customizedTitle_init();
        bottom_nav_init();

        ((Button) findViewById(R.id.ForYou))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Yourfootprint_Activity.this,
                                    Yourfootprint_Index.class);
                            startActivity(intent);
                        }
                    });


        DataInitializer.init(AppDatabase.getInstance(getApplicationContext()));
    }
    // Set bottom navigation bar
    private void bottom_nav_init() {
        Intent intent_toMap = new Intent(Yourfootprint_Activity.this, Maps_Activity.class);
        Intent intent_toInd = new Intent(Yourfootprint_Activity.this, Yourfootprint_Index.class);
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
                        startActivity(intent_toInd);
                        return true;
                }
                return false;
            }
        });
    }

    public void customizedTitle_init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText("Footprint name");
    }
}
