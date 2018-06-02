package etsmtl.gti785.musicplayer;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ServerConfigActivity extends AppCompatActivity {

    EditText ipbox, portbox;
    Button btnCancel, btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        this.ipbox= (EditText) this.findViewById(R.id.ipbox);
        this.portbox= (EditText) this.findViewById(R.id.portbox);
        this.btnCancel= (Button) this.findViewById(R.id.btnBack);
        this.btnApply= (Button) this.findViewById(R.id.btnApply);

    }

    public void doCancel(View view) {
        Intent ConnectActivity = new Intent(this,ConnectActivity.class);
        startActivity(ConnectActivity);
    }

    public void doApply(View view) {
      //  String ip = String.valueOf(ipbox.getText());
       // String port = String.valueOf(portbox.getText());

       // Intent ConnectActivity = new Intent(this,ConnectActivity.class);
       // startActivity(ConnectActivity);
    }
}