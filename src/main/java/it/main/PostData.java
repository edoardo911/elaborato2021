package it.main;

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class PostData extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground(String... strings)
    {
        try
        {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strings[0]);
            Request request = new Request.Builder()
                    .url("http://vaccinationdata.duckdns.org:8000/data/")
                    .post(body)
                    .build();
            client.newCall(request).execute();
        } catch(Exception e) { e.printStackTrace(); }
        return null;
    }
}
