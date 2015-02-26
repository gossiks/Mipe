package com.kazin.mipe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.kazin.interfaces.IMipePresenter;
import com.kazin.interfaces.IPhotoPresenter;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alexey on 11.02.2015.
 */
public class ChangeAvatarActivity extends Activity{
    static final int REQUEST_IMAGE_CHOOSE = 2;
    Singleton singleton = Singleton.getInstance();
    IMipePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("changeAvatarActivity", "Started");
        choosePictureIntent();
    }

    public void choosePictureIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        this.startActivityForResult(photoPickerIntent, REQUEST_IMAGE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ChangeAvatarAct","Result code: "+resultCode+ "Request code is: "+requestCode);
        if (requestCode == REQUEST_IMAGE_CHOOSE && resultCode == RESULT_OK) {
            setAvatar(data);
        }
        Intent intent = new Intent(this, MainActivityMipe.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setAvatar(Intent data){
        Uri imageUri = data.getData();
        ContentResolver contentResolver = singleton.getConxt().getContentResolver();
        Bitmap imageBitmap = null;
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File newPhoto = FileHelper.createFile();
        FileHelper.writeTofile(imageBitmap, newPhoto);

        byte[] byteData = null;
        try {
            byteData = IOUtils.toByteArray(new FileInputStream(newPhoto));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ParseObject currentUser = ParseUser.getCurrentUser();
        byteData = FileHelper.reduceImageForUpload(byteData);
        final ParseFile parseFilePhoto = new ParseFile("avatar.jpg",byteData);
        parseFilePhoto.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) { //TODO redo more clear
                currentUser.put(ParseConstants.KEY_AVATAR, parseFilePhoto);
                presenter = singleton.getMipePresenter();
                presenter.alertUploadToastShow(getString(R.string.alert_avatar_image_changed));
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        try {
                            String avatarUrl = ParseUser.getCurrentUser().getParseFile(ParseConstants.KEY_AVATAR).getUrl();
                            presenter.setAvatar(avatarUrl);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
