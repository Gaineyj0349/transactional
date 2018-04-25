package com.gainwise.transactional.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gainwise.transactional.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
