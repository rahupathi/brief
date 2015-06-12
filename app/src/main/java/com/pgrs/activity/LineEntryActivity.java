package com.pgrs.activity;

import com.pgrs.dal.LineDAL;
import com.pgrs.models.TLine;
import com.pgrs.myaccount.R;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

public class LineEntryActivity extends Activity {

	private EditText txtLineCode;
	private EditText txtLineName;
	int mLineId = -1;;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_master);

		txtLineCode = (EditText) findViewById(R.id.txtLineCode);
		txtLineName = (EditText) findViewById(R.id.txtLineName);

		TextView lblLineMode = (TextView) findViewById(R.id.lblLineMode);

		Bundle bLine = getIntent().getExtras();

		if (bLine != null) {
			txtLineCode.setText(bLine.getString("LineCode"));
			txtLineName.setText(bLine.getString("LineName"));
			mLineId = Integer.parseInt(bLine.getString("LineId"));

			Toast.makeText(LineEntryActivity.this, bLine.getString("LineId"),
					Toast.LENGTH_LONG).show();
			if (mLineId > 0) {
				lblLineMode.setText("Edit");
			}
		}
		txtLineCode.setRawInputType(Configuration.KEYBOARD_QWERTY);
		this.SetClickEvents();

	}

	private void SetClickEvents() {

		findViewById(R.id.btnLineSave).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if (Validation() == false) {
							if (SaveLine() > 0) {

								String sMessage = "Saved Successfully";

								if (mLineId > 0)
									sMessage = "Updated Successfully";

								Toast.makeText(LineEntryActivity.this,
										sMessage, Toast.LENGTH_LONG).show();

								if (mLineId > 0) {
									finish();
									Intent iLineEntry = new Intent();
									iLineEntry.setClass(LineEntryActivity.this,
											LineListActivity.class);
									startActivity(iLineEntry);
								}
								txtLineCode.setText("");
								txtLineName.setText("");
							}
						}

					}
				});

		findViewById(R.id.btnLineBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
						Intent iLineEntry = new Intent();
						iLineEntry.setClass(LineEntryActivity.this,
								LineListActivity.class);
						startActivity(iLineEntry);
					}
				});

	}

	private long SaveLine() {

		long iReturn = 0;
		LineDAL objLineDAL = new LineDAL(getApplicationContext());
		TLine tLine = new TLine();
		tLine.setLineCode(txtLineCode.getText().toString().trim());
		tLine.setLineName(txtLineName.getText().toString().trim());
		if (mLineId > 0) {
			tLine.setLineId(mLineId);
			iReturn = objLineDAL.Update(tLine);
		} else {
			iReturn = objLineDAL.insert(tLine);
		}

		return iReturn;
	}

	private boolean Validation() {

		txtLineCode.setError(null);
		txtLineName.setError(null);

		boolean cancel = false;
		View focusView = null;
		if (TextUtils.isEmpty(txtLineCode.getText().toString().trim())) {
			txtLineCode
					.setError(Html
							.fromHtml("<font color='white'>This field is required</font>"));
			focusView = txtLineCode;
			cancel = true;
		}

		if (TextUtils.isEmpty(txtLineName.getText().toString().trim())) {
			txtLineName
					.setError(Html
							.fromHtml("<font color='white'>This field is required</font>"));
			focusView = txtLineName;
			cancel = true;
		}

		LineDAL objLineDal = new LineDAL(getApplicationContext());
		TLine tLine = objLineDal.GetLineByLineIdAndLineCode(mLineId,
				txtLineCode.getText().toString());
		if (tLine != null) {
			String sMessage = "<font color='white'>Entered LineCode already given to ["
					+ tLine.getLineName() + "]. You cannot duplicate.</font>";
			txtLineCode.setError(Html.fromHtml(sMessage));
			focusView = txtLineCode;
			cancel = true;
			tLine = null;
		}
		objLineDal = null;

		if (cancel)
			focusView.requestFocus();

		return cancel;
	}

}
