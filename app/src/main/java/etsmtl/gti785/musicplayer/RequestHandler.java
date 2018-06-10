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

public class RequestHandler extends AsyncTask<String, Void, JsonObject> {

    OkHttpClient client;
    MainActivity mainActivity;

    public RequestHandler(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    @Override
    // 0 request, 1 address, 2 operation
    protected JsonObject doInBackground(String... objects) {

        Response response = null;
        JsonObject jsonObject = new JsonObject();

        String operation = objects[0].toString();
        String address = objects[1].toString();
        String currentSong = objects[2].toString();
        Request request = createRequest(operation, address, currentSong);

        try {
            response = client.newCall(request).execute();

            if(response != null && response.isSuccessful()){
                jsonObject = createJsonObjectFromResponse(response, operation, address);
                if(jsonObject != null){
                    return jsonObject;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void onPostExecute(JsonObject response){

        if(response != null){
            try{
                String operation = response.get("operation").getAsString();
                String address = response.get("address").getAsString();
                Song song = jsonDecode(response.get("song"));

                // set current song for the next time we make a request
                mainActivity.streamService.setCurrentSong(song.getTitle());

                // http.192.168.../raw/songname
                // operations de controle du player
                if(song != null){
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
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(mainActivity.getBaseContext(),"Query Failed",Toast.LENGTH_SHORT).show();
        }
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
            mainActivity.songTitle.setText(song.getTitle());

    //       String songUri = "file://" + address+"/raw/"+song.path+".mp3";
            String songUri = address+"/raw/"+song.path;
//            String songUri = address+"/raw/"+song.path+".mp3";
            Uri myUri = Uri.parse(songUri);

            mainActivity.mediaPlayer = null;
            mainActivity.mediaPlayer = new MediaPlayer();
            mainActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // mainActivity.mediaPlayer = MediaPlayer.create(mainActivity.context(), myUri);

            // faire un contentprovider pour gérer l'URL ?
//            ContentProvider contentProvider =

            mainActivity.mediaPlayer.setDataSource(mainActivity.context, myUri);
            mainActivity.mediaPlayer.prepare();

            int duration = mainActivity.mediaPlayer.getDuration();
            mainActivity.seekBar.setMax(duration);
            String maxTimeString = mainActivity.createTimeLabel(duration);
            mainActivity.textCurrentPosition.setText("0:00");

            mainActivity.textMaxTime.setText(Integer.parseInt(song.getDuration()) / 100);
    //       mainActivity.textMaxTime.setText(maxTimeString);

            mainActivity.mediaPlayer.seekTo(0);
            mainActivity.mediaPlayer.setLooping(false); // par défaut
            mainActivity.mediaPlayer.setVolume(0.5f, 0.5f);

            mainActivity.mediaPlayer.start();
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
