package com.kazin.mvp.mipeflip;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.kazin.interfaces.IMipeModel;
import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IMipeViewer;
import com.kazin.mvp.Singleton;


/**
 * Created by @gossiks on 11.01.2015.
 */
public class MipePresenter implements IMipePresenter{

    Singleton singleton = Singleton.getInstance();
    IMipeModel model;
    IMipeViewer viewer;



    @Override
    public void onCreateView(ViewPager pager) {
        model = singleton.getMipeModel();
        viewer = singleton.getMipeViewer();
        PagerAdapter adapter = pager.getAdapter();
        viewer.setPager(pager); //TODO place this in viewer
        model.onCreateView(adapter);
    }

    @Override
    public void onResume() {
        model = singleton.getMipeModel();
        model.onResume();
    }

    @Override
    public void setMipeAdapter(PagerAdapter mipeAdapter) {
        viewer = singleton.getMipeViewer();
        viewer.setAdapter(mipeAdapter);
    }

    @Override
    public void setMipeListener(ViewPager.OnPageChangeListener onFlipListener) {
        viewer = singleton.getMipeViewer();
        viewer.setMipeListener(onFlipListener);
    }



    @Override
    public void alertUploadToastShow(String message) {
        viewer.alertUploadToastShow(message);
    }

    @Override
    public void onActionButtonUpload() {
        model =  singleton.getMipeModel();
        model.onActionButtonUpload();
    }

    @Override
    public void setLoadingProcess(Integer percent, int currentFetchingImage, int overallImages) {
        viewer = singleton.getMipeViewer();
        viewer.setLoadingProcess(percent, currentFetchingImage,overallImages);
    }

    @Override
    public void setProgressBarsVisible(boolean visibility) {
        viewer = singleton.getMipeViewer();
        viewer.setProgressBarsVisibility(visibility);
    }

    @Override
    public void setPagerMipeVisible(boolean visibility) {
        viewer = singleton.getMipeViewer();
        viewer.setPagerMipeVisible(visibility);
    }

    @Override
    public void setUsername(String username) {
        viewer = singleton.getMipeViewer();
        viewer.setUsername(username);
    }

    @Override
    public void setAvatar(String url) {
        viewer =singleton.getMipeViewer();
        viewer.setAvatar(url);
    }

    @Override
    public void setUpUserNameImage() {
        model=singleton.getMipeModel();
        model.setUpUserNameImage();
    }

    @Override
    public void setPagerCurrentItem(int position) {
        viewer = singleton.getMipeViewer();
        viewer.setCurrentPagerItem(position);
    }


}
