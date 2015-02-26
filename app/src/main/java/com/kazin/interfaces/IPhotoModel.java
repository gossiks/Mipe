package com.kazin.interfaces;

import android.content.Intent;

/**
 * Created by Alexey on 20.01.2015.
 */
public interface IPhotoModel {
    void onClickTakePhoto();
    void onClickChoosePhoto();
    void onClickSendPhoto();

    void onActivityResultGetPhoto(Intent intentResult);
    void onActivityResultChoosePhoto(Intent intentResult);
}
