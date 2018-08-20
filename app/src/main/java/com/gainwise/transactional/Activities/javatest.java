package com.gainwise.transactional.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dekoservidoni.omfm.OneMoreFabMenu;
import com.gainwise.transactional.R;

import spencerstudios.com.fab_toast.FabToast;

public class javatest extends AppCompatActivity {


    static OneMoreFabMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_javatest);


    }

    public void MIT(View V) {
        try{
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://opensource.org/licenses/MIT?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6607"));
        startActivity(browserIntent);
    } catch(Exception e){
            FabToast.makeText(this, "There was an error opening, please try with internet",
                    FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
        }
    }
    public void APACHE(View v){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://opensource.org/licenses/Apache-2.0?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6819"));
            startActivity(browserIntent);
        }catch(Exception e){
            FabToast.makeText(this, "There was an error opening, please try with internet",
                    FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
        }
    }


}