package etsmtl.gti785.musicplayer;

import com.squareup.okhttp.OkHttpClient;

public class StreamService {


    OkHttpClient client;
    MainActivity mainActivity;
    String serverPort = "8765";
    String serverIp = "192.168.0.111";
    String httpPrefix = "http://";
    RequestHandler requestHandler;

    public StreamService(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    String getServerAdress() {
        return httpPrefix + serverIp + ':' + serverPort;
    }

    void getNextSong() {
        try {
            requestHandler = new RequestHandler(client, mainActivity);
            requestHandler.execute("/hello", getServerAdress(), "next song");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

/*
@TODO
Le service a les methodes de travail, il recoit les appels depuis lactivite et fais en sorte de caller le handler avec les bonnes infos pour les requetes
les resultats sont directement retournés au streamservice qui se chargera de voir quoi faire avec, dans les methodes appelées
 */