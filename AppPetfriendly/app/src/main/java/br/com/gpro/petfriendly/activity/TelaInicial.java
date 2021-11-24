package br.com.gpro.petfriendly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.gpro.petfriendly.MainActivity;
import br.com.gpro.petfriendly.R;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }
        },3000);
    }
}
