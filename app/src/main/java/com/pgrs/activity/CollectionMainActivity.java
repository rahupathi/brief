package com.pgrs.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.TextUtils;

import com.pgrs.dal.LineDAL;
import com.pgrs.dal.MembersDAL;
import com.pgrs.models.TLine;
import com.pgrs.myaccount.R;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("ValidFragment")
public class CollectionMainActivity extends FragmentActivity {

	int mLineId = -1;;

	EditText mEdit;
	Spinner cmbLineName;
	ArrayAdapter<String> dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_main);
	
		// Load Controls
		mEdit = (EditText) findViewById(R.id.editText1);
		cmbLineName = (Spinner) findViewById(R.id.collcmbLineName);
		addItemsOnToLineSpinner();
		setActionToControls();
		addListenerOnSpinnerItemSelection();
	}

	private void setActionToControls() {
		findViewById(R.id.btnColGo).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						mEdit.setError("");
						String sRequired = "<font color='white'>Select Collection Date</font>";
						if (TextUtils
								.isEmpty(mEdit.getText().toString().trim())) {
							mEdit.setError(Html.fromHtml(sRequired));
							mEdit.requestFocus();
							return;
						} else {
							Intent iCollectionEntry = new Intent(
									CollectionMainActivity.this,
									CollectionListActivity.class);
							Bundle b = new Bundle();
							b.putString("CollectionDate", mEdit.getText()
									.toString());
							b.putString("LineId", Integer.toString(mLineId));
							iCollectionEntry.putExtras(b);
							startActivity(iCollectionEntry);
							finish();
						}

					}
				});

		findViewById(R.id.btnColBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
						Intent iLineEntry = new Intent();
						iLineEntry.setClass(CollectionMainActivity.this,
								MainActivity.class);
						startActivity(iLineEntry);
					}
				});
	}

	public void selectDate(View view) {
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getSupportFragmentManager(), "DatePicker");
	}

	public void populateSetDate(int year, int month, int day) {
		mEdit.setText(day + "/" + month + "/" + year);
	}

	public void addItemsOnToLineSpinner() {
		LineDAL oLineDal = new LineDAL(getApplicationContext());

		ArrayList<TLine> lstLines = oLineDal.GetLineList();
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < lstLines.size(); i++) {

			String sLine = lstLines.get(i).getLineCode()
					+ "-"
					+ lstLines.get(i).getLineName()
							.toUpperCase(Locale.getDefault());

			list.add(sLine);
		}

		dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
				list);
		dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		cmbLineName.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {

		cmbLineName = (Spinner) findViewById(R.id.collcmbLineName);
		cmbLineName
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			String[] separated = parent.getItemAtPosition(pos).toString()
					.split("-");
			if (separated.length > 0) {
				String sLineCode = separated[0].toString();
				if (sLineCode.length() > 0) {
					LineDAL oLineDAL = new LineDAL(getApplicationContext());
					TLine tLine = oLineDAL.GetLineByLineIdAndLineCode(-1,
							sLineCode);
					if (tLine != null) {
						mLineId = tLine.getLineId();
						Toast.makeText(CollectionMainActivity.this,
								Integer.toString(tLine.getLineId()),
								Toast.LENGTH_LONG).show();
					}
				}
			}
			// Toast.makeText(
			// parent.getContext(),
			// "OnItemSelectedListener : "
			// + parent.getItemAtPosition(pos).toString(),
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}

	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}
	}

}
