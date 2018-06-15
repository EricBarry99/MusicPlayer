package etsmtl.gti785.musicplayer;

import com.squareup.okhttp.OkHttpClient;

public class StreamService {

    OkHttpClient client;
    MainActivity mainActivity;
    String serverPort = "8765";
    String serverIp = "192.168.43.75";
//    String serverIp = "192.168.0.111";
    String httpPrefix = "http://";
    RequestHandler requestHandler;
    String currentSong = "";
    String currentFileName = "";


    public StreamService(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
    }

    public String getCurrentSong() {
        return currentSong;
    }

    String getServerAdress() {
        return httpPrefix + serverIp + ':' + serverPort;
    }

    void initPlayer() {
        try {
            requestHandler = new RequestHandler(client, mainActivity);
            requestHandler.execute("initPlayer", getServerAdress(),"");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void getNextSong() {
        try {
            requestHandler = new RequestHandler(client, mainActivity);
            requestHandler.execute("nextSong", getServerAdress(), getCurrentSong());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void getPreviousSong() {
        try {
            requestHandler = new RequestHandler(client, mainActivity);
            requestHandler.execute("previousSong", getServerAdress(), getCurrentSong());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void shuffle() {
        try {
            requestHandler = new RequestHandler(client, mainActivity);
            requestHandler.execute("shuffleSong", getServerAdress(), getCurrentSong());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}