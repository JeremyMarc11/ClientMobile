package id.ac.petra.clientmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListKonserActivity extends AppCompatActivity {

    private ListView listkonser;
    private Button pesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_konser);
        listkonser = findViewById(R.id.listkonser);

        new FetchKonserDataTask().execute();
    }

    private class FetchKonserDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result;
            try {
                URL url = new URL("http://192.168.141.181:7000/listkonser");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> konserList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject konserObject = jsonArray.getJSONObject(i);
                    String namakonser = konserObject.getString("namakonser");
                    String genre = konserObject.getString("genre");
                    String biasa = konserObject.getString("biasa");
                    String VIP = konserObject.getString("VIP");

                    String konserDetails = "Nama konser: " + namakonser + "\n" + "Genre: " + genre + "\n"
                            + "Harga Tiket Biasa: " + biasa + "\n"
                            + "Harga Tiket VIP: " + VIP;

                    konserList.add(konserDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListKonserActivity.this,
                        android.R.layout.simple_list_item_1, konserList);
                listkonser.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(ListKonserActivity.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}