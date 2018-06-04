package etsmtl.gti785.musicplayer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

// SOURCE :
// http://devdeeds.com/how-to-create-a-5-seconds-splash-screen-in-android/
// https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154


public class SplashScreen extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
       // startActivity(new Intent(SplashScreen.this, ConnectActivity.class));

        // close splash activity
      //  finish();

        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //The following code will execute after the 5 seconds.

                try {

                    //Go to next page i.e, start the next activity.
                    Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
                    startActivity(intent);

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, 3000);  // Give a 5 seconds delay.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
