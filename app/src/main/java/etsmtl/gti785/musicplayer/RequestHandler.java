package etsmtl.gti785.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        JsonObject jsonObject = new JsonObject();

        String operation = objects[0].toString();
        String address = objects[1].toString();
        String currentSong = objects[2].toString();
        Request request = createRequest(operation, address, currentSong);

        try {
            response = client.newCall(request).execute();
            if(response != null && response.isSuccessful()){

                Song song = extractSong(response);

                if(song != null){
                    mainActivity.streamService.setCurrentSong(song.getTitle());

                    if(operation == "initPlayer"){
                        initPlayer(address, song);
                    }
                    else if (operation == "nextSong"){
                        setNextSong();
                    }
                    else if (operation == "previousSong"){
                        setPreviousSong();
                    }
                    else if (operation == "shuffleSong"){
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
//            int duration = mainActivity.mediaPlayer.getDuration();
            mainActivity.seekBar.setMax((Integer.parseInt(song.getDuration())/100));
            String maxTimeString = mainActivity.createTimeLabel((Integer.parseInt(song.getDuration())/100));
            mainActivity.textCurrentPosition.setText("0:00");

    //        mainActivity.textMaxTime.setText(Integer.parseInt(song.getDuration()) / 100);
    //       mainActivity.textMaxTime.setText(maxTimeString);
//
//            mainActivity.mediaPlayer.seekTo(0);
//            mainActivity.mediaPlayer.setLooping(false); // par défaut
//            mainActivity.mediaPlayer.setVolume(0.5f, 0.5f);

//            mainActivity.mediaPlayer.start();

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

    public Song jsonDecode(JsonElement response){
        Gson gson = new GsonBuilder().create();
        try{
            Song song = gson.fromJson(response.getAsString(), Song.class);
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

    public JsonObject createJsonObjectFromResponse(Response response, String operation, String address){
        // source: https://stackoaptuverflow.com/questions/28221555/how-does-okhttp-get-json-string
        //https://stackoverflow.com/questions/28405545/strange-namevaluepairs-key-appear-when-using-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        try{
            String jsonData = response.body().string();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("song", jsonData);
            jsonObject.addProperty("operation", operation);
            jsonObject.addProperty("address", address);

            return jsonObject;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void initPlayer(String address, Song song){
        try{

            String songUri = address+"/raw/"+song.path;

            mainActivity.mediaPlayer = null;
            mainActivity.mediaPlayer = new MediaPlayer();
            mainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mainActivity.mediaPlayer.setDataSource(songUri);
            mainActivity.mediaPlayer.prepareAsync();

            //mp3 will be started after completion of preparing...
            mainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    mainActivity.mediaPlayer.start();
                    mainActivity.mediaPlayer.seekTo(0);
            mainActivity.mediaPlayer.setLooping(false); // par défaut
            mainActivity.mediaPlayer.setVolume(0.5f, 0.5f);
                }
            });

//
//            int duration = mainActivity.mediaPlayer.getDuration();
//            mainActivity.seekBar.setMax(duration);
//            String maxTimeString = mainActivity.createTimeLabel(duration);
//            mainActivity.textCurrentPosition.setText("0:00");
//
//            mainActivity.textMaxTime.setText(Integer.parseInt(song.getDuration()) / 100);
//    //       mainActivity.textMaxTime.setText(maxTimeString);
//
//            mainActivity.mediaPlayer.seekTo(0);
//            mainActivity.mediaPlayer.setLooping(false); // par défaut
//            mainActivity.mediaPlayer.setVolume(0.5f, 0.5f);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void setNextSong(){

    }


    public void setPreviousSong(){

    }


    public void shuffleNextSong(){

    }

}
