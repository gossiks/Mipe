package com.kazin.mvp.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ProgressBar;

import com.kazin.interfaces.IPhotoModel;
import com.kazin.interfaces.IPhotoPresenter;
import com.kazin.interfaces.IPhotoViewer;
import com.kazin.mvp.Singleton;

/**
 * Created by Alexey on 20.01.2015.
 */
public class PhotoPresenter implements IPhotoPresenter{
    Singleton singleton = Singleton.getInstance();
    IPhotoModel model;
    IPhotoViewer viewer;

    @Override
    public void onClickTakePhoto() {
        model = singleton.getPhotoModel();
        model.onClickTakePhoto();
    }

    @Override
    public void onClickChoosePhoto() {
        model = singleton.getPhotoModel();
        model.onClickChoosePhoto();
    }

    @Override
    public void onClickSendPhoto() {
        model = singleton.getPhotoModel();
        model.onClickSendPhoto();
    }

    @Override
    public void setPhotoToSend(Bitmap bitmap) {
        viewer = singleton.getPhotoViewer();
        viewer.setPhotoToSend(bitmap);
    }

    @Override
    public void onActivityResultGetPhoto(Intent intentResult) {
        model  = singleton.getPhotoModel();
        model.onActivityResultGetPhoto(intentResult);
    }

    @Override
    public void onAcivityResultChoosePhoto(Intent intentResult) {
        model = singleton.getPhotoModel();
        model.onActivityResultChoosePhoto(intentResult);
    }

    @Override
    public void alertUploadToastShow(String message) {
        viewer.alertUploadToastShow(message);
    }

    @Override
    public String getHashTags() {
        viewer = singleton.getPhotoViewer();
        return viewer.onGetHashTags();
    }

    @Override
    public void alertDialogOk(String text) {
        viewer = singleton.getPhotoViewer();
        viewer.alertDialogOk(text);
    }

    @Override
    public void setProgressBarVisible(boolean visibility) {
        viewer = singleton.getPhotoViewer();
        viewer.setProgressBarVisibility(visibility);
    }

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        viewer = singleton.getPhotoViewer();
        viewer.setProgressBar(progressBar);
    }
}
