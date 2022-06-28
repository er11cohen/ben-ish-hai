package com.eran.benishhai;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.eran.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WebActivity extends Activity {

    WebView wv;
    WebSettings wvSetting;
    int scrollY = 0;
    MenuItem nightModeItem = null;
    SharedPreferences BIHPreferences;
    SharedPreferences defaultSharedPreferences;
    boolean fullScreen = false;
    AudioManager am;
    String phoneStatus;
    int startRingerMode = 2;// RINGER_MODE_NORMAL
    String appName = "/BenIshHai";

    String yearEn, yearHe, humashEn, parshHe, parshEn, textToSearch;
    boolean pageReady = false;
    GestureDetector gs = null;
    ActionBar actionBar = null;
    SearchView searchView;
    MenuItem PreviousMI = null;
    MenuItem NextMI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        fullScreen = defaultSharedPreferences.getBoolean("CBFullScreen", false);

        setContentView(R.layout.activity_web);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            actionBar.hide();
        }

        boolean keepScreenOn = defaultSharedPreferences.getBoolean(
                "CBKeepScreenOn", false);
        if (keepScreenOn) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        am = (AudioManager) getBaseContext().getSystemService(
                Context.AUDIO_SERVICE);
        phoneStatus = defaultSharedPreferences.getString("phone_status", "-1");

        BIHPreferences = getSharedPreferences("BIHPreferences", MODE_PRIVATE);
        wv = (WebView) findViewById(R.id.webViewBIH);
        wvSetting = wv.getSettings();
        // registerForContextMenu(wv);

        Intent intent = getIntent();
        String requiredFileName = intent.getStringExtra("requiredFileName");
        if (requiredFileName != null) {
            ArrayList<Location> locationList;
            Gson gson = new Gson();

            SharedPreferences preferences = getSharedPreferences("Locations",
                    MODE_PRIVATE);
            String preferencesLocationsJson = preferences.getString(
                    "preferencesLocationsJson", null);
            if (preferencesLocationsJson != null) {
                locationList = gson.fromJson(preferencesLocationsJson,
                        new TypeToken<ArrayList<Location>>() {
                        }.getType());
                Location requiredLocation = null;
                if (requiredFileName.equals("-1")/* lastLocation */) {
                    int lastLocation = locationList.size() - 1;
                    requiredLocation = locationList.get(lastLocation);
                } else// History
                {
                    for (int i = locationList.size() - 1; i >= 0; i--) {
                        requiredLocation = locationList.get(i);
                        if (requiredLocation.getTime().equals(requiredFileName)) {
                            break;
                        }
                    }
                }

                if (requiredLocation != null) {
                    yearEn = requiredLocation.getYearEn();
                    yearHe = requiredLocation.getYearHe();
                    humashEn = requiredLocation.getHumashEn();
                    parshHe = requiredLocation.getParshHe();
                    parshEn = requiredLocation.getParshEn();
                    scrollY = requiredLocation.getScrollY();
                } else {
                    finish();// not need to arrive to here
                }
            } else {
                finish();// not need to arrive to here
            }
        } else {
            Location location = (Location) intent.getParcelableExtra("location");
            yearEn = location.getYearEn();
            yearHe = location.getYearHe();
            humashEn = location.getHumashEn();
            parshHe = location.getParshHe();
            parshEn = location.getParshEn();
            textToSearch = location.getTextToSearch();
        }

        wvSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvSetting.setJavaScriptEnabled(true);
        LoadWebView();

        WeakReference<Activity> weakReferenceActivity = new WeakReference<Activity>(this);
        Utils.toggleFullScreen(weakReferenceActivity, getApplicationContext(), R.id.webViewBIH, actionBar, fullScreen);
        Utils.firstDoubleClickInfo(defaultSharedPreferences, weakReferenceActivity);
    }

    protected void onResume() {
        super.onResume();// Always call the superclass method first

        startRingerMode = am.getRingerMode();
        Utils.setRingerMode(this, Integer.parseInt(phoneStatus), startRingerMode);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web, menu);
        nightModeItem = menu.findItem(R.id.nightMode);

        NextMI = menu.findItem(R.id.next);
        PreviousMI = menu.findItem(R.id.previous);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            // Toast.makeText(getApplicationContext(),"onQueryTextChange "
            // ,Toast.LENGTH_LONG).show();

            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            searchItem.setVisible(true);
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("חיפוש בפרשה הנוכחית");

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    scrollY = wv.getScrollY();// save the location before the
                    // search
                    find(query);
                    // TODO Auto-generated method stub
                    return true;
                }

            });

            searchView.setOnCloseListener(new OnCloseListener() {
                @Override
                public boolean onClose() {
                    closeSearch(true);
                    return false;
                }

            });

            searchView.setOnSearchClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.nightMode:
                Utils.NightMode(true, BIHPreferences, wv, nightModeItem);
                break;
            case R.id.zoomUp:
                Utils.changeSize(true, BIHPreferences, wvSetting);
                break;
            case R.id.zoomDown:
                Utils.changeSize(false, BIHPreferences, wvSetting);
                break;
            case R.id.previous:
                previous();
                break;
            case R.id.next:
                next();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadWebView() {
        Utils.setOpacity(wv, 0.1);// for where the wv already exist
        setTitle(yearHe + " - " + parshHe);
        wv.loadUrl("file:///android_asset/" + yearEn + "/" + humashEn + "/"
                + parshEn + ".html");
        int size = Utils.readSize(BIHPreferences);
        wvSetting.setDefaultFontSize(size);

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utils.setOpacity(wv, 0.1);
                Utils.showWebView(wv,
                        (ProgressBar) findViewById(R.id.progressBarBIH), true);
                if (scrollY != 0) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            wv.scrollTo(0, scrollY);
                            setPageFinishSettings();
                        }
                    }, 1100);
                } else if (textToSearch != null) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            searchView.setIconified(false);//open the search view programmatically
                            searchView.setQuery(textToSearch, true);
                            searchView.clearFocus();//close the keyboard
                            setPageFinishSettings();
                        }
                    }, 1100);
                } else {
                    setPageFinishSettings();
                }
            }
        });
    }

    private void setPageFinishSettings() {
        ChangeWebViewBySettings();
        pageReady = true;
        Utils.setOpacity(wv, 1);
    }

    private void ChangeWebViewBySettings() {
        Utils.NightMode(false, BIHPreferences, wv, nightModeItem);
    }

    @Override
    protected void onPause() {
        super.onPause(); // Always call the superclass method first

        if (!phoneStatus.equals("-1") && startRingerMode != 0/* silent */) {
            am.setRingerMode(startRingerMode);
        }

        saveLastLocation();
    }

    private void saveLastLocation() {
        File path = Utils.getFilePath(getApplicationContext());
        File folder = new File(path + appName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (success && pageReady) {
            SharedPreferences preferences = getSharedPreferences(
                    "Locations", MODE_PRIVATE);
            String preferencesLocationsJson = preferences.getString(
                    "preferencesLocationsJson", null);

            if (preferencesLocationsJson == null)// for second install,
            // remove the old files
            {
                if (folder.isDirectory()) {
                    String[] children = folder.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(folder, children[i]).delete();
                    }
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentTime = sdf.format(new Date());

            View content = findViewById(R.id.layoutBenIshHi);
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();
            File file = new File(path + appName + "/" + currentTime + ".png");
            ArrayList<Location> locationList = new ArrayList<Location>();
            Gson gson = new Gson();
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, ostream);
                ostream.close();

                scrollY = wv.getScrollY();
                Location location = new Location(currentTime, yearEn,
                        yearHe, "", humashEn, parshHe, parshEn, scrollY);

                if (preferencesLocationsJson != null) {
                    locationList = gson.fromJson(preferencesLocationsJson,
                            new TypeToken<ArrayList<Location>>() {
                            }.getType());
                    if (locationList.size() >= 10) {
                        String idFirstLocation = locationList.get(0)
                                .getTime();
                        File imageToDelete = new File(path + appName + "/" + idFirstLocation + ".png");
                        if (imageToDelete.exists()) {
                            boolean deleted = imageToDelete.delete();
                        }

                        locationList.remove(0);
                    }
                }

                locationList.add(location);

                String json = gson.toJson(locationList);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("preferencesLocationsJson", json);
                editor.commit();

            } catch (Exception e) {
            }
        }
    }

    @SuppressLint("NewApi")
    public void onBackPressed() {

        wv.clearFocus();// for close pop-up of copy, select etc.

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (!searchView.isIconified()) {
                closeSearch(false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    // for voice search
    @SuppressLint("NewApi")
    @Override
    protected void onNewIntent(Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
    }

    private void closeSearch(Boolean fromListener) {
        wv.clearMatches();// clear the finds
        PreviousMI.setVisible(false);
        NextMI.setVisible(false);

        if (!fromListener) {// the listener do this by himself
            searchView.setIconified(true);// clear the searchView
            searchView.onActionViewCollapsed();// close the searchView
        }
    }

    @SuppressLint("NewApi")
    private void find(String query) {
        PreviousMI.setVisible(true);
        NextMI.setVisible(true);
        wv.findAllAsync(query);
    }

    private void previous() {
        wv.findNext(false);
    }

    private void next() {
        wv.findNext(true);
    }
}
