package etsmtl.gti785.musicplayer;

import android.os.AsyncTask;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class RequestHandler extends AsyncTask {

    OkHttpClient client;
    MainActivity mainActivity;

    public RequestHandler(OkHttpClient client, MainActivity mainActivity) {
        this.client = client;
        this.mainActivity = mainActivity;
    }

    @Override
    // 0 request, 1 adress, 2 caller
    protected Object doInBackground(Object[] objects) {

        String param = (String) objects[0];
        String adress = (String) objects[1];
        String caller = (String) objects[1];

        Request request = new Request.Builder()
                .url(adress + param)
                .build();

        try {
            Response response = client.newCall(request).execute();
            final String body = (String) response.body().string();

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.songTitle.setText(body);
                    Toast toast = Toast.makeText(mainActivity.getApplicationContext(),"ENFIN CALISS",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            return response.body();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
