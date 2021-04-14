package br.com.arcom.signpad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.com.arcom.signpad.R;

public class ConclusaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conclusao_activity);
    }

    public void toNextActivity(View view) {
        Intent intent = new Intent(ConclusaoActivity.this, SignPadActivity.class);
        startActivity(intent);
        finish();
    }

}