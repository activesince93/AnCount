package active.since93.ancount.activities;

import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import active.since93.ancount.R;
import active.since93.ancount.constants.Constants;
import active.since93.ancount.database.DatabaseHandler;
import active.since93.ancount.graph.MyMarkerView;
import active.since93.ancount.model.UnlockDataItem;

/**
 * Created by Darshan on 11-Sep-15.
 */
public class AnalysisActivity extends AppCompatActivity implements View.OnClickListener {

    private BarChart mChart;
    DatabaseHandler databaseHandler;
    private Button last7Days, lastMonth, last3Months, last6Months, lastYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_analysis);

        loadAd();

        // TODO Receive broadcast message
        IntentFilter intentFilter = new IntentFilter(Constants.UPDATE_ACTIVITIES);
        LocalBroadcastManager.getInstance(this).registerReceiver(null, intentFilter);

        databaseHandler = new DatabaseHandler(this);
        mChart = (BarChart) findViewById(R.id.chart1);
        last7Days = (Button) findViewById(R.id.last7Days);
        lastMonth = (Button) findViewById(R.id.lastMonth);
        last3Months = (Button) findViewById(R.id.last3Months);
        last6Months = (Button) findViewById(R.id.last6Months);
        lastYear = (Button) findViewById(R.id.lastYear);

        last7Days.setOnClickListener(this);
        lastMonth.setOnClickListener(this);
        last3Months.setOnClickListener(this);
        last6Months.setOnClickListener(this);
        lastYear.setOnClickListener(this);

        setChartData(R.id.last7Days);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
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
        mChart.animateY(2500);
        mChart.getLegend().setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void setChartData(int id) {
        ArrayList<UnlockDataItem> unlockDataItemArrayList = new ArrayList<UnlockDataItem>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        if (id == R.id.last7Days) {
            // last 7 days
            unlockDataItemArrayList = databaseHandler.getPreviousData(7, 0, 0);
            xVals = databaseHandler.getLast7daysNames();
            for (int i = 0; i < xVals.size(); i++) {
                int count = 0;
                for (int j = 0; j < unlockDataItemArrayList.size(); j++) {
                    if (xVals.get(i).equalsIgnoreCase(unlockDataItemArrayList.get(j).getDayName())) {
                        count += 1;
                    }
                }
                yVals.add(new BarEntry(count, i));
            }
        } else if (id == R.id.lastMonth) {
            // last month
            unlockDataItemArrayList = databaseHandler.getPreviousData(0, 1, 0);
            for (int i = 0; i < unlockDataItemArrayList.size(); i++) {
                xVals.add(unlockDataItemArrayList.get(i).getDayName());
            }
            for (int i = 0; i < xVals.size(); i++) {
                int count = 0;
                for (int j = 0; j < unlockDataItemArrayList.size(); j++) {
                    if (xVals.get(i).equalsIgnoreCase(unlockDataItemArrayList.get(j).getDayName())) {
                        count += 1;
                    }
                }
                yVals.add(new BarEntry(count, i));
            }
        } else if (id == R.id.last3Months) {
            // last 3 months
            unlockDataItemArrayList = databaseHandler.getPreviousData(0, 3, 0);
        } else if (id == R.id.last6Months) {
            // last 6 months
            unlockDataItemArrayList = databaseHandler.getPreviousData(0, 6, 0);
        } else if (id == R.id.lastYear) {
            // last year
            unlockDataItemArrayList = databaseHandler.getPreviousData(0, 0, 1);
        }


        BarDataSet set1 = new BarDataSet(yVals, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(false);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }

    private void loadAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("01655592ABF64891026E34AE31E4D613")
                .addTestDevice("9F5AC4E8105A5E67840DFECF888E4B84")
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        last7Days.setEnabled(true);
        lastMonth.setEnabled(true);
        last3Months.setEnabled(true);
        last6Months.setEnabled(true);
        lastYear.setEnabled(true);
        switch (id) {
            case R.id.last7Days:
                last7Days.setEnabled(false);
                setChartData(id);
                mChart.animateY(1000);
                break;
            case R.id.lastMonth:
                lastMonth.setEnabled(false);
                setChartData(id);
                mChart.animateY(1000);
                break;
            case R.id.last3Months:
                last3Months.setEnabled(false);
                setChartData(id);
                mChart.animateY(1000);
                break;
            case R.id.last6Months:
                last6Months.setEnabled(false);
                setChartData(id);
                mChart.animateY(1000);
                break;
            case R.id.lastYear:
                lastYear.setEnabled(false);
                setChartData(id);
                mChart.animateY(1000);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.STATUS_ANALYSIS_ACTIVITY = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.STATUS_ANALYSIS_ACTIVITY = false;
    }
}