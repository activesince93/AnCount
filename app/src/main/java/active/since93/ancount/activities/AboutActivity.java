package active.since93.ancount.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import active.since93.ancount.R;
import active.since93.ancount.constants.Constants;

/**
 * Created by Darshan on 11-Sep-15.
 */
public class AboutActivity extends AppCompatActivity {

    private TextView txtDetails, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);

        txtDetails = (TextView) findViewById(R.id.txtDetail);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        Typeface fontLight = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Light.ttf");
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        txtDetails.setTypeface(fontRegular);
        txtTitle.setTypeface(fontLight);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        Constants.loadAd(mAdView);
        setActionBarCustomFont();
    }

    private void setActionBarCustomFont() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(getString(R.string.title_about).toUpperCase());
        ss.setSpan(new Constants.CustomTypefaceSpan("", font2), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(ss);
        }
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