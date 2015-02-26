package com.kazin.mvp.mipeflip;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IMipeViewer;
import com.kazin.mvp.Singleton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by @gossiks on 11.01.2015.
 */
public class MipeViewer implements IMipeViewer {

    private Singleton  singleton = Singleton.getInstance();
    private IMipePresenter presenter;
    private ViewPager pager;
    private int currentPagerItem = 0;


    @Override
    public void onCreateView(ViewPager pager) {
        presenter = singleton.getMipePresenter();
        presenter.onCreateView(pager);
    }


    @Override
    public void onResume() {
        presenter = singleton.getMipePresenter();
        presenter.onResume();
    }

    @Override
    public void setAdapter(PagerAdapter imageFlipAdapter) {
        pager.setAdapter(imageFlipAdapter);
        pager.setCurrentItem(currentPagerItem);
    }

    @Override
    public void setMipeListener(ViewPager.OnPageChangeListener onFlipListener) {
        pager.setOnPageChangeListener(onFlipListener);
    }



    @Override
    public void alertUploadToastShow(String message) {
        Context context  = singleton.getConxt();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    @Override
    public void onActionButtonUpload() {
        presenter = singleton.getMipePresenter();
        presenter.onActionButtonUpload();
    }

    @Override
    public void setLoadingProcess(Integer percent, int currentFetchingImage, int overallImages) {
        singleton.getProgressBarOverall().setProgress(percent);
        singleton.getProgressBarCurrent().setProgress(currentFetchingImage);
    }

    @Override
    public void setProgressBarsVisibility(boolean visibility) {
        int vis;
        if (visibility == true) {
            vis = View.VISIBLE;
        }
        else {
            vis = View.INVISIBLE;
        }
        singleton.getProgressBarOverall().setVisibility(vis);
        singleton.getProgressBarCurrent().setVisibility(vis);
        singleton.getProgressBarText().setVisibility(vis);
    }

    @Override
    public void setPagerMipeVisible(boolean visibility) {
        int vis;
        if (visibility == true) {
            vis = View.VISIBLE;
        }
        else {
            vis = View.INVISIBLE;
        }
        pager.setVisibility(vis);
    }

    @Override
    public void setUsername(String username) {
        TextView usernameText = singleton.getUsernameText(); //TODO place on viewer
        usernameText.setText(username);
    }

    @Override
    public void setAvatar(String url) {
        CircleImageView avatar = singleton.getAvatarView();
        Picasso.with(singleton.getConxt()).load(url).into(avatar);
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public ViewPager getPager() {
        return pager;
    }

    @Override
    public void onPause() {
        currentPagerItem = pager.getCurrentItem();
    }

    @Override
    public void setCurrentPagerItem(int position) {
        currentPagerItem = position;
    }


}
