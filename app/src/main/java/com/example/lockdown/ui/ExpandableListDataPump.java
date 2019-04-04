package com.example.lockdown.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> fp1 = new ArrayList<String>();
        fp1.add("Music");
        fp1.add("Pictures");
        fp1.add("Videos");
        fp1.add("Words");
        fp1.add("...");

        List<String> fp2 = new ArrayList<String>();
        fp2.add("Music");
        fp2.add("Pictures");
        fp2.add("...");

        List<String> fp3 = new ArrayList<String>();
        fp3.add("Music");
        fp3.add("Videos");
        fp3.add("Words");
        fp3.add("...");

        expandableListDetail.put("OK", fp1);
        expandableListDetail.put("Library", fp2);
        expandableListDetail.put("Gym", fp3);
        expandableListDetail.put("cnm", new ArrayList());
        return expandableListDetail;
    }
}
