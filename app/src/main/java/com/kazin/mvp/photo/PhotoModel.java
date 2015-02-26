package com.kazin.mvp.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.kazin.interfaces.IPhotoModel;
import com.kazin.interfaces.IPhotoPresenter;
import com.kazin.mipe.R;
import com.kazin.mvp.ParseConstants;
import com.kazin.mvp.Singleton;
import com.kazin.mvp.mipeflip.FileHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 20.01.2015.
 */
public class PhotoModel implements IPhotoModel{

    Singleton singleton = Singleton.getInstance();
    IPhotoPresenter presenter;
    static final int REQUEST_IMAGE_CAPTURE = 1; //TODO place it in singleton
    static final int REQUEST_IMAGE_CHOOSE = 2;

    @Override
    public void onClickTakePhoto() {
        takePictureIntent();
    }

    @Override
    public void onClickChoosePhoto() {
        choosePictureIntent();
    }


    @Override
    public void onClickSendPhoto() {
        presenter = singleton.getPhotoPresenter();
        String hashTagsRaw = presenter.getHashTags();
        hashTagsRaw = hashTagsRaw.replace(" ","");
        String[] hashTags = hashTagsRaw.split("#");
        List<String> hashTagsArrayList = removeBadValues(hashTags);
        Log.d("onClickSendPhoto", "hashtags: " +hashTags.toString() );
        if (hashTagsArrayList.size()==0){
            String text = singleton.getConxt().getString(R.string.alert_enter_some_hashtags);
            presenter.alertDialogOk(text);
            return;
        }
        if(singleton.getPhotoSend()==null){
            return;
        }
        if(hasTooLongHashtag(hashTagsArrayList)){
            String text = singleton.getConxt().getString(R.string.alert_hashtags_must_be_less_than);
            presenter.alertDialogOk(text);
            return;
        }

        presenter.setProgressBarVisible(true);
        byte[] byteData = null;
        try {
            byteData =IOUtils.toByteArray(new FileInputStream(singleton.getPhotoSend()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ParseObject sendPhoto = new ParseObject(ParseConstants.CLASS_IMAGES);
        sendPhoto.put(ParseConstants.KEY_CREATED_BY, ParseUser.getCurrentUser().getObjectId());
        sendPhoto.put(ParseConstants.KEY_HASHTAGS, hashTagsArrayList);
        byteData = FileHelper.reduceImageForUpload(byteData);
        final ParseFile parseFilePhoto = new ParseFile("photo.jpg",byteData);
        parseFilePhoto.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                sendPhoto.put(ParseConstants.KEY_FILE, parseFilePhoto);
                sendPhoto.saveEventually();
                presenter = singleton.getPhotoPresenter();
                presenter.alertUploadToastShow(singleton.getConxt().getString(R.string.alert_image_uploaded));
                presenter.setProgressBarVisible(false);
                presenter.setPhotoToSend(BitmapFactory.decodeResource(singleton.getRes(),
                        R.drawable.no_image_yet)); //TODO redo without unnecessary converting drawable to bitmap
            }
        });

    }

    private boolean hasTooLongHashtag(List<String> hashtags) {
        for(String hashtag:hashtags){
            if (hashtag.length()>13){
                return true;
            }
        }
        return false;
    }

    private List<String> removeBadValues(String[] firstArray) {
        List<String> list = new ArrayList<String>();
        for(String s : firstArray) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        return  list;
    }

    @Override
    public void onActivityResultGetPhoto(Intent intentResult) {
        Bitmap imageBitmap;
        File file  = singleton.getPhotoSend();
        imageBitmap = FileHelper.getPicResized(singleton.getImageViewSend(),file.getPath());
        presenter = singleton.getPhotoPresenter();
        presenter.setPhotoToSend(imageBitmap);
    }

    @Override
    public void onActivityResultChoosePhoto(Intent intentResult) {
        Uri imageUri = intentResult.getData();
        ContentResolver contentResolver = singleton.getConxt().getContentResolver();
        Bitmap imageBitmap = null;
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File newPhoto = FileHelper.createFile();
        FileHelper.writeTofile(imageBitmap, newPhoto);
        singleton.setPhotoSend(newPhoto);
        ImageView imageViewSend = singleton.getImageViewSend();
        imageBitmap = FileHelper.getPicResized(imageViewSend,newPhoto.getPath());
        presenter = singleton.getPhotoPresenter();
        presenter.setPhotoToSend(imageBitmap);
    }


    public void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Activity mainActivity = singleton.getMainActivity();
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = FileHelper.createFile();

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                singleton.setPhotoSend(photoFile);
                mainActivity.startActivityForResult(takePictureIntent, singleton.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void choosePictureIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        Activity mainActivity = singleton.getMainActivity();
        mainActivity.startActivityForResult(photoPickerIntent, REQUEST_IMAGE_CHOOSE);
    }

}
