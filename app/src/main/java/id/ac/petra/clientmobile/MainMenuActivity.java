package id.ac.petra.clientmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ListKonserButton;
    private Button PesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ListKonserButton = findViewById(R.id.ListKonserButton);
        PesanButton = findViewById(R.id.PesanButton);

        ListKonserButton.setOnClickListener(this);
        PesanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ListKonserButton:
                openListKonserActivity();
                break;
            case R.id.PesanButton:
                openPesanActivity();
                break;
        }
    }

    private void openListKonserActivity() {
        Intent intent = new Intent(this, ListKonserActivity.class);
        startActivity(intent);
    }

    private void openPesanActivity() {
        Intent intent = new Intent(this, PesanActivity.class);
        startActivity(intent);
    }
}