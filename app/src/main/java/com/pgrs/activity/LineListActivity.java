package com.pgrs.activity;

import java.util.ArrayList;
import java.util.Locale;

import com.pgrs.adapter.LineAdapter;
import com.pgrs.dal.LineDAL;
import com.pgrs.models.TLine;
import com.pgrs.myaccount.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;


public class LineListActivity extends Activity {

	private ArrayList<TLine> arraylist = new ArrayList<TLine>();
	private LineAdapter adapter;
	private ListView lView;
	private EditText inputSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_list);

		try {

			lView = ((ListView) findViewById(R.id.lvLineList));

			arraylist = new ArrayList<TLine>();
			LineDAL objLineDAL = new LineDAL(getApplicationContext());
			arraylist = objLineDAL.GetLineList();

			adapter = new LineAdapter(LineListActivity.this, arraylist);
			lView.setAdapter(adapter);

			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TLine tLine = (TLine) lView.getItemAtPosition(position);
					if (tLine != null) {

						Bundle b = new Bundle();

						b.putString("LineId",
								Integer.toString(tLine.getLineId()));
						b.putString("LineName", tLine.getLineName());
						b.putString("LineCode", tLine.getLineCode());

						Intent iLineEdit = new Intent(LineListActivity.this,
								LineEntryActivity.class);
						iLineEdit.putExtras(b);
						startActivity(iLineEdit);
					}
				}
			});

			inputSearch = (EditText) findViewById(R.id.edtLineSearch);

			inputSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					String text = inputSearch.getText().toString()
							.toLowerCase(Locale.getDefault());
					adapter.filter(text);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}
			});

		} catch (Exception ex) {
			Toast.makeText(LineListActivity.this, ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 0, "Back").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(1, 2, 1, "New Line").setIcon(android.R.drawable.ic_input_add);
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
			iLineEntry.setClass(LineListActivity.this, LineEntryActivity.class);
			startActivity(iLineEntry);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
