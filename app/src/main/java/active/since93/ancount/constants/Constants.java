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
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMillis);
        Calendar now = Calendar.getInstance();
        Date date = new Date(smsTimeInMillis);

        String dateStr = DateUtils.getRelativeTimeSpanString(smsTimeInMillis
                , now.getTimeInMillis()
                , DateUtils.DAY_IN_MILLIS
                , DateUtils.FORMAT_ABBREV_RELATIVE).toString();

        if(dateStr.equals("Today") || dateStr.equals("Tomorrow") || dateStr.equals("Yesterday")) {
            return dateStr;
        } else {
            // Any other date
            String dateTimeFormatString = "dd/MM";
            SimpleDateFormat df = new SimpleDateFormat(dateTimeFormatString, Locale.US);
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        }
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
