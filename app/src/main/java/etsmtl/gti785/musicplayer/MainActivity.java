package etsmtl.gti785.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;


public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    StreamService streamService;
    Response response;
    Context context;

    TextView textMaxTime, songTitle, textCurrentPosition;
    Button buttonPause, buttonStart, btnRewind, btnPrevious,btnForward, btnNext, btnRepeat,btnShuffle;
    SeekBar seekBar,volBar;

    ArrayList<String> playList = new ArrayList();
    Handler threadHandler = new Handler();
    MediaPlayer mediaPlayer;
    UpdateSeekBarThread updateSeekBarThread;
    String IP,PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        client = new OkHttpClient();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            IP = bundle.getString("IPADRESS");
            PORT = bundle.getString("PORTADRESS");
        }

//        response = new Response();
        // service pour les appels au serveur

        streamService = new StreamService(client, this);

        updateSeekBarThread= new UpdateSeekBarThread();
        this.songTitle = findViewById(R.id.songTitle);
        this.textCurrentPosition = (TextView)this.findViewById(R.id.textView_currentPosition);
        this.textMaxTime=(TextView) this.findViewById(R.id.textView_maxTime);
        this.buttonStart= (Button) this.findViewById(R.id.btnPlay);
        this.buttonPause= (Button) this.findViewById(R.id.btnPause);
        this.buttonPause.setVisibility(View.GONE);
        this.btnForward= (Button) this.findViewById(R.id.btnForward);
        this.btnRewind= (Button) this.findViewById(R.id.btnRewind);
        this.btnNext= (Button) this.findViewById(R.id.btnNext);
        this.btnPrevious= (Button) this.findViewById(R.id.btnPrevious);
        this.btnRepeat= (Button) this.findViewById(R.id.btnRepeat);
        this.btnShuffle= (Button) this.findViewById(R.id.btnShuffle);
        this.btnShuffle.setEnabled(false);

        // Progress Bar
        this.seekBar= (SeekBar) this.findViewById(R.id.songProgressBar);
        this.seekBar.setClickable(true);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            seekBar.setProgress(progress);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );
        // Volume Bar
        volBar = (SeekBar) findViewById(R.id.volumeBar);
        this.volBar.setClickable(true);
        volBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );
        this.playList = listElemRaw();

        // ici le player est dans l'éetat prepared apres la methode create
        // on get la premiere chanson par defaut
        int resID = getResources().getIdentifier(this.playList.get(0), "raw", getPackageName());
        this.mediaPlayer =  MediaPlayer.create(this, resID);
        int duration = this.mediaPlayer.getDuration();
        this.seekBar.setMax(duration);
        String maxTimeString = this.createTimeLabel(duration);
        this.textCurrentPosition.setText("0:00");
        this.textMaxTime.setText(maxTimeString);
        this.mediaPlayer.seekTo(0);
        this.mediaPlayer.setLooping(false); // par défaut
        this.mediaPlayer.setVolume(0.5f, 0.5f);
    }

    // Thread to Update position for SeekBar.
    class UpdateSeekBarThread implements Runnable {
        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = createTimeLabel(currentPosition);
            textCurrentPosition.setText(currentPositionStr);
            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }

    public void doShuffle(View view) {
        this.updateSeekBarThread= new UpdateSeekBarThread();
        this.threadHandler.postDelayed(updateSeekBarThread,50);
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();
        String randSongID = playList.get(new Random().nextInt(playList.size()));
        int resID = getResources().getIdentifier(randSongID, "raw", getPackageName());
        this.mediaPlayer =  MediaPlayer.create(this, resID);
        int duration = this.mediaPlayer.getDuration();
        this.seekBar.setMax(duration);
        String maxTimeString = this.createTimeLabel(duration);
        this.textMaxTime.setText(maxTimeString);
        this.textCurrentPosition.setText("0:00");
        this.seekBar.setProgress(0);
        this.songTitle.setText("Song title : " + randSongID);
        this.mediaPlayer.start();
    }

    // Fonction executer sur btn PLAY
    public void doStart(View view)  {
        this.mediaPlayer.start();
        threadHandler.postDelayed(updateSeekBarThread,50);
        this.btnShuffle.setEnabled(true);
        this.songTitle.setText("Song title : " + playList.get(0));
        this.buttonStart.setVisibility(View.GONE);
        this.buttonPause.setVisibility(View.VISIBLE);
    }


    // retourne un path, si on envoie getPathWithSondId(songId)
    private String getPathWithSondId(String rand){
        String output = this.getResources().getResourceName(Integer.valueOf((rand)));
        return output;
    }

    // Find ID of resource in 'raw' folder.
    public int getRawResIdByName(String resName)  {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }
    // pour remplacer la fonction douteuse originale
    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

    public void doRepeat(View view)  {
        if(mediaPlayer.isLooping() == true){
            mediaPlayer.setLooping(false);
            btnRepeat.setBackgroundResource(R.drawable.repeat);
        }else{
            mediaPlayer.setLooping(true);
            btnRepeat.setBackgroundResource(R.drawable.repeat2);
        }
    }

    // When user click to "Pause".
    public void doPause(View view)  {
        this.mediaPlayer.pause();
        this.btnShuffle.setEnabled(false);
        this.buttonStart.setVisibility(View.VISIBLE);
        this.buttonPause.setVisibility(View.GONE);
    }

    // When user click to "Rewind".
    public void doRewind(View view)  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int SUBTRACT_TIME = 5000;

        if(currentPosition - SUBTRACT_TIME > 0 )  {
            this.mediaPlayer.seekTo(currentPosition - SUBTRACT_TIME);
        }
    }

    // When user click to "Fast-Forward".
    public void doFastForward(View view)  {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
        // 5 seconds.
        int ADD_TIME = 5000;

        if(currentPosition + ADD_TIME < duration)  {
            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
        }
    }

    // When user click to "Next".
    public void doNext(View view) throws IOException {
        streamService.getNextSong();
    }


    // When user click to "Previous".
    public void doPrevious(View view)  {
       // streamService.getPreviousSong();
    }

    // When user click to "Previous".
    public void doReturn(View view)  {
        Intent i = new Intent(this, ConnectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("IPADRESS", IP);
        bundle.putString("PORTADRESS", PORT);
        i.putExtras(bundle);
        startActivity(i);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        text = "DISCONNECTED FROM SERVER";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public ArrayList<String> listElemRaw(){
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            Log.d("Raw Asset: ", fields[count].getName());
            String field = fields[count].getName();
            playList.add(field);
        }
        return playList;
    }
}