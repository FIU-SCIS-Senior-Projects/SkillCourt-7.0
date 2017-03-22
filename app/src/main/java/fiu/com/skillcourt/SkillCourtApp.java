package fiu.com.skillcourt;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by pedrocarrillo on 10/17/16.
 */

public class SkillCourtApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }


    
}
