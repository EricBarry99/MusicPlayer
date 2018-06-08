package etsmtl.gti785.musicplayer;

import com.squareup.okhttp.OkHttpClient;


public class StreamService {

    OkHttpClient client;
    MainActivity mainActivity;
    String serverPort = "8765";
//    String serverIp = "192.168.0.103";
//    String serverIp = "10.192.186.245";
    String serverIp = "192.168.43.75";
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
            requestHandler.execute("/play", getServerAdress(), "next_song");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}