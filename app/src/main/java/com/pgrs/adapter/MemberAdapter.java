package com.pgrs.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.pgrs.models.CMembersByLine;
import com.pgrs.models.TMembers;
import com.pgrs.myaccount.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private ArrayList<CMembersByLine> continentList;
	private ArrayList<CMembersByLine> originalList;

	public MemberAdapter(Activity context, List<CMembersByLine> mMembersList) {
		this.context = context;
		this.continentList = new ArrayList<CMembersByLine>();
		this.continentList.addAll(mMembersList);
		this.originalList = new ArrayList<CMembersByLine>();
		this.originalList.addAll(mMembersList);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<TMembers> countryList = continentList.get(groupPosition)
				.getMembersList();
		return countryList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// LayoutInflater inflater = context.getLayoutInflater();
		TMembers tMember = (TMembers) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.member_expand_detail,
					null);
		}

		TextView txtMemberName = (TextView) convertView
				.findViewById(R.id.txtMemName);
		TextView txtMemberCode = (TextView) convertView
				.findViewById(R.id.txtMemCode);
		TextView txtMemMobile = (TextView) convertView
				.findViewById(R.id.txtMemMobileNo);
		TextView txtMemAddres = (TextView) convertView
				.findViewById(R.id.txtMemAddres);
		// TextView txtGuaranteedBy = (TextView) convertView
		// .findViewById(R.id.txtMemGuaranteedBy);

		txtMemberName.setText(tMember.getMemberName());
		txtMemberCode.setText(tMember.getMemberCode());
		txtMemMobile.setText(tMember.getMobileNumber());
		txtMemAddres.setText(tMember.getAddress());
		// txtGuaranteedBy.setText(tMember.getGuaranteedBy());

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		ArrayList<TMembers> countryList = continentList.get(groupPosition)
				.getMembersList();
		return countryList.size();

	}

	public Object getGroup(int groupPosition) {
		return continentList.get(groupPosition);
	}

	public int getGroupCount() {
		return continentList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		CMembersByLine laptopName = (CMembersByLine) getGroup(groupPosition);

		ImageView image = null;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.member_group_item,
					null);
		}
		TextView item = (TextView) convertView
				.findViewById(R.id.txtMemLineName);
		// item.setTypeface(null, Typeface.BOLD);

		image = (ImageView) convertView.findViewById(R.id.expandableIcon);

		if (groupPosition != 0) {
			int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float
					: android.R.drawable.arrow_down_float;
			image.setImageResource(imageResourceId);

			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.INVISIBLE);
		}

		StringBuilder title = new StringBuilder();
		title.append(laptopName.getName().replace("/", ".").toString());
		title.append(" (");
		title.append(getChildrenCount(groupPosition));
		title.append(")");

		item.setText(title.toString());
		// item.setText(laptopName);

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void filterData(String query) {

		query = query.toLowerCase(Locale.getDefault());
		Log.v("MemberAdapter", String.valueOf(continentList.size()));

		continentList.clear();
		if (query.isEmpty()) {
			continentList.addAll(originalList);
		} else {

			for (CMembersByLine continent : originalList) {

				ArrayList<TMembers> countryList = continent.getMembersList();
				ArrayList<TMembers> newList = new ArrayList<TMembers>();
				for (TMembers tMembers : countryList) {
					if (tMembers.getMemberCode()
							.toLowerCase(Locale.getDefault()).contains(query)
							|| tMembers.getMemberName()
									.toLowerCase(Locale.getDefault())
									.contains(query)
							|| tMembers.getMobileNumber()
									.toLowerCase(Locale.getDefault())
									.contains(query)
							|| tMembers.getAddress()
									.toLowerCase(Locale.getDefault())
									.contains(query)) {
						newList.add(tMembers);
					}
				}
				if (newList.size() > 0) {
					CMembersByLine nContinent = new CMembersByLine(
							continent.getName(), newList);
					continentList.add(nContinent);
				}
			}
		}

		Log.v("MyListAdapter", String.valueOf(continentList.size()));
		notifyDataSetChanged();

	}

}
