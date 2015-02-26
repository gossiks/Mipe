package com.kazin.mvp.stats;

import com.kazin.interfaces.IStatsModel;
import com.kazin.interfaces.IStatsPresenter;
import com.kazin.interfaces.IStatsViewer;
import com.kazin.mvp.Singleton;

import java.util.HashMap;

/**
 * Created by Alexey on 26.01.2015.
 */
public class StatsPresenter implements IStatsPresenter{
    Singleton singleton = Singleton.getInstance();
    IStatsModel model;
    IStatsViewer viewer;

    @Override
    public void onFragmentLoad() {
        model = singleton.getStatsModel();
        model.onFragmentLoad();
    }

    @Override
    public void setNewDataGraph(HashMap<String, Long> graphData) {
        viewer = singleton.getStatsViewer();
        viewer.setNewDataGraph(graphData);
    }

}
