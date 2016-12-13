package active.since93.ancount.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.MenuItem;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import active.since93.ancount.R;
import active.since93.ancount.adapter.SingleDayItemAdapter;
import active.since93.ancount.constants.Constants;
import active.since93.ancount.database.DatabaseHandler;
import active.since93.ancount.model.UnlockDataItem;

/**
 * Created by myzupp on 13-12-2016.
 *
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */

public class SingleDayActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUnlockItem;
    private ArrayList<UnlockDataItem> unlockDataItemArrayList = new ArrayList<>();
    private SingleDayItemAdapter adapter;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_day);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        Constants.loadAd(mAdView);

        String yearStr = getIntent().getStringExtra(Constants.YEAR_VALUE);
        String dateStr = getIntent().getStringExtra(Constants.DATE_VALUE);
        setActionBarCustomFont(dateStr);

        databaseHandler = new DatabaseHandler(SingleDayActivity.this);
        recyclerViewUnlockItem = (RecyclerView) findViewById(R.id.recyclerViewUnlockItem);
        recyclerViewUnlockItem.setLayoutManager(new LinearLayoutManager(this));

        String date;
        if(dateStr.equals("Today")) {
            Calendar cal = Calendar.getInstance();
            date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        } else if(dateStr.equals("Yesterday")) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        } else {
            date = dateStr.substring(0, dateStr.indexOf(",") - 2);
        }
        String month = yearStr.substring(0, yearStr.indexOf(","));
        String year = yearStr.substring(yearStr.indexOf(",") + 1).trim();
        unlockDataItemArrayList = databaseHandler.getDataForSelectedDate(date, month, year);
        Collections.reverse(unlockDataItemArrayList);
        adapter = new SingleDayItemAdapter(SingleDayActivity.this, unlockDataItemArrayList);
        recyclerViewUnlockItem.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarCustomFont(String str) {
        String titles[] = str.split(",");
        String finalStr = (titles.length > 1) ? titles[0] + ", " + getMonthName(titles[1].trim()) : titles[0];

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(finalStr.toUpperCase());
        ss.setSpan(new Constants.CustomTypefaceSpan("", font2), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(ss);
        }
    }

    String getMonthName(String str) {
        int value = Integer.parseInt(str);
        if(value == 0) return "January";
        else if(value == 1) return "February";
        else if(value == 2) return "March";
        else if(value == 3) return "April";
        else if(value == 4) return "May";
        else if(value == 5) return "June";
        else if(value == 6) return "July";
        else if(value == 7) return "August";
        else if(value == 8) return "September";
        else if(value == 9) return "October";
        else if(value == 10) return "November";
        else return "December";
    }
}
