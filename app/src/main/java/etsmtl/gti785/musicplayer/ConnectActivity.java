package etsmtl.gti785.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ConnectActivity extends AppCompatActivity{

    Button btnConnect, btnSettings;

    String AppStatus;

    public boolean AppConnectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.btnConnect= (Button) this.findViewById(R.id.btnConnect);
        this.btnSettings= (Button) this.findViewById(R.id.btnSettings);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
         AppStatus = bundle.getString("APPSTATUS");
           if(AppStatus == "true"){
               AppConnectionStatus= false;
               this.btnConnect.setText("Connect");
               this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.connect, 0, 0, 0);
               this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtngreen));

           }else{
               AppConnectionStatus= true;
               this.btnConnect.setText("Disconnect");
               this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtnred));
               this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disconnect, 0, 0, 0);
           }
        }
    }


    public void doConnect(View view) {
        // lance la connexion au server
        Log.d("AppStatus: ", String.valueOf(AppConnectionStatus));
        if(AppConnectionStatus){
            AppConnectionStatus = false;
            this.btnConnect.setText("Connect");
            this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.connect, 0, 0, 0);
            this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtngreen));
        }else{
            AppConnectionStatus = true;
            this.btnConnect.setText("Disconnect");
            this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtnred));
            this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disconnect, 0, 0, 0);

            Intent i = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("APPSTATUS", String.valueOf(AppConnectionStatus));
            //bundle.putString("TEST", "testvalue");
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    public void doSettings(View view) {
        Intent serverActivity = new Intent(this,ServerConfigActivity.class);
        startActivity(serverActivity);
    }


    // Find ID of resource in 'raw' folder.
    public int getRawResIdByName(String resName)  {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }
}