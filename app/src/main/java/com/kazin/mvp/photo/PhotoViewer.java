package com.kazin.mvp.photo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kazin.interfaces.IPhotoPresenter;
import com.kazin.interfaces.IPhotoViewer;
import com.kazin.mvp.Singleton;

/**
 * Created by Alexey on 20.01.2015.
 */
public class PhotoViewer implements IPhotoViewer{
    Singleton singleton = Singleton.getInstance();
    IPhotoPresenter presenter;

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    ProgressBar progressBar;

    @Override
    public void onClickTakePhoto() {
        presenter = singleton.getPhotoPresenter();
        presenter.onClickTakePhoto();
    }

    @Override
    public void onClickChoosePhoto() {
        presenter = singleton.getPhotoPresenter();
        presenter.onClickChoosePhoto();
    }

    @Override
    public void onClickSendPhoto() {
        presenter = singleton.getPhotoPresenter();
        presenter.onClickSendPhoto();
    }

    @Override
    public String onGetHashTags() {
        EditText editText = singleton.getEditText();
        return editText.getText().toString();
    }

    @Override
    public void alertUploadToastShow(String message) {
        Context context  = singleton.getConxt();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }

    @Override
    public void setPhotoToSend(Bitmap bitmap) {
        ImageView imageSend = singleton.getImageViewSend();
        imageSend.setImageBitmap(bitmap);
    }

    @Override
    public void alertDialogOk(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(singleton.getMainActivity());
        builder.setMessage(text)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setProgressBarVisibility(boolean visibility) {

        int vis;
        if (visibility == true){
            vis = View.VISIBLE;
        }
        else{
            vis = View.INVISIBLE;
        }
        progressBar.setVisibility(vis);
    }

}
