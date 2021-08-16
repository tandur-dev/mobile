package com.android.tandur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.tandur.R;

public class LahanActivity extends Activity {

    Button btnsewa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_detail);

        btnsewa = (Button) findViewById(R.id.btnSewa);

        btnsewa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sewaSekarang(view);
            }
        });
    }

    public void sewaSekarang(View view) {
        Intent intent = new Intent(this, confirmsewa.class);
        intent.putExtra("kodesewa", "1691202171");
        startActivity(intent);
    }
}
