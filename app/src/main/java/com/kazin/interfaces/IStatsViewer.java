package com.kazin.interfaces;

import java.util.HashMap;

/**
 * Created by Alexey on 26.01.2015.
 */
public interface IStatsViewer {
    public void onFragmentLoad();
    public void setNewDataGraph(HashMap<String, Long> points);
}
