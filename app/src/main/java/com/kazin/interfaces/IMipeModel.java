package com.kazin.interfaces;

import android.support.v4.view.PagerAdapter;
import android.widget.Adapter;

/**
 * Created by @gossiks on 04.01.2015.
 */
public interface IMipeModel {
    void onCreateView(PagerAdapter adapter);

    void setUpUserNameImage();

    void onResume();
    void onActionButtonUpload();
}
