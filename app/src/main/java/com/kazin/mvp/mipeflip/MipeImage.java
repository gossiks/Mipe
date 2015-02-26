package com.kazin.mvp.mipeflip;

import android.graphics.drawable.Drawable;

/**
 * Created by Alexey on 12.01.2015.
 */
public class MipeImage {
    private String imageId;
    private long imageTime;

    public MipeImage(){
    }

    public MipeImage(String imageId, long imageTime){
        this.imageId = imageId;
        this.imageTime = imageTime;
    }

    public String getId(){
        return imageId;
    }
    public long getImageTime(){
        return imageTime;
    }

    public void setImageTime(long imageTime){
        this.imageTime = imageTime;
    }

    public void plusImageTime(long imageTime){
        this.imageTime = this.imageTime + imageTime;
    }


}
