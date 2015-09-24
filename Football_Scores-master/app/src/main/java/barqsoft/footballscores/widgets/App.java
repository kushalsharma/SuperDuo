package barqsoft.footballscores.widgets;

import android.app.Application;
import android.content.Context;

/**
 * Created by Kush on 9/24/2015.
 */
public class App extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}