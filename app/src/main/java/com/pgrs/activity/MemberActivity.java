package com.pgrs.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

import com.pgrs.adapter.MemberAdapter;
import com.pgrs.dal.LineDAL;
import com.pgrs.dal.MembersDAL;
import com.pgrs.models.CMembersByLine;
import com.pgrs.models.TLine;
import com.pgrs.models.TMembers;
import com.pgrs.myaccount.R;

public class MemberActivity extends Activity implements
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private ExpandableListView expListView;
	private SearchView search;
	private MemberAdapter expListAdapter;

	private ArrayList<CMembersByLine> continentList = new ArrayList<CMembersByLine>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_expand_list);

		LoadSearch();

		LineDAL objLineDAL = new LineDAL(getApplicationContext());
		ArrayList<TLine> lstLine = objLineDAL.GetLineList();
		if (lstLine.size() > 0)
			LoadGrid();
		else {
			Toast.makeText(getBaseContext(), "Enter Line to add New Member",
					Toast.LENGTH_LONG).show();
			Intent iLineEntry = new Intent();
			iLineEntry.setClass(MemberActivity.this, LineEntryActivity.class);
			startActivity(iLineEntry);
			this.finish();
		}

	}

	private void LoadSearch() {

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search = (SearchView) findViewById(R.id.memExpSearch);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setIconifiedByDefault(false);
		search.setOnQueryTextListener(this);
		search.setOnCloseListener(this);
		int id = search.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.WHITE);
		search.setQueryHint(Html
				.fromHtml("<font color = #FFFF00>Search</font>"));

	}

	private void expandAll() {
		int count = expListAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			expListView.expandGroup(i);
		}
	}

	public boolean onQueryTextChange(String query) {
		expListAdapter.filterData(query);
		expandAll();
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		expListAdapter.filterData(query);
		expandAll();
		return false;
	}

	@Override
	public boolean onClose() {
		expListAdapter.filterData("");
		expandAll();
		return false;
	}

	public void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
				- getDipsFromPixel(5));
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	protected void LoadGrid() {

		try {

			ArrayList<TMembers> arrayMembers = new ArrayList<TMembers>();
			ArrayList<TLine> arrLine = new ArrayList<TLine>();

			LineDAL objLineDAL = new LineDAL(getApplicationContext());
			arrLine = objLineDAL.GetLineList();

			if (arrLine.size() > 0) {
				MembersDAL objMembersDAL = new MembersDAL(
						getApplicationContext());

				for (int iLineIndex = 0; iLineIndex < arrLine.size(); iLineIndex++) {

					arrayMembers = null;
					arrayMembers = objMembersDAL.GetMembersListByLineId(arrLine
							.get(iLineIndex).getLineId());

					String sLine = arrLine.get(iLineIndex).getLineCode()
							+ "-"
							+ arrLine.get(iLineIndex).getLineName()
									.toUpperCase(Locale.getDefault());

					CMembersByLine cMembersByLine = new CMembersByLine(sLine,
							arrayMembers);

					continentList.add(cMembersByLine);
				}
				objMembersDAL = null;

			}
		} catch (Exception ex) {
			Log.w("Member", ex.getMessage());
		}

		expListView = (ExpandableListView) findViewById(R.id.lvMemberList);
		expListView.setGroupIndicator(null);
		expListAdapter = new MemberAdapter(MemberActivity.this, continentList);
		expListView.setAdapter(expListAdapter);
		// setGroupIndicatorToRight();
		if (expListAdapter.getChildrenCount(0) >= 1) {
			expListView.expandGroup(0);
		}

		expListView
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						if (groupPosition == 0) {
							return true;
						} else {
							return false;
						}
					}
				});

		expListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				final TMembers selected = (TMembers) expListAdapter.getChild(
						groupPosition, childPosition);

				Bundle b = new Bundle();

				b.putString("MemberId",
						Integer.toString(selected.getMemberId()));

				Intent iMemberEdit = new Intent(MemberActivity.this,
						MemberEntryActivity.class);
				iMemberEdit.putExtras(b);
				startActivity(iMemberEdit);
				finish();
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 0, "Back").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(1, 2, 1, "New Member")
				.setIcon(android.R.drawable.ic_input_add);
		return true;
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case 1:
			finish();
			return true;

		case 2:

			Intent iLineEntry = new Intent();
			iLineEntry.setClass(MemberActivity.this, MemberEntryActivity.class);
			startActivity(iLineEntry);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}