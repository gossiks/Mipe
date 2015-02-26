package com.kazin.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.parse.ParseObject;

import java.util.List;


/**
 * Created by @gossiks on 04.01.2015.
 */
public interface IMipeViewer {
    void onCreateView(ViewPager pager);
    void onResume();
    void setAdapter(PagerAdapter imageFlipAdapter);

    void setMipeListener(ViewPager.OnPageChangeListener onFlipListener);
    void alertUploadToastShow(String message);
    void onActionButtonUpload();

    void setLoadingProcess(Integer percent, int currentFetchingImage, int overallImages);

    void setProgressBarsVisibility(boolean visibility);

    void setPagerMipeVisible(boolean visibility);

    void setUsername(String username);

    void setAvatar(String url);

    void setPager(ViewPager pager);

    ViewPager getPager();

    void onPause();

    void setCurrentPagerItem(int position);
}
