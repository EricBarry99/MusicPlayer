package etsmtl.gti785.musicplayer;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ServerConfigActivity extends AppCompatActivity {

    EditText ipbox;
    EditText portbox;
    Button btnCancel;
    Button btnApply;

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
        Intent masterActivity = new Intent(this,ConnectActivity.class);
        startActivity(masterActivity);
    }

    public void doApply(View view) {


        //Intent masterActivity = new Intent(this,ServerConfigActivity.class);
        //startActivity(masterActivity);
    }
}