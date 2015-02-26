package com.kazin.interfaces;

import java.util.HashMap;

/**
 * Created by Alexey on 26.01.2015.
 */
public interface IStatsPresenter {
    public void onFragmentLoad();
    public void setNewDataGraph(HashMap<String,Long> graphData);
}
