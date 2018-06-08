package etsmtl.gti785.musicplayer;

import android.os.AsyncTask;
import android.widget.Toast;
import bean.Song;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class RequestHandler extends AsyncTask<String, Void, String> {

    OkHttpClient client;
    MainActivity mainActivity;

    public RequestHandler(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    @Override
    // 0 request, 1 adress, 2 caller
    protected String doInBackground(String... objects) {

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
          //      String jsonData = response.body().string();
           //     JSONObject Jobject = new JSONObject(jsonData);
          //      Jobject.put("caller",caller);

//                     JSONObject Jobject = new JSONObject(response.g);

//                JSONObject Jobject = new JSONObject();
//                Jobject.put("response", response.body().string());

                String resStr = response.body().string().toString();
                JSONObject json = new JSONObject(resStr);

                String test =  json.get("nameValuePairs").toString();

                JSONObject json2 = new JSONObject(json.get("nameValuePairs").toString());


//                return response.body().string();

          //      return Jobject;
                return "";

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }




    public void onPostExecute(String response){

        String caller = "";
        try{
//            caller = jsonData.get("nameValuePairs")[1];
        }catch(Exception e){
            e.printStackTrace();
        }
        Song song = jsonDecode(response);


        if(song != null){
            if(caller == "next_song"){
                mainActivity.songTitle.setText(song.getTitle());
            }
            else if (caller == "play"){
            }
        }

    }


    public Song jsonDecode(String response){
        Gson gson = new GsonBuilder().create();
        try{
            JSONObject obj = new JSONObject(response);

            Song song = gson.fromJson(response, Song.class);
            return song;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }





//
//    public void onPostExecute(JSONObject jsonData){
//
//        String caller = "";
//        try{
////            caller = jsonData.get("nameValuePairs")[1];
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//            Song song = jsonDecode(jsonData);
//
//
//        if(song != null){
//            if(caller == "next_song"){
//                mainActivity.songTitle.setText(song.getTitle());
//            }
//            else if (caller == "play"){
//            }
//        }
//
//    }
//
//
//    public Song jsonDecode(JSONObject jsonObject){
//        Gson gson = new GsonBuilder().create();
//        try{
//            Song song = gson.fromJson(jsonObject.get("response").toString(), Song.class);
//            return song;
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }



}
