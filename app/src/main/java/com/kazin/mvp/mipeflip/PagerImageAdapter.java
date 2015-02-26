package com.kazin.mvp.mipeflip;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kazin.mipe.R;
import com.kazin.mvp.ParseConstants;
import com.kazin.mvp.Singleton;
import com.parse.Parse;
import com.parse.ParseImageView;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Alexey on 02.02.2015.
 */
public class PagerImageAdapter extends PagerAdapter {

    Context context;
    protected List<ParseObject> images;
    protected Singleton singleton = Singleton.getInstance();
    protected Resources res;


    PagerImageAdapter(Context context, List<ParseObject> imagesObjects){
        this.context=context;
        images = imagesObjects;
        renewSingleton();
    }



    @Override
    public int getCount() {
        return images.size()+1; //one extra view for image which represent end of loaded data
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        res = singleton.getRes();
        ParseImageView image = new ParseImageView(context);
        if(position ==(getCount()-1)){
            image.setImageDrawable(res.getDrawable(R.drawable.refresh));
        }
        else {
            ParseObject imageObject = images.get(position);
            image.setPlaceholder(res.getDrawable(R.drawable.still_loading));
            image.setParseFile(imageObject.getParseFile(ParseConstants.KEY_FILE));
            image.loadInBackground();
        }
        container.addView(image, 0);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container.findViewById(R.id.viewPageMipe)).removeView((ImageView) object);
        container.removeView((ImageView)object);
    }


    // Additional stuff

    private void renewSingleton() {
        MipeImage tempMipeImage;
        singleton.setNewMipeArrayL(images.size());
        singleton.setMaxPosition(getCount()-1);
        int i =0;
        for( ParseObject image:images){
            tempMipeImage = new MipeImage(image.getObjectId(), 0);
            singleton.setMipeImageArrayL(i, tempMipeImage);
            i++;
        }
    }


}