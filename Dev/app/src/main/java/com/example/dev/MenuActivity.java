package com.example.dev;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity {

    /*TextView one,two;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Could use this to make the bottom nav bar centered correctly, not sure how to get it into the xml file though.
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;*/


        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.MenuDisplayFCV, new MenuFragment(), "profile");
        fTrans.replace(R.id.NavBarFCV, new NavBar(), "navbar");
        fTrans.commit();

    }

}