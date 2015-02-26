package com.kazin.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.kazin.interfaces.IMipeModel;
import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IMipeViewer;
import com.kazin.interfaces.IPhotoModel;
import com.kazin.interfaces.IPhotoPresenter;
import com.kazin.interfaces.IPhotoViewer;
import com.kazin.interfaces.IStatsModel;
import com.kazin.interfaces.IStatsPresenter;
import com.kazin.interfaces.IStatsViewer;
import com.kazin.mipe.MainActivityMipe;
import com.kazin.mipe.MipeApplication;
import com.kazin.mvp.mipeflip.MipeImage;
import com.kazin.mvp.mipeflip.MipeModel;
import com.kazin.mvp.mipeflip.MipePresenter;
import com.kazin.mvp.mipeflip.MipeViewer;
import com.kazin.mvp.photo.PhotoModel;
import com.kazin.mvp.photo.PhotoPresenter;
import com.kazin.mvp.photo.PhotoViewer;
import com.kazin.mvp.stats.StatsModel;
import com.kazin.mvp.stats.StatsPresenter;
import com.kazin.mvp.stats.StatsViewer;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by @gossiks on 05.01.2015.
 */
public class Singleton {
    public static Singleton instance;
    private IMipeViewer mipeViewer;
    private IMipePresenter mipePresenter;
    private IMipeModel mipeModel;
    private IPhotoViewer photoViewer;
    private IPhotoPresenter photoPresenter;
    private IPhotoModel photoModel;
    private IStatsViewer statsViewer;
    private IStatsModel statsModel;
    private IStatsPresenter statsPresenter;


    private View rootView;
    private ViewPager flipView;
    private ImageView imageViewSend;
    private EditText editText;
    private Activity mainActivity;
    private GraphView statsGraph;
    private File photoSend;
    private ArrayList<MipeImage> MipeImageArrayL;
    private long startTime;
    private int oldPosition;
    private int maxPosition;
    private int numberOfStatsToDisplay = 5;
    private ProgressBar progressBarOverall;
    private ProgressBar progressBarCurrent;
    private TextView progressBarText;
    private ProgressBar progressBarPhoto;
    private CircleImageView avatarView;
    private TextView usernameText;

    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int REQUEST_IMAGE_CHOOSE = 2;

    private Singleton(){
    }


    public static Singleton getInstance(){
        if (instance ==null){
            instance = new Singleton();
        }
        return instance;
    }

    public static Singleton resetInstance(){
        instance = new Singleton();
        return instance;
    }

    public ProgressBar getProgressBarOverall() {
        return progressBarOverall;
    }

    public void setProgressBarOverall(ProgressBar progressBarOverallinput) {
        progressBarOverall = progressBarOverallinput;
    }

    public ProgressBar getProgressBarCurrent() {
        return progressBarCurrent;
    }

    public void setProgressBarCurrent(ProgressBar progressBarCurrentInput) {
        progressBarCurrent = progressBarCurrentInput;
    }

    public TextView getProgressBarText() {
        return progressBarText;
    }

    public void setProgressBarText(TextView progressBarTextInput) {
        progressBarText = progressBarTextInput;
    }


    public IMipeViewer getMipeViewer(){
        if(mipeViewer == null){
            mipeViewer = new MipeViewer();
        }
        return mipeViewer;
    }

    public IMipePresenter getMipePresenter(){
        if(mipePresenter ==null){
            mipePresenter = new MipePresenter();
        }
        return mipePresenter;
    }

    public IMipeModel getMipeModel(){
        if (mipeModel ==null){
            mipeModel = new MipeModel();
        }
        return mipeModel;
    }

    public IPhotoViewer getPhotoViewer(){
        if(photoViewer == null){
            photoViewer = new PhotoViewer();
        }
        return photoViewer;
    }

    public IPhotoPresenter getPhotoPresenter(){
        if(photoPresenter ==null){
            photoPresenter = new PhotoPresenter();
        }
        return photoPresenter;
    }

    public IPhotoModel getPhotoModel(){
        if (photoModel ==null){
            photoModel = new PhotoModel();
        }
        return photoModel;
    }

    public IStatsViewer getStatsViewer(){
        if(statsViewer == null){
            statsViewer = new StatsViewer();
        }
        return statsViewer;
    }

    public IStatsPresenter getStatsPresenter(){
        if(statsPresenter ==null){
            statsPresenter = new StatsPresenter();
        }
        return statsPresenter;
    }

    public IStatsModel getStatsModel(){
        if (statsModel ==null){
            statsModel = new StatsModel();
        }
        return statsModel;
    }

    public Resources getRes(){
        return MipeApplication.RESOURCES;
    }

    public Context getConxt(){
        return MipeApplication.CONTEXT;
    }

    public View getRootView(){
        if(rootView ==null){
            rootView = MainActivityMipe.ROOTVIEW;
        }
        return rootView;
    }

    public void setNewMipeArrayL(int length){
        MipeImageArrayL = new ArrayList<MipeImage>(); //why this doesnt work throws "new ArrayList<...>(length)
        MipeImage tempEmptyMipeImage = new MipeImage();
        for (int i = 0 ;i<length; i++){
            MipeImageArrayL.add(i,tempEmptyMipeImage);
        }
    }

    public void setMipeImageArrayL(int position, MipeImage mipeImage){
        MipeImageArrayL.set(position, mipeImage);
    }
    public MipeImage getMipeImage(int position){
        return MipeImageArrayL.get(position);
    }
    public ArrayList<MipeImage> getMipeImageArrayL(){
        return MipeImageArrayL;
    }

    public void plusMipeImageTime(int position, long time){
        MipeImage mipeImage =  getMipeImage(position);
        mipeImage.plusImageTime(time);
        setMipeImageArrayL(position, mipeImage);
    }

    public void setStartTime(){
        startTime = System.currentTimeMillis();
    }
    public long getStartTime(){
        return startTime;
    }
    public void setOldPosition(int position){
        oldPosition = position;
    }
    public int getOldPosition(){
        return oldPosition;
    }
    public void setMaxPosition(int position){
        maxPosition = position;
    }
    public int getMaxPosition(){
        return maxPosition;
    }
    public void setImageViewSend(ImageView imageViewSend){
        this.imageViewSend = imageViewSend;
    }
    public ImageView getImageViewSend(){
        return imageViewSend;
    }
    public void setEditText(EditText editText){
        this.editText = editText;
    }
    public EditText getEditText(){
        return editText;
    }
    public void setMainActivity(Activity activity){
        mainActivity = activity;
    }
    public Activity getMainActivity(){
        return mainActivity;
    }
    public void setPhotoSend(File file){
        photoSend = file;
    }
    public File getPhotoSend(){
        return photoSend;
    }
    public void setStatsGraph(GraphView graphView){
        statsGraph = graphView;
    }
    public GraphView getStatsGraph(){
        return statsGraph;
    }

    public int getNumberOfStatsToDisplay(){
        return numberOfStatsToDisplay;
    }


    public ProgressBar getProgressBarPhoto(){
        return progressBarPhoto;
    }

    public void setAvatarView(CircleImageView avatarView) {
        this.avatarView = avatarView;
    }

    public CircleImageView getAvatarView() {
        return avatarView;
    }

    public void setUsernameText(TextView usernameText) {
        this.usernameText = usernameText;
    }

    public TextView getUsernameText() {
        return usernameText;
    }
}
