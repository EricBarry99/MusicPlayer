package etsmtl.gti785.musicplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerConfigActivity extends AppCompatActivity {

    EditText ipbox, portbox;
    Button btnCancel, btnApply;

    String MASTERIP, MASTERPORT;

    private Pattern pattern, pattern2;
    private Matcher matcher, matcher2;



    //Source : https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final String PORTADDRESS_PATTERN = "^[0-9]{4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        this.ipbox = (EditText) this.findViewById(R.id.ipbox);
        this.portbox = (EditText) this.findViewById(R.id.portbox);
        this.btnCancel = (Button) this.findViewById(R.id.btnBack);
        this.btnApply = (Button) this.findViewById(R.id.btnApply);

        pattern = Pattern.compile(IPADDRESS_PATTERN);
        pattern2 = Pattern.compile(PORTADDRESS_PATTERN);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            MASTERIP = bundle.getString("IPADRESS");
            MASTERPORT = bundle.getString("PORTADRESS");
            ipbox.setText(MASTERIP);
            portbox.setText(MASTERPORT);
        }
    }

    public void doCancel(View view) {
        Intent ConnectActivity = new Intent(this, ConnectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("IPADRESS", MASTERIP);
        bundle.putString("PORTADRESS", MASTERPORT);
        ConnectActivity.putExtras(bundle);
        startActivity(ConnectActivity);
    }

    public void doApply(View view) {

        MASTERIP = String.valueOf(ipbox.getText());
        MASTERPORT = String.valueOf(portbox.getText());
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;

        if (!MASTERIP.isEmpty() && !MASTERPORT.isEmpty()) {
            if (ipValidator(MASTERIP) && portValidator(MASTERPORT)) {
                text = "ALL GOOD, READY TO CONNECT";
                Intent ConnectActivity = new Intent(this,ConnectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("IPADRESS", MASTERIP);
                bundle.putString("PORTADRESS", MASTERPORT);
                ConnectActivity.putExtras(bundle);
                startActivity(ConnectActivity);
            }else if(!ipValidator(MASTERIP)){
                text = "INVALID IP ADRESS!";
            } else if(!portValidator(MASTERPORT)){
                text = "INVALID PORT ADRESS!";
            }else {
                text = "WTF srly.";
            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private boolean ipValidator(String ip) {
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private boolean portValidator(String port) {
        matcher2 = pattern2.matcher(port);
        return matcher2.matches();
    }
}