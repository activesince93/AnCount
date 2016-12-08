package active.since93.ancount.constants;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.TypefaceSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by myzupp on 15-02-2016.
 */
public class Constants {

    // Activities status
    public static boolean STATUS_MAIN_ACTIVITY = false;
    public static boolean STATUS_HISTORY_ACTIVITY = false;

    //Broadcast message
    public static final String UPDATE_ACTIVITIES = "active.since93.UPDATE_ACTIVITIES";

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
}
