package com.eran.benishhai;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.eran.utils.Utils;

import java.util.ArrayList;

public class SearchInAllBook extends Activity {

    SearchView searchView;
    String[] BIHRows = null;
    String queryText = "";
    private ListView lv;
    private ArrayList<Halach> alHalach;
    ProgressBar progressBar;
    TextView TVNoResult;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    this::backButton
            );
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search_in_all_book);
        lv = findViewById(R.id.ListViewHalach);
        progressBar = findViewById(R.id.progressBarSearchInAllBook);
        TVNoResult = findViewById(R.id.TVNoResult);

        alHalach = new ArrayList<>();

        lv.setOnItemClickListener((parent, currView, position, id) -> {
            Halach selected = (Halach) lv.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            Location location = new Location(selected.getYearEn(), selected.getYearHe(), selected.getHumashHe(),
                    selected.getHumashEn(), selected.getParshHe(), selected.getParshEn(), queryText);
            intent.putExtra("location", location);
            startActivity(intent);
        });

        String text = Utils.ReadTxtFile("files/benIshHaiCSV.csv", getApplicationContext());
        BIHRows = text.split("\n");
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_in_all_book, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchItem.setVisible(true);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("חיפוש בכל הספר");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                //Toast.makeText(getApplicationContext(),"onQueryTextChange " + query ,Toast.LENGTH_LONG).show();
                int queryLength = query.length();
                if (queryLength > 0) {
                    String lastChar = query.substring(query.length() - 1);
                    if (lastChar.matches("[a-zA-Z]")) {
                        Toast.makeText(getApplicationContext(), "יש להכניס תווים בעברית בלבד", Toast.LENGTH_SHORT).show();
                        query = query.substring(0, query.length() - 1);//remove last char
                        searchView.setQuery(query, false);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                TVNoResult.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);

                queryText = query;
                searchView.clearFocus();//close the keyboard
                searchInAllBook();
                // TODO Auto-generated method stub
                return true;
            }

        });

        searchView.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }

        });

        searchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
            }
        });

        searchView.setIconified(false);//open the search view programmatically
        return true;
    }

    @SuppressLint("NewApi")
    private void closeSearch() {
        searchView.setIconified(true);// clear the searchView
        searchView.onActionViewCollapsed();// close the searchView
    }

    @SuppressLint("NewApi")
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.BAKLAVA) {
            backButton();
        }
    }

    private void backButton() {
        if (!searchView.isIconified()) {
            closeSearch();
        } else {
            finish();
        }
    }

    // for voice search
    @SuppressLint("NewApi")
    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, true);

            // close the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
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

    private void searchInAllBook() {
        try {
            alHalach.clear();
            for (int i = 0; i < BIHRows.length; i++) {
                String[] columns = BIHRows[i].split(",");
                String parashText = Utils.ReadTxtFile(columns[1] + "/" + columns[4] + "/" + columns[3] + ".html"
                        , getApplicationContext());
                int count = parashText.split(queryText, -1).length - 1;
                if (count > 0) {

                    String str = columns[0] + " - " + columns[2] + " - " + columns[5];
                    str += " - " + count + " תוצאות";
                    Halach halach = new Halach(-2, columns[0], columns[1], columns[2], columns[3], "", columns[4], str, null);
                    alHalach.add(halach);
                }
            }
            ArrayAdapter<Halach> adapter = new ArrayAdapter<Halach>(this, android.R.layout.simple_list_item_1, alHalach);
            lv.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            if (alHalach.size() > 0) {
                lv.setVisibility(View.VISIBLE);
            } else {
                TVNoResult.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
