package com.example.lockdown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lockdown.ui.music.MusicFragment;

public class FootPrintManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MusicFragment.newInstance())
                    .commitNow();
        }
    }
}
