package com.kazin.mvp.stats;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kazin.interfaces.IStatsPresenter;
import com.kazin.interfaces.IStatsViewer;
import com.kazin.mipe.R;
import com.kazin.mvp.Singleton;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey on 26.01.2015.
 */
public class StatsViewer implements IStatsViewer {
    Singleton singleton = Singleton.getInstance();
    IStatsPresenter presenter;

    @Override
    public void onFragmentLoad() {
        presenter = singleton.getStatsPresenter();
        presenter.onFragmentLoad();
    }

    @Override
    public void setNewDataGraph(HashMap<String, Long> points) {
        GraphView graph =  singleton.getStatsGraph();
        graph.removeAllSeries();
        graph.setTitle(singleton.getConxt().getString(R.string.stats_plot_title));
        Viewport viewport = graph.getViewport();
        viewport.setScalable(false);
        viewport.setMaxX(4);
        viewport.setMinX(0);
        viewport.setXAxisBoundsManual(true);

        Log.d("viewer.setNewDataGraph", points.toString());
        DataPoint[] dataPoints = new DataPoint[points.size()];
        String[] horizontalLabels = new String[points.size()];
        int i=0;
        for(Map.Entry<String,Long> entry:points.entrySet()){
            dataPoints[i] = new DataPoint(i, entry.getValue());
            horizontalLabels[i] = entry.getKey();
            i++;
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        graph.addSeries(series);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(horizontalLabels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }


}
