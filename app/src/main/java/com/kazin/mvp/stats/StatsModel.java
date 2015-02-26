package com.kazin.mvp.stats;

import android.util.Log;

import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IStatsModel;
import com.kazin.interfaces.IStatsPresenter;
import com.kazin.mipe.R;
import com.kazin.mvp.ParseConstants;
import com.kazin.mvp.Singleton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey on 26.01.2015.
 */
public class StatsModel implements IStatsModel {
    Singleton singleton = Singleton.getInstance();
    IStatsPresenter presenter = singleton.getStatsPresenter();
    @Override
    public void onFragmentLoad() {
        Log.d("StatsModel.OnFragmentLoad", "method called");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_IMAGES_TIME);
        query.whereEqualTo(ParseConstants.KEY_PARENT_USER, ParseUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null) {
                    Log.d("StatsModel.OnFragmentLoad", "done parse request with object: " + parseObject.toString());
                    JSONObject imagesIdTime = parseObject.getJSONObject(ParseConstants.KEY_ID_TIME);
                    int numberOfStatsToDisplay = singleton.getNumberOfStatsToDisplay();
                    if (imagesIdTime!=null) {
                        getStatsSimple(imagesIdTime, numberOfStatsToDisplay);
                    }
                    else{
                        IMipePresenter presenterMipe = singleton.getMipePresenter(); //TODO redo in IStatsPresenter
                        presenterMipe.alertUploadToastShow(singleton.getConxt().getString(R.string.alert_not_enough_stats));
                    }

                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getStatsSimple(final JSONObject imagesIdTime, final int numberOfStatsToDisplay){
        Iterator<String> iter = imagesIdTime.keys();
        ArrayList<String> keys = new ArrayList<String>(); //TODO redo in faster array
            while (iter.hasNext()) {
                keys.add(iter.next());
            }

        ArrayList<String> includeFields = new ArrayList<String>();
        includeFields.add(ParseConstants.KEY_HASHTAGS); //TODO redo faster
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_IMAGES);
        query.whereContainedIn(ParseConstants.KEY_OBJECT_ID, keys);
        query.selectKeys(includeFields);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if (e==null) {
                    Long imageTime = null;
                    List<String> hashTags = null;
                    HashMap<String, Long> hashGraphData = new HashMap<String, Long>();
                    for (ParseObject image : images) {
                        try {
                            imageTime = imagesIdTime.getLong(image.getObjectId());
                            hashTags = image.getList(ParseConstants.KEY_HASHTAGS);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        for (String hashTag : hashTags) {
                            if (hashGraphData.containsKey(hashTag)) {
                                Long time = hashGraphData.get(hashTag);
                                time = time + imageTime;
                                hashGraphData.put(hashTag, time);
                            } else {
                                hashGraphData.put(hashTag, imageTime);
                            }
                        }
                    }

                    hashGraphData = sortHashMapByValuesD(hashGraphData);
                    hashGraphData = getFirstValues(hashGraphData,  numberOfStatsToDisplay);
                    presenter = singleton.getStatsPresenter();
                    presenter.setNewDataGraph(hashGraphData);
                }
            }
        });
    }

    private LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues, Collections.reverseOrder());
        Collections.sort(mapKeys, Collections.reverseOrder());

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Long)val);
                    break;
                }

            }

        }
        return sortedMap;
    }
    private HashMap<String, Long> getFirstValues(HashMap<String,Long> map, int numberOf) {
        HashMap<String, Long> hashMap = new HashMap<>();
        Iterator iterator = map.entrySet().iterator();
        for (int i = 0; i<numberOf; i++){
            Map.Entry pairs = (Map.Entry) iterator.next();
            hashMap.put((String)pairs.getKey(), (Long)pairs.getValue());
        }
        return hashMap;
    }
}
