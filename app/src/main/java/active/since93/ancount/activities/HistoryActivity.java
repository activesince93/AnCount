package active.since93.ancount.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import active.since93.ancount.R;
import active.since93.ancount.adapter.ExpandableListAdapter;
import active.since93.ancount.constants.Constants;
import active.since93.ancount.database.DatabaseHandler;
import active.since93.ancount.model.ExpandableListHeaderItem;
import active.since93.ancount.model.UnlockDataItem;

/**
 * Created by Darshan on 11-Sep-15.
 */
public class HistoryActivity extends AppCompatActivity {

    ExpandableListAdapter expListAdapter;
    ExpandableListView expandableListView;
    List<ExpandableListHeaderItem> listDataHeader;
    HashMap<String, List<ExpandableListHeaderItem>> listDataChild;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        Constants.loadAd(mAdView);

        IntentFilter intentFilter = new IntentFilter(Constants.UPDATE_ACTIVITIES);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateDataReceiver, intentFilter);

        databaseHandler = new DatabaseHandler(this);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        displayListData();

        setActionBarCustomFont();

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem && previousItem != -1)
                    expandableListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void setActionBarCustomFont() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(getString(R.string.title_history).toUpperCase());
        ss.setSpan(new Constants.CustomTypefaceSpan("", font2), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(ss);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        ArrayList<String> yearsCount;
        yearsCount = databaseHandler.getNumberOfYearsFromDB();
        for (int i = 0; i < yearsCount.size(); i++) {
            ArrayList<UnlockDataItem> yearDataArrayList;
            yearDataArrayList = databaseHandler.getDataOfSelectedYear(yearsCount.get(i));
            ArrayList<String> monthsCount;
            monthsCount = databaseHandler.getNumberOfMonthsForSelectedYear(yearsCount.get(i));
            int len = monthsCount.size();
            for (int j = 0; j < len; j++) {
                ExpandableListHeaderItem expandableListHeaderItem = new ExpandableListHeaderItem();
                ArrayList<UnlockDataItem> monthDataArrayList = new ArrayList<>();
                for (int k = 0; k < yearDataArrayList.size(); k++) {
                    if (monthsCount.get(j).equals(yearDataArrayList.get(k).getMonth())) {
                        monthDataArrayList.add(yearDataArrayList.get(k));
                    }
                }
                ArrayList<String> daysCount;
                daysCount = databaseHandler.getNumberOfDaysForSelectedMonthAndYear(monthsCount.get(j), yearsCount.get(i));
                int daysCountInt = daysCount.size();

                ArrayList<ExpandableListHeaderItem> expandableListSubHeaderItemArrayList = new ArrayList<>();
                for (int k = 0; k < daysCountInt; k++) {
                    ExpandableListHeaderItem expandableListSubHeaderItem = new ExpandableListHeaderItem();
                    ArrayList<UnlockDataItem> daysDataArrayList = new ArrayList<>();
                    int monthsDataArrayListSize = monthDataArrayList.size();
                    for (int l = 0; l < monthsDataArrayListSize; l++) {
                        if (daysCount.get(k).equals(monthDataArrayList.get(l).getDate())) {
                            daysDataArrayList.add(monthDataArrayList.get(l));
                        }
                    }
                    String dayDate = daysCount.get(k);
                    String subHeaderTitle;
                    if(DateUtils.isToday(Long.parseLong(daysDataArrayList.get(0).getTime()))) {
                        subHeaderTitle = "Today";
                    } else if(Constants.isYesterday(Long.parseLong(daysDataArrayList.get(0).getTime()))) {
                        subHeaderTitle = "Yesterday";
                    } else {
                        subHeaderTitle = dayDate + getDateExtension(Integer.parseInt(dayDate)) + ", " + monthsCount.get(j);
                    }
                    String subHeaderCount = String.valueOf(daysDataArrayList.size());

                    expandableListSubHeaderItem.setStrHeader(subHeaderTitle);
                    expandableListSubHeaderItem.setCountHeader(subHeaderCount);
                    expandableListSubHeaderItemArrayList.add(expandableListSubHeaderItem);
                }

                String title = monthsCount.get(j) + ", " + yearsCount.get(i);
                String count = String.valueOf(monthDataArrayList.size());
                expandableListHeaderItem.setStrHeader(title);
                expandableListHeaderItem.setCountHeader(count);
                listDataHeader.add(expandableListHeaderItem);
                Collections.reverse(expandableListSubHeaderItemArrayList);
                listDataChild.put(title, expandableListSubHeaderItemArrayList);
            }
        }
    }

    String getDateExtension(int date) {
        if (date == 1 || date == 21 || date == 31) return "st";
        else if (date == 2 || date == 22) return "nd";
        else if (date == 3 || date == 23) return "rd";
        else return "th";
    }

    private final BroadcastReceiver updateDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constants.STATUS_HISTORY_ACTIVITY) {
                displayListData();
            }
        }
    };

    private void displayListData() {
        prepareListData();
        expListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.STATUS_HISTORY_ACTIVITY = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.STATUS_HISTORY_ACTIVITY = false;
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
}