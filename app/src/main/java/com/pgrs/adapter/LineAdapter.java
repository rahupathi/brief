package com.pgrs.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pgrs.models.TLine;

import com.pgrs.myaccount.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LineAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<TLine> Linelist = null;
	private ArrayList<TLine> arraylist;

	public LineAdapter(Context context,
			List<TLine> LineDetaillist) {
		mContext = context;
		this.Linelist = LineDetaillist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<TLine>();
		this.arraylist.addAll(Linelist);
	}


	public class ViewHolder {
        TextView txtLineCode;
        TextView txtLineName;
        TextView txtListLineCreatedDate;
    }

	@Override
	public int getCount() {
		return Linelist.size();
	}

	@Override
	public TLine getItem(int position) {
		return Linelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			
			view = inflater.inflate(R.layout.line_detail, null);
			holder.txtLineName = (TextView) view.findViewById(R.id.txtListLineName);
			holder.txtLineCode = (TextView) view.findViewById(R.id.txtListLineCode);
			holder.txtListLineCreatedDate = (TextView) view.findViewById(R.id.txtListLineCreatedDate);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.txtLineCode.setText(Linelist.get(position).getLineCode());
	    holder.txtLineName.setText(Linelist.get(position).getLineName());
	    holder.txtListLineCreatedDate.setText(Linelist.get(position).getCreatedDate());
	    
	    
	    return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		Linelist.clear();
		if (charText.length() == 0) {
			Linelist.addAll(arraylist);
		} else {
			for (TLine wp : arraylist) {
				if (wp.getLineName().toLowerCase(Locale.getDefault())
						.contains(charText)
						||
						wp.getLineCode().toLowerCase(Locale.getDefault())
						.contains(charText)
						) {
					Linelist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
