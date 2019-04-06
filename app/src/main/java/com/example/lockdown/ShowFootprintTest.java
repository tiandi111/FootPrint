package com.example.lockdown;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.lockdown.database.Footprint;

import java.util.List;

public class ShowFootprintTest extends AppCompatActivity {

    private ShowFootprintViewModel model;

    private TextView mFootprintsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        model = ViewModelProviders.of(this).get(ShowFootprintViewModel.class);
        setContentView(R.layout.footprint_show);
        mFootprintsTextView = findViewById(R.id.fpView);
        model = ViewModelProviders.of(this).get(ShowFootprintViewModel.class);
        subscribeUiFootprints();
    }

    private void subscribeUiFootprints(){
        model.getmFootprints().observe(this, new Observer<List<Footprint>>() {
            @Override
            public void onChanged(@Nullable List<Footprint> footprints) {
                showFootprintsInUi(footprints);
            }
        });
    }

    private void showFootprintsInUi(final @NonNull List<Footprint> footprints) {
        StringBuilder sb = new StringBuilder();

        for (Footprint fp : footprints) {
            sb.append(fp.id);
            sb.append(": ");
            sb.append(fp.title);
            sb.append("\n");
        }
        mFootprintsTextView.setText(sb.toString());
    }

}
