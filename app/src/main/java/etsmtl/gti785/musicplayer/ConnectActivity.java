package etsmtl.gti785.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConnectActivity extends AppCompatActivity{

    Button btnConnect, btnSettings;
    String IP,PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.btnConnect= (Button) this.findViewById(R.id.btnConnect);
        this.btnSettings= (Button) this.findViewById(R.id.btnSettings);

        this.btnConnect.setText("Connect");
        this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.connect, 0, 0, 0);
        this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtngreen));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("IPADRESS") != null && bundle.getString("PORTADRESS") != null){
                IP = bundle.getString("IPADRESS");
                PORT = bundle.getString("PORTADRESS");
            }
        }
        else{
            IP = "192.168.43.75";
            PORT = "8765";
        }
    }


    public void doConnect(View view) {
            this.btnConnect.setText("Disconnect");
            this.btnConnect.setBackgroundColor(getResources().getColor(R.color.colorbtnred));
            this.btnConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.disconnect, 0, 0, 0);
            Intent i = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("IPADRESS", IP);
            bundle.putString("PORTADRESS", PORT);
            i.putExtras(bundle);
            startActivity(i);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        text = "CONNECTION ESTABLISHED";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void doSettings(View view) {
        Intent serverActivity = new Intent(this,ServerConfigActivity.class);
        if(IP != "" && IP != null && PORT != "" && PORT != null){
            Bundle bundle = new Bundle();
            bundle.putString("IPADRESS", IP);
            bundle.putString("PORTADRESS", PORT);
            serverActivity.putExtras(bundle);
        }
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