package id.ac.petra.clientmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PesanActivity extends AppCompatActivity {

    private EditText namalengkapField;
    private EditText emailField;
    private EditText nomorhpField;
    private EditText namakonserField;
    private Spinner jenistiketField;
    private Button pesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        namalengkapField = findViewById(R.id.namalengkapField);
        emailField = findViewById(R.id.emailField);
        nomorhpField = findViewById(R.id.nomorhpField);
        namakonserField = findViewById(R.id.namakonserField);
        jenistiketField = findViewById(R.id.jenistiketField);
        pesanButton = findViewById(R.id.pesanButton);

        List<String> jenis = new ArrayList<>();
        jenis.add("Tiket Biasa");
        jenis.add("Tiket VIP");

        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenis);
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenistiketField.setAdapter(jenisAdapter);

        pesanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namalengkap = namalengkapField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String nomorhp = nomorhpField.getText().toString().trim();
                String namakonser = namakonserField.getText().toString().trim();
                String jenistiket = jenistiketField.getSelectedItem().toString().trim();

                if (namalengkap.isEmpty() || email.isEmpty() || nomorhp.isEmpty() || namakonser.isEmpty() || jenistiket.isEmpty()) {
                    Toast.makeText(PesanActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookingData = new JSONObject();
                    try {
                        bookingData.put("namalengkap", namalengkap);
                        bookingData.put("email", email);
                        bookingData.put("nomorhp", nomorhp);
                        bookingData.put("namakonser", namakonser);
                        bookingData.put("jenistiket",jenistiket);

                        new BookAsyncTask().execute(bookingData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String bookingData = params[0];
                URL url = new URL("http://192.168.141.181:7000/pesan");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(bookingData.getBytes());
                os.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    result = responseBuilder.toString();
                } else {
                    result = "Error: " + responseCode;
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error occurred while connecting to the server.";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(PesanActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}