package com.example.lockdown;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.lockdown.database.AppDatabase;
import com.example.lockdown.database.Footprint;

import java.util.List;

public class ShowFootprintViewModel extends AndroidViewModel{
    // Create a LiveData with a String

    private LiveData<List<Footprint>> mFootprints;

    private AppDatabase mDb;

    public ShowFootprintViewModel(Application application){
        super(application);
        mDb = AppDatabase.getInstance(this.getApplication());
        mFootprints = mDb.fpModel().findAllFootprints();
    }

    public LiveData<List<Footprint>> getmFootprints(){
        return mFootprints;
    }

    public void subscribeToDbChanges(){
        mFootprints = mDb.fpModel().findAllFootprints();
    }

}
