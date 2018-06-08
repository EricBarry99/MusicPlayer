package etsmtl.gti785.musicplayer;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import bean.Song;

public class RequestHandler extends AsyncTask<String, Void, JSONObject> {

    OkHttpClient client;
    MainActivity mainActivity;

    public RequestHandler(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    @Override
    // 0 request, 1 adress, 2 caller
    protected JSONObject doInBackground(String... objects) {

        String param = objects[0].toString();
        String adress = objects[1].toString();
        String caller = objects[2].toString();

        Request request = new Request.Builder()
                .url(adress + param)
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();

            if(response != null){
                // source: https://stackoaptuverflow.com/questions/28221555/how-does-okhttp-get-json-string
                String jsonData = response.body().string();
                JSONObject Jobject = new JSONObject(jsonData);

                Jobject.put("caller",caller);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void onPostExecute(JSONObject response){

        String caller = "";
        try{
        }catch(Exception e){
            e.printStackTrace();
        }
        Song song = jsonDecode(response);


        // operations de controle du player
        if(song != null){
            if(caller == "next_song"){
                mainActivity.songTitle.setText(song.getTitle());
            }
            else if (caller == "play"){
            }
        }
    }


    public Song jsonDecode(JSONObject response){
        Gson gson = new GsonBuilder().create();
        try{
            Song song = gson.fromJson(response.toString(), Song.class);
            return song;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}