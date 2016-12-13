package active.since93.ancount.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import active.since93.ancount.R;
import active.since93.ancount.constants.Constants;
import active.since93.ancount.custom.CustomTextView;
import active.since93.ancount.database.DatabaseHandler;
import active.since93.ancount.graph.MyMarkerView;
import active.since93.ancount.model.StringAndInteger;
import active.since93.ancount.model.UnlockDataItem;

public class MainActivity extends AppCompatActivity {

    private CustomTextView todayCount, averageCountTextView, lastWeekCount, yesterdayCount;
    DatabaseHandler databaseHandler;
    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        Constants.loadAd(mAdView);

        // Receive broadcast message
        IntentFilter intentFilter = new IntentFilter(Constants.UPDATE_ACTIVITIES);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateDataReceiver, intentFilter);

        todayCount = (CustomTextView) findViewById(R.id.txtCount);
        averageCountTextView = (CustomTextView) findViewById(R.id.aversgeCount);
        lastWeekCount = (CustomTextView) findViewById(R.id.lastWeekCount);
        yesterdayCount = (CustomTextView) findViewById(R.id.yesterdayCount);
        mChart = (BarChart) findViewById(R.id.lastWeekChart);
        databaseHandler = new DatabaseHandler(this);

        setActionBarCustomFont();
        updateData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChartProperties() {
        mChart.setDescription("");
        setChartData();
        mChart.setMaxVisibleValueCount(60);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setDrawGridBackground(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mChart.setMarkerView(mv);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Light.ttf");
        xAxis.setTypeface(font);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTypeface(font);

        YAxis yAxis1 = mChart.getAxisRight();
        yAxis1.setTextColor(Color.WHITE);
        yAxis1.setTypeface(font);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.animateY(0);
        mChart.getLegend().setEnabled(false);
    }

    private void setActionBarCustomFont() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(getString(R.string.app_name_caps));
        ss.setSpan(new Constants.CustomTypefaceSpan("", font2), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(ss);
        }
    }

    private void setChartData() {
        int todayCountInt = 0, yesterdayCountInt = 0, lastWeekCountInt = 0;

        ArrayList<UnlockDataItem> unlockDataItemArrayList;
        ArrayList<BarEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        // last 7 days
        unlockDataItemArrayList = databaseHandler.getPreviousData(6, 0, 0);
        ArrayList<StringAndInteger> daysAndCountArrayList = getDaysAndCountArrayList(unlockDataItemArrayList);

        for(int i = 0; i < daysAndCountArrayList.size(); i++) {
            xVals.add(daysAndCountArrayList.get(i).getDay());
            yVals.add(new BarEntry(daysAndCountArrayList.get(i).getCount(), i));
        }

        BarDataSet set1 = new BarDataSet(yVals, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(false);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        mChart.setData(data);
        mChart.invalidate();

        for(UnlockDataItem unlockDataItem : unlockDataItemArrayList) {
            if(Constants.getDateOnly(Long.parseLong(unlockDataItem.getTime())).equals("Today")) ++todayCountInt;
            else if(Constants.getDateOnly(Long.parseLong(unlockDataItem.getTime())).equals("Yesterday")) ++yesterdayCountInt;
            ++lastWeekCountInt;
        }

        yesterdayCount.setText(String.valueOf(yesterdayCountInt));
        todayCount.setText(String.valueOf(todayCountInt));
        lastWeekCount.setText(String.valueOf(lastWeekCountInt));
    }

    private ArrayList<StringAndInteger> getDaysAndCountArrayList(ArrayList<UnlockDataItem> unlockDataItemArrayList) {
        ArrayList<StringAndInteger> daysAndCountArrayList = new ArrayList<>();

        LinkedHashSet<String> dayDateSet = new LinkedHashSet<>();
        for(UnlockDataItem unlockDataItem : unlockDataItemArrayList) {
            dayDateSet.add(unlockDataItem.getDate());
        }

        for(String dayDate : dayDateSet) {
            String relativeDay = "";
            int count = 0;
            for (UnlockDataItem unlockDataItem : unlockDataItemArrayList) {
                if(dayDate.equals(unlockDataItem.getDate())) {
                    ++count;
                    relativeDay = Constants.getDateOnly(Long.parseLong(unlockDataItem.getTime()));
                }
            }
            daysAndCountArrayList.add(new StringAndInteger(relativeDay, count));
        }
        return daysAndCountArrayList;
    }

    public static void backupDatabase() throws IOException {
        //Open your local db as the input stream
        String inFileName = "/data/data/active.since93.unlockcounter/databases/unlock_database";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory() + "/unlock_database";
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.STATUS_MAIN_ACTIVITY = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.STATUS_MAIN_ACTIVITY = false;
    }

    private final BroadcastReceiver updateDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constants.STATUS_MAIN_ACTIVITY) {
                updateData();
            }
        }
    };

    // Update displaying data
    private void updateData() {
        setChartProperties();

        int totalEntries = databaseHandler.getTotalUnlocksCount();
        int totalDays = databaseHandler.getNumberOfTotalDays();
        if (totalDays != 0 && totalEntries != 0)
            averageCountTextView.setText(String.valueOf(totalEntries / totalDays));
    }
}