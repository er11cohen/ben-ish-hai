package com.eran.benishhai;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.eran.utils.Utils;

import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;

import java.lang.ref.WeakReference;
import java.util.Calendar;

//Toast.makeText(this,Integer.toString(scrollY),Toast.LENGTH_LONG).show();
public class MainActivity extends Activity {

    SharedPreferences defaultSharedPreferences;
    final String shareTextIntent = "בן איש חי  - Ben Ish Chai https://play.google.com/store/apps/details?id=com.eran.benishhai";
    WeakReference<Activity> WeakReferenceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeakReferenceActivity = new WeakReference<Activity>(this);
        defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences BIHPreferences = getSharedPreferences(
                "BIHPreferences", MODE_PRIVATE);

        String version = BIHPreferences.getString("version", "-1");
        if (!version.equals("1.6.2")) {
            String message = Utils.ReadTxtFile("files/newVersion.txt",
                    getApplicationContext());
            ((TextView) new AlertDialog.Builder(this)
                    .setTitle("חדשות ללומדי הבן איש חי")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setIcon(drawable.ic_input_add)
                    .setMessage(Utils.fromHtml(message))
                    .setPositiveButton("אשריכם תזכו למצוות",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            }).show().findViewById(android.R.id.message))
                    .setMovementMethod(LinkMovementMethod.getInstance());

            SharedPreferences.Editor editor = BIHPreferences.edit();
            editor.putString("version", "1.6.2");
            editor.commit();
        }

//		if (!Utils.isPermissionWriteRequired(MainActivity.this, 0)) {
//			// Your code if permission available
//		}

    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            default:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    switch (permission) {
                        case "android.permission.WRITE_EXTERNAL_STORAGE":
                            Utils.firstTimeAskedPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE");
                            if (PackageManager.PERMISSION_GRANTED == grantResult) {
                                //Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem item = menu.findItem(R.id.menu_item_share);
            ShareActionProvider myShareActionProvider = (ShareActionProvider) item
                    .getActionProvider();
            Intent myIntent = new Intent();
            myIntent.setAction(Intent.ACTION_SEND);
            myIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "בן איש חי  - Ben Ish Chai https://play.google.com/store/apps/details?id=com.eran.benishhai");
            myIntent.setType("text/plain");
            myShareActionProvider.setShareIntent(myIntent);
        } else {
            MenuItem itemSearch = menu.findItem(R.id.menu_search_all_book);
            itemSearch.setVisible(false);
        }
        return true;
    }

    public void SelectYear(View v) {
        Intent intent = new Intent(getApplicationContext(), HumashActivity.class);
        Location location = new Location(null, (String) ((Button) v).getTag(), (String) ((Button) v).getText(), null, null, null, null, -1);
//		intent.putExtra("yearHe", ((Button) v).getText());
//		intent.putExtra("yearEn", (String) ((Button) v).getTag());
        intent.putExtra("location", location);
        startActivity(intent);
    }

    public void LastLocation(View v) {
//		if (!Utils.isPermissionWriteRequired(MainActivity.this, 1, true))
//		{
        SharedPreferences preferences = getSharedPreferences("Locations",
                MODE_PRIVATE);
        String preferencesLocationsJson = preferences.getString(
                "preferencesLocationsJson", null);
        if (preferencesLocationsJson != null) {
            Intent intent = new Intent(getApplicationContext(),
                    WebActivity.class);
            intent.putExtra("requiredFileName", "-1"/* lastLocation */);
            startActivity(intent);
        }
//		}
    }

    public void SelectHistory(View v) {
        //	if (!Utils.isPermissionWriteRequired(MainActivity.this, 2, true)) {
        Intent intent = new Intent(getApplicationContext(),
                GalleryBIH.class);
        startActivityForResult(intent, 1);
        //	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1/* from gallery */) {
            if (data != null && data.getExtras().containsKey("fileName")) {
                String fileName = data.getStringExtra("fileName");
                // Toast.makeText(getApplicationContext(), "1 + "+fileName ,
                // Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),
                        WebActivity.class);
                intent.putExtra("requiredFileName", fileName);
                startActivity(intent);
            }
        }
    }

    public void OpenSettings(View v) {
        Intent intent = new Intent(getApplicationContext(),
                SettingsActivity.class);
        startActivity(intent);
    }

    public void CurrentWeek(View v) {
        String yearEn = (String) ((Button) v).getTag();
        String yearHe = "שנה א";
        String yearFile = "parshotYear1.csv";
        if (yearEn.equals("Year_2")) {
            yearHe = "שנה ב";
            yearFile = "parshotYear2.csv";
        }

        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int dayToShabat = 7 - dayOfWeek;
        c.add(Calendar.DATE, dayToShabat);
        JewishCalendar jc = new JewishCalendar(c);
        boolean LiveInIsrael = defaultSharedPreferences.getBoolean(
                "CBLiveInIsrael", true);
        jc.setInIsrael(LiveInIsrael);


        //System.out.println("getTchilasZmanKidushLevana3Days: "+jc.getTchilasZmanKidushLevana3Days().toString());
        //System.out.println("getTchilasZmanKidushLevana7Days: "+jc.getTchilasZmanKidushLevana7Days().toString());
        //System.out.println("getSofZmanKidushLevanaBetweenMoldos: "+jc.getSofZmanKidushLevanaBetweenMoldos().toString());
        //System.out.println("getSofZmanKidushLevana15Days: "+jc.getSofZmanKidushLevana15Days().toString());

        int parshaIndex = jc.getParshaIndex();
        while (parshaIndex == -1) {
            c.add(Calendar.DATE, 7);
            jc = new JewishCalendar(c);
            parshaIndex = jc.getParshaIndex();
            if (parshaIndex == 0) // Breshit
            {
                parshaIndex = 60;// Vzothabraha
            }
        }

        String parashotMapStr = Utils.ReadTxtFile("files/" + yearFile,
                getApplicationContext());
        Halach halach = null;

        try {
            String[] items = parashotMapStr.split("\n");
            for (int i = 0; i < items.length; i++) {
                String[] parashotStr = items[i].split(",");
                int curParshaIndex = Integer
                        .parseInt(parashotStr[0].toString());
                if (curParshaIndex == parshaIndex) {
                    halach = new Halach(parshaIndex, yearHe, yearEn,
                            parashotStr[1].trim(),// /parshHe
                            parashotStr[2].trim(),// parshEn
                            "",
                            parashotStr[3].trim(),// humashEn
                            "",
                            null);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        if (halach == null) {
            return;
        }

        // פרשיות שלא קימות בשנה ב
        if (yearEn.equals("Year_2") && halach.getParshHe().equals("NONE")) {
            String messageNotExistYear2 = "פרשת השבוע איננה קיימת בשנה ב";
            NotExist(messageNotExistYear2);
            return;
        }

        if (halach.getParshEn().contains("&")) {

            final Halach halachFinal = halach;
            ((TextView) new AlertDialog.Builder(this)
                    .setTitle("פרשיות מחוברות")
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .setMessage("צדיק בחר פרשה")
                    .setPositiveButton(halachFinal.getParshHe().split("&")[0],
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    halachFinal.setParshHe(halachFinal
                                            .getParshHe().split("&")[0]);
                                    halachFinal.setParshEn(halachFinal
                                            .getParshEn().split("&")[0]);
                                    continueToOpenLimud(halachFinal);
                                }
                            })
                    .setNegativeButton(halachFinal.getParshHe().split("&")[1],
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    halachFinal.setParshHe(halachFinal
                                            .getParshHe().split("&")[1]);
                                    halachFinal.setParshEn(halachFinal
                                            .getParshEn().split("&")[1]);
                                    continueToOpenLimud(halachFinal);
                                }
                            }).show().findViewById(android.R.id.message))
                    .setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            continueToOpenLimud(halach);
        }

    }

    public void NotExist(String message) {

        ((TextView) new AlertDialog.Builder(this)
                .setTitle("צדיק לידיעתך")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("הבנתי",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();

                            }
                        }).show().findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void continueToOpenLimud(Halach halach) {
        Intent intentCurrentWeek = new Intent(getApplicationContext(),
                WebActivity.class);
//		intentCurrentWeek.putExtra("yearEn", halach.getYearEn());
//		intentCurrentWeek.putExtra("yearHe", halach.getYearHe());
//		intentCurrentWeek.putExtra("humashEn", halach.getHumashEn());
//		intentCurrentWeek.putExtra("parshHe", halach.getParshHe());
//		intentCurrentWeek.putExtra("parshEn", halach.getParshEn());

        Location location = new Location(null, halach.getYearEn(), halach.getYearHe(), halach.getHumashHe(),
                halach.getHumashEn(), halach.getParshHe(), halach.getParshEn(), -1);
        intentCurrentWeek.putExtra("location", location);
        startActivity(intentCurrentWeek);
    }

    public void OpenHelp(View v) {
        Utils.alertDialogShow(WeakReferenceActivity, getApplicationContext(),
                "עזרה", android.R.drawable.ic_menu_help, "files/help.txt",
                "הבנתי", "זכה את הרבים", shareTextIntent);

    }

    public void OpenAbout(View v) {
        Utils.alertDialogShow(WeakReferenceActivity, getApplicationContext(),
                "אודות", android.R.drawable.ic_menu_info_details,
                "files/about.txt", "אשריכם תזכו למצוות", "זכה את הרבים",
                shareTextIntent);
    }

    public void OpenKeyYears(View v) {
        Intent intent = new Intent(getApplicationContext(),
                KeyYearsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_score:
                scoreInGooglePlay();
                break;
            case R.id.menu_search_all_book:
                searchInAllBook();
                break;
            default:
                break;
        }

        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }

    private void scoreInGooglePlay() {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.eran.benishhai"));
        startActivity(browserIntent);

        String text = "צדיק דרג אותנו  5 כוכבים וטול חלק בזיכוי הרבים.";
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void searchInAllBook() {
        Intent intent = new Intent(getApplicationContext(), SearchInAllBook.class);
        startActivity(intent);
    }
}
