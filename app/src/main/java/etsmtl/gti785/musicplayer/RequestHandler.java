package etsmtl.gti785.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import bean.Song;

public class RequestHandler extends AsyncTask<String, Void, Song> {

    OkHttpClient client;
    MainActivity mainActivity;

    public RequestHandler(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    @Override
    // 0 request, 1 address, 2 operation
    protected Song doInBackground(String... objects) {

        Response response = null;

        String operation = objects[0].toString();
        String address = objects[1].toString();
        String currentSong = objects[2].toString();
        Request request = createRequest(operation, address, currentSong);

        System.out.println("CURRENT SONG IN REQUEST: " + currentSong);

        try {
            response = client.newCall(request).execute();
            if(response != null && response.isSuccessful()){

                Song song = extractSong(response);

                if(song != null){
                    if(operation.equals("initPlayer")){
                        initPlayer(address, song);
                    }
                    else if (operation.equals("nextSong")){
                        setNextSong();
                    }
                    else if (operation.equals("previousSong")){
                        setPreviousSong();
                    }
                    else if (operation.equals("shuffleSong")){
                        shuffleNextSong();
                    }
                    else{
                        Toast.makeText(mainActivity.getBaseContext(),"Song is null",Toast.LENGTH_SHORT).show();
                    }
                    return song;
                }
                else{
                    Toast.makeText(mainActivity.getBaseContext(),"Query Failed",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void onPostExecute(Song song){
        try{
            String maxTimeString = mainActivity.createTimeLabel((Integer.parseInt(song.getDuration())));
            mainActivity.textMaxTime.setText(maxTimeString);
            mainActivity.textCurrentPosition.setText("0:00");
            mainActivity.songTitle.setText(song.getTitle());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Song extractSong(Response response){
        Gson gson = new GsonBuilder().create();
        try{
            String res = response.body().string();
            Song song = gson.fromJson(res, Song.class);
            return song;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Request createRequest(String operation, String address, String currentSong){

        Request request;
        if(currentSong == ""){
            request = new Request.Builder()
                    .url(address + '/' + operation+"?"+"testing=yes")
                    .build();
        }
        else{
            request = new Request.Builder()
                    .url(address + '/' + operation+"?" + "song="+currentSong)
                    .build();
        }
        return request;
    }

    public void initPlayer(String address, Song song){
        try{


            String songUri = address+"/raw/"+song.path+".mp3";
            Uri myUri = Uri.parse(songUri);

            mainActivity.mediaPlayer = new MediaPlayer();
            mainActivity.mediaPlayer.setDataSource(songUri);
            mainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mainActivity.mediaPlayer.prepareAsync();

            //mp3 will be started after completion of preparing...
            mainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    mainActivity.mediaPlayer.seekTo(0);
                    mainActivity.mediaPlayer.setLooping(false); // par défaut
                    mainActivity.mediaPlayer.setVolume(0.5f, 0.5f);
                    player.start();
                }
            });

            mainActivity.streamService.setCurrentSong(song.getTitle());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setNextSong(){
        Toast.makeText(mainActivity.getBaseContext(),"Playing Next Song",Toast.LENGTH_SHORT).show();
    }

    public void setPreviousSong(){
        Toast.makeText(mainActivity.getBaseContext(),"Playing previous Song",Toast.LENGTH_SHORT).show();
    }

    public void shuffleNextSong(){
        Toast.makeText(mainActivity.getBaseContext(),"Shuffling next Song",Toast.LENGTH_SHORT).show();
    }
}