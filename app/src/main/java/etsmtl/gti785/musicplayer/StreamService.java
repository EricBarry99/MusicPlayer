package etsmtl.gti785.musicplayer;

import android.support.annotation.UiThread;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class StreamService {


    OkHttpClient client;
    MainActivity mainActivity;

    public StreamService(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }


    void sendRequest() throws IOException {

        // construction de la requete a envoyer
        Request request = new Request.Builder()
                .url("192.168.0.105:8765/hello")
                .build();

        // phase dappel au serveur
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ONFAILURE", e.getMessage(), e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String myResponse = response.body().string();
                // ici on sera en mesure de faire quelquechose avec la reponse

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.songTitle.setText(myResponse);
                    }
                });

            }
        });
    }

}
