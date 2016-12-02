package active.since93.ancount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

import active.since93.ancount.R;
import active.since93.ancount.custom.CustomTextView;
import active.since93.ancount.model.ExpandableListHeaderItem;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<ExpandableListHeaderItem> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<ExpandableListHeaderItem>> _listDataChild;

	public ExpandableListAdapter(Context context, List<ExpandableListHeaderItem> listDataHeader, HashMap<String, List<ExpandableListHeaderItem>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public ExpandableListHeaderItem getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition).getStrHeader()).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ExpandableListHeaderItem expandableListHeaderItem = getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_items_raw, null);
		}

		CustomTextView txtListChild = (CustomTextView) convertView.findViewById(R.id.txtListItem);
		CustomTextView txtListChildCount = (CustomTextView) convertView.findViewById(R.id.txtListItemCount);
		String titles[] = expandableListHeaderItem.getStrHeader().split(",");
		String str = titles[0] + ", " + getMonthName(titles[1].trim());
		txtListChild.setText(str);
		txtListChildCount.setText(expandableListHeaderItem.getCountHeader());
		return convertView;
	}

	@Override
	public ExpandableListHeaderItem getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition).getStrHeader()).size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ExpandableListHeaderItem expandableListHeaderItem = getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_header_raw, null);
		}

		CustomTextView lblListHeader = (CustomTextView) convertView.findViewById(R.id.txtListHeader);
		CustomTextView lblListCount = (CustomTextView) convertView.findViewById(R.id.txtListHeaderCount);

		String titles[] = expandableListHeaderItem.getStrHeader().split(",");
		String str = getMonthName(titles[0]) + ", " + titles[1];

		lblListHeader.setText(str);
		lblListCount.setText(expandableListHeaderItem.getCountHeader());

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
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
