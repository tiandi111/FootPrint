package com.example.lockdown;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.lockdown.ui.CustomExpandableListAdapter;
import com.example.lockdown.ui.ExpandableListDataPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Yourfootprint_Index extends AppCompatActivity {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.yourfootprint_index);

        customizedTitle_init();
        bottom_nav_init();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent_toAct = new Intent(Yourfootprint_Index.this, Yourfootprint_Activity.class);
                intent_toAct.putExtra("Loc", expandableListTitle.get(groupPosition));
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                switch ( expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition)) {
                    case "...":
                        startActivity(intent_toAct);
                        return true;
                }
                return false;
            }
        });

    }
    // Set bottom navigation bar
    private void bottom_nav_init() {
        Intent intent_toMap = new Intent(Yourfootprint_Index.this, Maps_Activity.class);
        BottomNavigationView bottomNV = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        startActivity(intent_toMap);
                        return true;
                    case R.id.navigation_explore:
                        return true;
                    case R.id.navigation_yourfootprint:
                        return true;
                }
                return false;
            }
        });
        bottomNV.setSelectedItemId(R.id.navigation_yourfootprint);
    }

    public void customizedTitle_init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText("My Footprints");
    }

}
