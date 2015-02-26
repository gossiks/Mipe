package com.kazin.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ProgressBar;

/**
 * Created by Alexey on 20.01.2015.
 */
public interface IPhotoPresenter {
    void onClickTakePhoto();
    void onClickChoosePhoto();
    void onClickSendPhoto();
    void setPhotoToSend(Bitmap bitmap);
    void onActivityResultGetPhoto(Intent intentResult);
    void onAcivityResultChoosePhoto(Intent intentResult);
    void alertUploadToastShow(String message);
    String getHashTags();

    void alertDialogOk(String text);

    void setProgressBarVisible(boolean visibility);

    void setProgressBar(ProgressBar progressBar);
}
