package com.pgrs.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pgrs.models.CMemberCollection;
import com.pgrs.models.TLine;
import com.pgrs.models.TMembers;

import com.pgrs.myaccount.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberCollectionAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<CMemberCollection> Memberlist = null;
	private ArrayList<CMemberCollection> arraylist;

	public MemberCollectionAdapter(Context context, List<CMemberCollection> MemberDetaillist) {
		mContext = context;
		this.Memberlist = MemberDetaillist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<CMemberCollection>();
		this.arraylist.addAll(Memberlist);
	}

	public class ViewHolder {
		TextView txtMemberCode;
		TextView txtMemberName;
		TextView txtMemberMobile;
		TextView txtMemberAddress;
		TextView txtAmount;
	}

	@Override
	public int getCount() {
		return Memberlist.size();
	}

	@Override
	public TMembers getItem(int position) {
		return Memberlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.mem_collection_detail, null);
			holder.txtMemberCode = (TextView) view
					.findViewById(R.id.txtColMemCode);
			holder.txtMemberName = (TextView) view
					.findViewById(R.id.txtColMemName);
			holder.txtMemberMobile = (TextView) view
					.findViewById(R.id.txtColMemMobileNo);
			holder.txtMemberAddress = (TextView) view
					.findViewById(R.id.txtColMemAddres);
			holder.txtAmount = (TextView) view
					.findViewById(R.id.txtColAmount);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtMemberCode.setText(Memberlist.get(position).getMemberCode());
		holder.txtMemberName.setText(Memberlist.get(position).getMemberName());
		holder.txtMemberMobile.setText(Memberlist.get(position)
				.getMobileNumber());
		holder.txtMemberAddress.setText(Memberlist.get(position).getAddress());
		holder.txtAmount.setText(Memberlist.get(position).getAmount().toString());

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		Memberlist.clear();
		if (charText.length() == 0) {
			Memberlist.addAll(arraylist);
		} else {
			for (CMemberCollection wp : arraylist) {
				if (wp.getMemberCode().toLowerCase(Locale.getDefault())
						.contains(charText)
						|| wp.getMemberName().toLowerCase(Locale.getDefault())
								.contains(charText)
						|| wp.getMobileNumber()
								.toLowerCase(Locale.getDefault())
								.contains(charText)
						|| wp.getAddress().toLowerCase(Locale.getDefault())
								.contains(charText)) {
					Memberlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
