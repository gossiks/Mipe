package com.kazin.mipe;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by @gossiks on 11.01.2015.
 */
public class MipeApplication extends Application {

    public static Context CONTEXT;
    public static Resources RESOURCES;

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Enable Crash Reporting
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "GPZJK0q7OGg3MAEtovZIUjbHUeCpi1ySzW5bNaKB", "s6za9gTNYU3SBI2zPeyKawK8ET7ctrOs47fUVTNJ");
        ParseFacebookUtils.initialize("1525120017751386");
        CONTEXT = getApplicationContext();
        RESOURCES = getResources();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.kazin.mipe",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}
