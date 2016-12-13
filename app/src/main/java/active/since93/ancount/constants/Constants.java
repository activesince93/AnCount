package active.since93.ancount.constants;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TypefaceSpan;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by myzupp on 15-02-2016.
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */
public class Constants {

    // Activities status
    public static boolean STATUS_MAIN_ACTIVITY = false;
    public static boolean STATUS_HISTORY_ACTIVITY = false;

    //Broadcast message
    public static final String UPDATE_ACTIVITIES = "active.since93.UPDATE_ACTIVITIES";
    public static final String DATE_VALUE = "date_value";
    public static final String YEAR_VALUE = "YEAR_VALUE";

    /**
     * Get formatted date
     *
     * @param smsTimeInMillis time in milliseconds
     * @return time in string for e.x. TOMORROW, TODAY, 02/12(dd/MM) etc.
     */
    public static String getDateOnly(long smsTimeInMillis) {
        if(DateUtils.isToday(smsTimeInMillis)) {
            return "Today";
        } else if(isYesterday(smsTimeInMillis)) {
            return "Yesterday";
        } else {
            // Any other date
            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(smsTimeInMillis);
            Date date = new Date(smsTimeInMillis);

            String dateTimeFormatString = "dd/MM";
            SimpleDateFormat df = new SimpleDateFormat(dateTimeFormatString, Locale.US);
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        }
    }

    /**
     * Get formatted time
     *
     * @param smsTimeInMillis time in milliseconds
     * @return time in string for e.x. 02:15:55
     */
    public static String getTimeOnly(Context context, long smsTimeInMillis) {
        // Any other date
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMillis);
        Date date = new Date(smsTimeInMillis);

        final String timeFormatString = (!DateFormat.is24HourFormat(context))
                ? "hh:mm:ss aa" : "HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(timeFormatString, Locale.US);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    /**
     * Check if date is yesterday
     * @param date date in millis
     * @return true if date is yesterday
     */
    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static class CustomTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;
        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(tf);
        }
    }

    /**
     * Load ad in screen
     * @param mAdView adView
     */
    public static void loadAd(AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("01655592ABF64891026E34AE31E4D613")
                .addTestDevice("9F5AC4E8105A5E67840DFECF888E4B84")
                .build();
        mAdView.loadAd(adRequest);
    }
}
