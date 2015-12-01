package be.maartenvg.smartalarm;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Maarten on 30/11/2015.
 */
public class SmartAlarmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "A3WoyobOV1mCpn9yJdF9k47mnTrjnfjJhkLgLXzm", "n2MKgl9KrPyBc8VuXXCjtglk7HYJwLFcLIijP4Ih");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
