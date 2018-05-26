package etsmtl.gti785.musicplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class ConnectActivity extends AppCompatActivity{


    Button btnConnect;
    Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.btnConnect= (Button) this.findViewById(R.id.btnConnect);
        this.btnSettings= (Button) this.findViewById(R.id.btnSettings);

    }


    public void doConnect(View view) {
        startActivity(new Intent(ConnectActivity.this,MainActivity.class));
    }

    public void doSettings(View view) {

    }

}