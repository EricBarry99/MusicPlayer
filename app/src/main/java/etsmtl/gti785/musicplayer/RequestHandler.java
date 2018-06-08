package etsmtl.gti785.musicplayer;

import android.os.AsyncTask;

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
    // 0 request, 1 adress, 2 caller
    protected JsonObject doInBackground(String... objects) {

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
                //https://stackoverflow.com/questions/28405545/strange-namevaluepairs-key-appear-when-using-gson?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

                String jsonData = response.body().string();
                int size = jsonData.length();
                jsonData = jsonData.substring(18, size-1);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("song", jsonData);
                jsonObject.addProperty("caller", caller);

                return jsonObject;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public void onPostExecute(JsonObject response){

        try{
            String caller = response.get("caller").getAsString();
            Song song = jsonDecode(response.get("song"));

            // http.192.168.../raw/songname
            // operations de controle du player
            if(song != null){
                if(caller == "next_song"){
                    mainActivity.songTitle.setText(song.getTitle());
//                    mainActivity.mediaPlayer.setDataSource(song.getPath());
                }
                else if (caller == "play"){
                }
            }
        }catch(Exception e){
            e.printStackTrace();
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
}
