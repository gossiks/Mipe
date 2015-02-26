package com.kazin.mvp.mipeflip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.kazin.interfaces.IMipeModel;
import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IMipeViewer;
import com.kazin.mipe.R;
import com.kazin.mvp.ParseConstants;
import com.kazin.mvp.Singleton;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by @gossiks on 05.01.2015.
 */
public class MipeModel implements IMipeModel{
    private Singleton singleton = Singleton.getInstance();
    Context context;
    IMipePresenter presenter;
    private PagerAdapter adapter = null;
    private String usernameForSession = null;
    private String usernameForSessionFb = null;
    private String avatarUrl=null;
    private String avatarUrlFB=null;

    @Override
    public void onCreateView(PagerAdapter adapter) {
        presenter = singleton.getMipePresenter();
        presenter.setMipeListener(getMipeOnPageListener());
        presenter.setProgressBarsVisible(true);
        presenter.setPagerMipeVisible(false);
        Log.d("MipeModel/onCreateView", "this.adapter = "+this.adapter );
        if(this.adapter==null) {
            retrieveNewDataWithCallbacks(20, new AfterGetDataMipeCallback(), new GetFileDataMipeProgressCallback());
        }
        else {
            presenter.setProgressBarsVisible(false);
            presenter.setPagerMipeVisible(true);
            presenter.setMipeAdapter(this.adapter);
        }
    }
       @Override
    public void setUpUserNameImage() {
           presenter = singleton.getMipePresenter();
           if (usernameForSession!=null){
               presenter.setUsername(usernameForSession);
           }
           else {
               usernameForSession = ParseUser.getCurrentUser().getUsername();
               presenter.setUsername(usernameForSession);
           }
           if(avatarUrl==null){
               try {
                   avatarUrl = ParseUser.getCurrentUser().getParseFile(ParseConstants.KEY_AVATAR).getUrl();
                   presenter.setAvatar(avatarUrl);
               } catch (Exception e) {
                   try { //TODO get rid of try-try
                       e.printStackTrace();
                   } catch (Exception e1) {
                       e1.printStackTrace();
                   }
               }
           }
           else {
               presenter.setAvatar(avatarUrl);
           }
           if(ParseUser.getCurrentUser().has(ParseConstants.KEY_AUTH_DATA)& usernameForSessionFb==null){
               getFBprofile();
           }
           else if(usernameForSessionFb!=null){
               presenter = singleton.getMipePresenter();
               presenter.setUsername(usernameForSessionFb);
           }



    }

    @Override
    public void onResume() {
        singleton.setStartTime();
    }

    @Override
    public void onActionButtonUpload() {
        uploadMipeData();
        presenter = singleton.getMipePresenter();
        presenter.setProgressBarsVisible(true);
        presenter.setPagerMipeVisible(false);
        retrieveNewDataWithCallbacks(20, new AfterGetDataMipeCallback(), new GetFileDataMipeProgressCallback());
    }

    private  void retrieveNewDataWithCallbacks(int photosReceive, final AfterGetDataMipeCallback afterGetData, final GetFileDataMipeProgressCallback getFileDataMipeProgress){

        HashMap<String, Object> params = new HashMap<String, Object>();
        int numberOfPhotosRequested = photosReceive; //remember that cloud code can give 20 at max
        params.put("numberOfPhotosRequested", numberOfPhotosRequested);
        presenter = singleton.getMipePresenter();
        presenter.setProgressBarsVisible(true);
        presenter.setPagerMipeVisible(false);

        ParseCloud.callFunctionInBackground("getImageArray", params, new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects!=null) {
                        parseObjects.removeAll(Arrays.asList("null", null));
                        if(parseObjects.size() == 0){
                            noImages();
                        }
                    else {
                            AfterGetDataMipeCallback aftergetdata = new AfterGetDataMipeCallback();
                            aftergetdata.onGetData(parseObjects);
                            //parseObjectGetAllFileDataBackground getData = new parseObjectGetAllFileDataBackground(
                            //        parseObjects, afterGetData, getFileDataMipeProgress);
                            //getData.start();
                        }
                }
                else{
                    presenter = singleton.getMipePresenter();
                    presenter.alertUploadToastShow("Nothing to show. Error: "+e.getCode());
                    Log.d("callCloud getImageArray", "nothing to show");
                    e.printStackTrace();
                    someErrorLoading();
                }
            }
        });
    }

    private void someErrorLoading() {
        presenter = singleton.getMipePresenter();
        presenter.setPagerMipeVisible(false);
        presenter.setProgressBarsVisible(false);
        presenter.alertUploadToastShow(singleton.getConxt().getString(R.string.alert_error_uploading_usage_data));
    }

    private void noImages() {
        presenter = singleton.getMipePresenter();
        presenter.setPagerMipeVisible(false);
        presenter.setProgressBarsVisible(false);
        presenter.alertUploadToastShow(singleton.getConxt().getString(R.string.alert_seen_all_images));
    }


    private ViewPager.OnPageChangeListener getMipeOnPageListener(){
        singleton.setStartTime();
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == (singleton.getMaxPosition())){
                    uploadMipeData();
                    retrieveNewDataWithCallbacks(20, new AfterGetDataMipeCallback(), new GetFileDataMipeProgressCallback()); //TODO dynamically refreshing pager
                }
                else {
                    int oldPosition = singleton.getOldPosition();
                    if (oldPosition!=singleton.getMaxPosition()){
                        long time = System.currentTimeMillis() - singleton.getStartTime();
                        singleton.plusMipeImageTime(oldPosition, time);
                        singleton.setStartTime();
                        singleton.setOldPosition(position);
                    }
                    else if (oldPosition == singleton.getMaxPosition()) {
                        singleton.setStartTime();
                        singleton.setOldPosition(position);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        return onPageChangeListener;
    }




    private void uploadMipeData(){

        if (singleton.getMipeImageArrayL() == null ){
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_IMAGES_TIME);
        query.whereEqualTo(ParseConstants.KEY_PARENT_USER, ParseUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e==null) {
                    saveImageDataToCloud(parseObject);
                }
                else {
                    ParseObject newImagesTime = new ParseObject(ParseConstants.CLASS_IMAGES_TIME);
                    newImagesTime.put(ParseConstants.KEY_PARENT_USER, ParseUser.getCurrentUser().getObjectId());
                    saveImageDataToCloud(newImagesTime);
                }

            }
        });
    }



    private void saveImageDataToCloud(ParseObject parseObject) {
        ArrayList<MipeImage> mipeImageArrayList = singleton.getMipeImageArrayL();
        String[] ids = new String[mipeImageArrayList.size()];
        Long[] imageTime = new Long[mipeImageArrayList.size()];
        int i = 0;
        JSONObject jsonMap = new JSONObject();
        for (MipeImage image:mipeImageArrayList){
            ids[i] = image.getId();
            imageTime[i] = image.getImageTime();
            try {
                jsonMap.put(image.getId(),image.getImageTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
            Log.d("SaveImageTo cloud:", image.getId() + " with time " +image.getImageTime());
        }


        if(parseObject.has(ParseConstants.KEY_ID_TIME)){
            JSONObject jsonMapOld =  parseObject.getJSONObject(ParseConstants.KEY_ID_TIME);
            try {
                jsonMap = mergeJson(jsonMapOld, jsonMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("HAsObject", "jsonmap is: " + jsonMap.toString());
        }


        Set<String> idsSet = new HashSet<String>(Arrays.asList(ids)); //TODO redo simpler
        Set<Long> imageTimeSet = new HashSet<Long>(Arrays.asList(imageTime));

        parseObject.addAllUnique(ParseConstants.KEY_IMAGES_ID, idsSet);
        parseObject.put(ParseConstants.KEY_ID_TIME, jsonMap);

        Log.d("saveimagedatatocloud", " parseobject  id: "+parseObject.getObjectId());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e!=null) {
                    Log.d("SaveImageDataTocloud", "Error:"+e.getCode());

                }
                else {
                    Log.d("SaveImageDataTocloud", "Successfully saved data to cloud");
                    //retrieveNewDataWithCallbacks();
                }
            }
        });
    }

    private JSONObject mergeJson(JSONObject o1, JSONObject o2) throws JSONException {
        //I assume that your two JSONObjects are o1 and o2
        JSONObject mergedObj = new JSONObject();

        Iterator i1 = o1.keys();
        Iterator i2 = o2.keys();
        String tmp_key;
        while(i1.hasNext()) {
            tmp_key = (String) i1.next();
            mergedObj.put(tmp_key, o1.get(tmp_key));
        }
        while(i2.hasNext()) {
            tmp_key = (String) i2.next();
            mergedObj.put(tmp_key, o2.get(tmp_key));
        }
        return mergedObj;
    }

    public class parseObjectGetAllFileDataBackground {
        private int mI;
        private int max;
        private List<ParseFile> fileList;
        private List<ParseObject> objectList;
        private AfterGetDataMipeCallback afterGetDataMipeCallback;
        private GetFileDataMipeProgressCallback getFileDataMipeProgressCallback;
        private long startTime = 0;
        private long currentTime;
        private long maxRunTime =1000; //run time should not exceed this value (ms)

        parseObjectGetAllFileDataBackground(List<ParseObject> list, AfterGetDataMipeCallback afterGetData, GetFileDataMipeProgressCallback getFileDataMipeProgress){
            afterGetDataMipeCallback = afterGetData;
            getFileDataMipeProgressCallback = getFileDataMipeProgress;
            objectList = list;
            mI = list.size();
            max = mI;
            fileList = new ArrayList<ParseFile>(mI);
            for (int i= 0; i<mI;i++){
                fileList.add(list.get(i).getParseFile(ParseConstants.KEY_FILE));
            }

        }

        private void start(){
            if(startTime==0){
                startTime = System.currentTimeMillis();
            }


            mI = mI-1;
            if (mI<0){
                afterGetDataMipeCallback.onGetData(objectList);
            }
            else{
                fileList.get(mI).getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e!=null){e.printStackTrace();}

                        start(); // no big deal if data is not loaded, because th ParseImageView will handle the rest.
                    }
                }, new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {
                        currentTime = System.currentTimeMillis();
                        if(currentTime-startTime<=maxRunTime){
                            afterGetDataMipeCallback.onGetData(objectList);
                            return;
                        }
                        else {
                            getFileDataMipeProgressCallback.onProgress(integer, mI, max);
                        }
                    }
                });
            }

        }
    }

    private class AfterGetDataMipeCallback {
        public void onGetData(List<ParseObject> parseObjects){
            presenter = singleton.getMipePresenter();
            context = singleton.getConxt();
            Log.d("retrieveNewDataWithCallbacks", String.valueOf(parseObjects.size()));
            adapter = new PagerImageAdapter(singleton.getConxt(),  parseObjects);
            presenter.setProgressBarsVisible(false);
            presenter.setPagerCurrentItem(0);
            presenter.setMipeAdapter(adapter);
            presenter.setPagerMipeVisible(true);
        }
    }
    private class GetFileDataMipeProgressCallback {
        public void onProgress(Integer percent, int currentFetchingImage, int overallImages){
            presenter = singleton.getMipePresenter();
            presenter.setLoadingProcess(percent, currentFetchingImage*20, overallImages);
        }
    }


    private class addViewsProgressCallback extends GetFileDataMipeProgressCallback{
        @Override
        public void onProgress(Integer percent, int currentFetchingImage, int overallImages){
            //eventially leave blank
        }
    }

    public void getFBprofile() {
        ParseFacebookUtils.initialize("1525120017751386");
        final Session session = ParseFacebookUtils.getSession();
        if (session==null){
            Log.d("getFBProfile","session is : null");
            return;
        }
        Log.d("getFBProfile","session is :" + session.toString());
        if (session==null)
        {return;}
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                Log.d("getFBProfile", "UserId:" + user.getId() + ", username: " + user.getName());
                                final Singleton singleton = Singleton.getInstance();
                                final IMipePresenter presenter = singleton.getMipePresenter();

                                try {
                                    usernameForSessionFb = user.getName();
                                    presenter.setUsername(usernameForSessionFb);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(avatarUrlFB==null){
                                    setAvatarFB(presenter, session);
                                }
                                else {
                                    presenter.setAvatar(avatarUrlFB);
                                }




                            }
                        }
                        if (response.getError() != null) {
                            // TODO Handle errors, will do so later.
                            Log.d("GetFBProfile", "Response error: "+response.getError());
                        }
                    }
                });
        request.executeAsync();
    }

    public void setAvatarFB(final IMipePresenter presenter, Session session) {
    /* make the API call */
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new Request(
                session,
                "/me/picture",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {

                        GraphObject graphObject = response.getGraphObject();
                        if (graphObject != null) {

                            //Parse graphObject to User
                            JSONObject jsonObject = graphObject.getInnerJSONObject();

                            try {
                                JSONObject urlAvatar = jsonObject.getJSONObject("data");
                                String url = (String) urlAvatar.get("url");
                                presenter.setAvatar(url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAsync();
    }
}
