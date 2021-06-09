package it.main;

import android.os.AsyncTask;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostData extends AsyncTask<JSONObject, Void, JSONObject>
{
    private ChooseFragment instance;
    public PostData(ChooseFragment instance) { this.instance = instance; }

    @Override
    protected JSONObject doInBackground(JSONObject... objs)
    {
        try
        {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objs[0].toString());
            Request request = new Request.Builder()
                    .url("http://vaccinationdata.duckdns.org:8000/data/")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String rec = response.body().string();
            JSONObject vaccine = new JSONObject(rec.substring(1, rec.length() - 1));
            return vaccine.get("vaccino").equals("") ? null : vaccine;
        } catch(Exception e) { return null; }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject)
    {
        if(jsonObject == null)
            instance.focusEmail();
        else
        {
            Map<String, Object> vaccinato = new HashMap<String, Object>();
            vaccinato.put("cittaNascita", instance.data[0]);
            vaccinato.put("codiceFiscale", instance.data[1]);
            vaccinato.put("cognome", instance.data[2]);
            vaccinato.put("dataNascita", instance.data[3]);
            vaccinato.put("eta", instance.getEta(instance.data[3]));
            vaccinato.put("genere", instance.data[4]);
            vaccinato.put("indirizzo", instance.data[5]);
            vaccinato.put("nazioneNascita", instance.data[6]);
            vaccinato.put("nome", instance.data[7]);
            vaccinato.put("provincia", instance.data[8]);
            vaccinato.put("residenza", instance.data[9]);

            try
            {
                vaccinato.put("dataVaccino1", jsonObject.get("dataVaccino1"));
                vaccinato.put("dataVaccino2", jsonObject.get("dataVaccino2"));
                vaccinato.put("vaccino", jsonObject.get("vaccino"));
            } catch(Exception e) { e.printStackTrace(); }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("vaccinati").document().set(vaccinato);

            instance.userHasBooked();
        }
    }
}
