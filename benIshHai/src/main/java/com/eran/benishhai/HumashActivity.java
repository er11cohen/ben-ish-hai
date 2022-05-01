package com.eran.benishhai;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HumashActivity extends Activity {

    String yearEn, yearHe;
    Location location;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humash);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        location = (Location) intent.getParcelableExtra("location");
        yearHe = location.getYearHe();//intent.getStringExtra("yearHe");
        setTitle(yearHe);
        yearEn = location.getYearEn();//intent.getStringExtra("yearEn");

    }

    public void SelectIntro(View v) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
//	    	intent.putExtra("yearHe", "בן איש חי");
//	    	intent.putExtra("yearEn", "intro_1");
//	    	intent.putExtra("parshHe", "הקדמה");
//	    	intent.putExtra("parshEn", "intro");
//	    	intent.putExtra("humashEn", "intro_2");

        location.setYearHe("בן איש חי");
        location.setYearEn("intro_1");
        location.setParshHe("הקדמה");
        location.setParshEn("intro");
        location.setHumashEn("intro_2");
        intent.putExtra("location", location);
        startActivity(intent);
    }

    public void SelectHumash(View v) {
        Intent intent = new Intent(getApplicationContext(), ParashotActivity.class);
//    	intent.putExtra("yearHe", yearHe);
//    	intent.putExtra("yearEn", yearEn);
//    	String humashEn = (String)((Button)v).getTag();
//    	intent.putExtra("humashEn", humashEn);
//    	intent.putExtra("humashHe", ((Button)v).getText());

        location.setHumashEn((String) ((Button) v).getTag());
        location.setHumashHe((String) ((Button) v).getText());
        location.setYearEn(yearEn);
        location.setYearHe(yearHe);
        location.setParshHe(null);
        location.setParshEn(null);
        intent.putExtra("location", location);
        startActivity(intent);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
