package com.eran.benishhai;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;

public class ParashotActivity extends Activity {

    String BreshitEnglish1 = "Breshit,Noah,Lekhlkha,Vayera,HayeSara,Toldot,Vayetse,Vayishlah,Vayeshev,Hanuca,Mikets,Vayigash,Vayhi";
    String ShmotEnglish1 = "Shmot,Vaera,Bo,Bshalah,Yitro,Mishpatim,Truma,Ttsave,Kitisa,Vayakhel,Pkude";
    String VayikraEnglish1 = "Vayikra,Tsav,Shmini,Tazria_Mtsora,Aharemot_Kdoshim,Emor,Bhar_Bhukotay";
    String BamidbarEnglish1 = "Bamidbar,Naso,Bhaalotkha,Shlahlkha,Korah,Hukat,Balak,Pinhas,Matot,Mase";
    String DvarimEnglish1 = "Dvarim,Vaethanan,Ekev,Ree,Shoftim,Kitetse,Kitavo,Nitsavim,Vayelekh,Haazinu,Vzothabraha";

    String BreshitHebrew1 = "בראשית,נח,לך לך,וירא,חיי שרה,תולדות,ויצא,וישלח,וישב,הלכות חנוכה,מקץ,ויגש,ויחי";
    String ShmotHebrew1 = "שמות,וארא,בא,בשלח,יתרו,משפטים,תרומה,תצוה,כי תשא,ויקהל,פקודי";
    String VayikraHebrew1 = "ויקרא,צו,שמיני,תזריע מצרע,אחרי מות קדושים,אמור,בהר בחקתי";
    String BamidbarHebrew1 = "במדבר,נשא,בהעלתך,שלח לך,קרח,חוקת,בלק,פינחס,מטות,מסעי";
    String DvarimHebrew1 = "דברים,ואתחנן,עקב,ראה,שופטים,כי תצא,כי תבוא,ניצבים,וילך,האזינו,וזאת הברכה";


    String BreshitEnglish2 = "Breshit,Noah,Lekhlkha,Vayera,HayeSara,Toldot,Vayetse,Vayishlah,Mikets,Vayigash,Vayhi";
    String ShmotEnglish2 = "Shmot,Vaera,Bo,Bshalah,Yitro,Mishpatim,Truma,Ttsave,Kitisa,Vayakhel,Pkude";
    String VayikraEnglish2 = "Vayikra,Tsav,Shmini,Tazria,Mtsora,Aharemot,Kdoshim,Emor,Bhar_Bhukotay";
    String BamidbarEnglish2 = "Naso,Bhaalotkha,Shlahlkha,Korah,Hukat,Balak,Pinhas,Matot,Mase";
    String DvarimEnglish2 = "Vaethanan,Ekev,Ree,Shoftim,Kitetse,Kitavo";

    String BreshitHebrew2 = "בראשית,נח,לך לך,וירא,חיי שרה,תולדות,ויצא,וישלח,מקץ,ויגש,ויחי";
    String ShmotHebrew2 = "שמות,וארא,בא,בשלח,יתרו,משפטים,תרומה,תצוה,כי תשא,ויקהל,פקודי";
    String VayikraHebrew2 = "ויקרא,צו,שמיני,תזריע,מצרע,אחרי מות,קדושים,אמור,בהר בחקתי";
    String BamidbarHebrew2 = "נשא,בהעלתך,שלח לך,קרח,חוקת,בלק,פינחס,מטות,מסעי";
    String DvarimHebrew2 = "ואתחנן,עקב,ראה,שופטים,כי תצא,כי תבוא";


    String yearEn, yearHe, humashEn, humashHe;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parashot);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] EngliseArr = null;
        String[] HebrewArr = null;

        Intent intent = getIntent();
        location = (Location) intent.getParcelableExtra("location");
        yearEn = location.getYearEn();
        yearHe = location.getYearHe();

        humashHe = location.getHumashHe();
        setTitle(yearHe + " - " + humashHe);
        humashEn = location.getHumashEn();

        if (yearEn.equals("Year_1")) {
            if (humashEn.equals("Breshit")) {
                EngliseArr = BreshitEnglish1.split(",");
                HebrewArr = BreshitHebrew1.split(",");
            } else if (humashEn.equals("Shmot")) {
                EngliseArr = ShmotEnglish1.split(",");
                HebrewArr = ShmotHebrew1.split(",");
            } else if (humashEn.equals("Vayikra")) {
                EngliseArr = VayikraEnglish1.split(",");
                HebrewArr = VayikraHebrew1.split(",");
            } else if (humashEn.equals("Bamidbar")) {
                EngliseArr = BamidbarEnglish1.split(",");
                HebrewArr = BamidbarHebrew1.split(",");
            } else if (humashEn.equals("Dvarim")) {
                EngliseArr = DvarimEnglish1.split(",");
                HebrewArr = DvarimHebrew1.split(",");
            }
        } else if (yearEn.equals("Year_2")) {
            if (humashEn.equals("Breshit")) {
                EngliseArr = BreshitEnglish2.split(",");
                HebrewArr = BreshitHebrew2.split(",");
            } else if (humashEn.equals("Shmot")) {
                EngliseArr = ShmotEnglish2.split(",");
                HebrewArr = ShmotHebrew2.split(",");
            } else if (humashEn.equals("Vayikra")) {
                EngliseArr = VayikraEnglish2.split(",");
                HebrewArr = VayikraHebrew2.split(",");
            } else if (humashEn.equals("Bamidbar")) {
                EngliseArr = BamidbarEnglish2.split(",");
                HebrewArr = BamidbarHebrew2.split(",");
            } else if (humashEn.equals("Dvarim")) {
                EngliseArr = DvarimEnglish2.split(",");
                HebrewArr = DvarimHebrew2.split(",");
            }
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayoutParashot);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, getResources().getDisplayMetrics());
        //LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        Button btnParash;
        View view;
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) 1.5, getResources().getDisplayMetrics());
        LayoutParams lpView = new LayoutParams(LayoutParams.MATCH_PARENT, height2);

        for (int i = 0; i < EngliseArr.length; i++) {
            String hebrewParash = HebrewArr[i];
            String engliseParash = EngliseArr[i];

            btnParash = new Button(this);
            btnParash.setText(hebrewParash);
            btnParash.setTag(engliseParash);
            btnParash.setOnClickListener(SelectParash);
            btnParash.setBackgroundResource(R.drawable.background_button);
            ll.addView(btnParash, lp);

            view = new View(this);
            view.setBackgroundColor(-16777216/*black*/);
            ll.addView(view, lpView);
        }
    }

    View.OnClickListener SelectParash = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            location.setParshHe(((Button) v).getText().toString());
            location.setParshEn(v.getTag().toString());
            intent.putExtra("location", location);
            startActivity(intent);
        }
    };


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
