package active.since93.ancount.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import active.since93.ancount.R;
import active.since93.ancount.constants.Constants;
import active.since93.ancount.custom.CustomTextView;
import active.since93.ancount.model.UnlockDataItem;

/**
 * Created by myzupp on 13-12-2016.
 *
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */

public class SingleDayItemAdapter extends RecyclerView.Adapter<SingleDayItemAdapter.CustomViewHolder> {

    private ArrayList<UnlockDataItem> unlockDataItemArrayList = new ArrayList<>();
    private Context context;

    public SingleDayItemAdapter(Context context, ArrayList<UnlockDataItem> unlockDataItemArrayList) {
        this.context = context;
        this.unlockDataItemArrayList = unlockDataItemArrayList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_day_activity_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        UnlockDataItem unlockDataItem = unlockDataItemArrayList.get(position);
        holder.txtTime.setText(Constants.getTimeOnly(context, Long.parseLong(unlockDataItem.getTime())));
    }

    @Override
    public int getItemCount() {
        return unlockDataItemArrayList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtTime;
        public CustomViewHolder(View itemView) {
            super(itemView);
            txtTime = (CustomTextView) itemView.findViewById(R.id.txtTime);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans-Regular.ttf");
            txtTime.setTypeface(font);
        }
    }
}
