package com.eran.benishhai;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.eran.utils.Utils;

import java.util.ArrayList;

public class KeyYearsActivity extends Activity {

    private ListView lv;
    private EditText et;
    private ArrayList<Halach> alHalach;
    private ArrayList<Halach> alHalachFilter;
    int textlength = 0;
    private static final int RECOGNIZE_SPEECH_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_years);

        //prevent open keyboard automatically
        //done on xml
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button micButton = (Button) findViewById(R.id.button_mic);
        micButton.setBackgroundResource(android.R.drawable.presence_audio_busy);
        micButton.setVisibility(View.VISIBLE);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.ListViewHlach);
        et = (EditText) findViewById(R.id.EditTextSearch);


        alHalachFilter = new ArrayList<Halach>();
        alHalach = new ArrayList<Halach>();


        String text = Utils.ReadTxtFile("files/benIshHaiCSV.csv", getApplicationContext());
        fillHalachArray(text);
        ArrayAdapter<Halach> adapter = new ArrayAdapter<Halach>(this, android.R.layout.simple_list_item_1, alHalach);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View currView, int position, long id) {
                Halach selected = (Halach) lv.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), WebActivity.class);

                Location location = new Location(null, selected.getYearEn(), selected.getYearHe(), selected.getHumashHe(),
                        selected.getHumashEn(), selected.getParshHe(), selected.getParshEn(), -1);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });


        et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {
                // Abstract Method of TextWatcher Interface.
            }

            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {
                textlength = et.getText().length();
                alHalachFilter.clear();
                for (int i = 0; i < alHalach.size(); i++) {
                    if (textlength <= alHalach.get(i).getText().length()) {
                        if (alHalach.get(i).getText().contains(et.getText().toString())) {
                            alHalachFilter.add(alHalach.get(i));
                        }
                    }
                }
                lv.setAdapter(new ArrayAdapter<Halach>(KeyYearsActivity.this, android.R.layout.simple_list_item_1, alHalachFilter));
            }
        });

    }

    private void fillHalachArray(String text) {
        try {
            String[] items = text.split("\n");
            for (int i = 0; i < items.length; i++) {
                String[] halachStr = items[i].split(",");
                String str = halachStr[0] + " - " + halachStr[2] + " - " + halachStr[5];
                Halach halach = new Halach(-2, halachStr[0], halachStr[1], halachStr[2], halachStr[3], "", halachStr[4], str, null);
                alHalach.add(halach);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ClearSearch(View v) {
        et.setText("");
    }

    public void RecognizeSpeech(View v) {
        Boolean isConnected = Utils.isConnected(getApplicationContext());
        if (isConnected) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he-IL");
            // intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"string to show on pop up");
            startActivityForResult(intent, RECOGNIZE_SPEECH_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "אנא בדוק חיבורך לאינטרנט", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOGNIZE_SPEECH_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            et.setText(matches_text.get(0));
        }
    }

}
