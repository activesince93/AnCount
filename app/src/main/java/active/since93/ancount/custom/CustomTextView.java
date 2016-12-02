package active.since93.ancount.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Darshan on 12-Nov-15.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        String typeface = "JosefinSans-Bold.ttf";
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typeface);
        setTypeface(myTypeface);
    }
}
