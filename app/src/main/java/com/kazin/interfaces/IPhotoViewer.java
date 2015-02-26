package com.kazin.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ProgressBar;

/**
 * Created by Alexey on 20.01.2015.
 */
public interface IPhotoViewer  {
    void onClickTakePhoto();
    void onClickChoosePhoto();
    void onClickSendPhoto();
    String onGetHashTags();
    void alertUploadToastShow(String message);
    void setPhotoToSend(Bitmap bitmap);

    void alertDialogOk(String text);

    void setProgressBarVisibility(boolean visibility);

    void setProgressBar(ProgressBar progressBar);
}
