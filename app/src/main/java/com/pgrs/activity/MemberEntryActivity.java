package com.pgrs.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pgrs.dal.LineDAL;
import com.pgrs.dal.MembersDAL;
import com.pgrs.models.TLine;
import com.pgrs.models.TMembers;
import com.pgrs.myaccount.R;

@SuppressLint("DefaultLocale")
public class MemberEntryActivity extends Activity {

	private static final int CROP_FROM_CAMERA = 0;
	private static final int PICK_FROM_CAMERA = 1;
	Spinner cmbLineName;
	EditText txtMemberCode;
	EditText txtMemberName;
	EditText txtMemberAddress;
	EditText txtMemberMobile;
	EditText txtMemberGuaranteed;
	EditText txtMemEntryAltMobileNo;
	int mLineId = -1;
	int mMemberId = -1;
	ArrayAdapter<String> dataAdapter = null;
	ImageView mPhotoImageView;
	private Uri mImageCaptureUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_master);

		addItemsOnToLineSpinner();

		addListenerOnSpinnerItemSelection();

		loadDetail();

		setClickEvents();

	}

	private void ClearText() {
		txtMemberCode.setText("");
		txtMemberName.setText("");
		txtMemberGuaranteed.setText("");
		txtMemberAddress.setText("");
		txtMemberMobile.setText("");
		txtMemEntryAltMobileNo.setText("");
		mPhotoImageView.setImageBitmap(null);
	}

	public void addItemsOnToLineSpinner() {

		cmbLineName = (Spinner) findViewById(R.id.memEntrycmbLineName);

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

	private void loadDetail() {

		txtMemberCode = (EditText) findViewById(R.id.txtMemEntryCode);
		txtMemberName = (EditText) findViewById(R.id.txtMemEntryName);
		txtMemberMobile = (EditText) findViewById(R.id.txtMemEntryMobileNo);
		txtMemberAddress = (EditText) findViewById(R.id.txtMemEntryAddress);
		txtMemberGuaranteed = (EditText) findViewById(R.id.txtMemEntryGuaranteed);
		txtMemEntryAltMobileNo = (EditText) findViewById(R.id.txtMemEntryAltMobileNo);
		mPhotoImageView = (ImageView) findViewById(R.id.imgMemberPhoto);

		// Edit Mode
		Bundle bBundle = getIntent().getExtras();
		if (bBundle != null) {
			mMemberId = Integer.parseInt(bBundle.get("MemberId").toString());

			MembersDAL objMembersDAL = new MembersDAL(getApplicationContext());

			TMembers tMember = objMembersDAL
					.GetMemberDetailByMemberId(mMemberId);
			if (tMember != null) {
				txtMemberCode.setText(tMember.getMemberCode());
				txtMemberName.setText(tMember.getMemberName());
				txtMemberMobile.setText(tMember.getMobileNumber());
				txtMemberAddress.setText(tMember.getAddress());
				txtMemEntryAltMobileNo.setText(tMember.getAlternateNo());
				txtMemberGuaranteed.setText(tMember.getGuaranteedBy());

				if (tMember.getPhoto() != null) {
					mPhotoImageView.setImageBitmap(BitmapFactory
							.decodeByteArray(tMember.getPhoto(), 0,
									tMember.getPhoto().length));
				}

				if (tMember.getLineId() > 0) {
					LineDAL oLineDal = new LineDAL(getApplicationContext());
					TLine tLine = oLineDal.GetLineByLineId(tMember.getLineId());
					if (tLine != null) {
						String sLine = tLine.getLineCode()
								+ "-"
								+ tLine.getLineName().toUpperCase(
										Locale.getDefault());

						int spinnerPosition = dataAdapter.getPosition(sLine);
						cmbLineName.setSelection(spinnerPosition);
					}
				}

			}
		}

	}

	private void setClickEvents() {

		findViewById(R.id.btnMemberSave).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						if (validation() == false) {
							saveMember();
							if (mMemberId > 0) {
								finish();
								Intent iMemberEntry = new Intent();
								iMemberEntry.setClass(MemberEntryActivity.this,
										MemberActivity.class);
								startActivity(iMemberEntry);
							} else {
								ClearText();
								txtMemberCode.requestFocus();
							}
						}

					}
				});

		findViewById(R.id.btnMemberBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
						Intent iMemberEntry = new Intent();
						iMemberEntry.setClass(MemberEntryActivity.this,
								MemberActivity.class);
						startActivity(iMemberEntry);

					}
				});
		findViewById(R.id.imgMemberPhoto).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						doTakePhotoAction();
					}
				});

	}

	private boolean validation() {

		txtMemberName.setError(null);
		txtMemberCode.setError(null);
		txtMemberAddress.setError(null);
		txtMemberGuaranteed.setError(null);
		txtMemberMobile.setError(null);

		String sRequired = "<font color='white'>This field is required</font>";

		boolean cancel = false;
		View focusView = null;

		if (mLineId == -1) {
			Toast.makeText(MemberEntryActivity.this, "Select Line",
					Toast.LENGTH_LONG).show();

			focusView = txtMemberCode;
			cancel = true;
		}
		if (TextUtils.isEmpty(txtMemberCode.getText().toString().trim())) {
			txtMemberCode.setError(Html.fromHtml(sRequired));
			focusView = txtMemberCode;
			cancel = true;
		}

		if (TextUtils.isEmpty(txtMemberName.getText().toString().trim())) {
			txtMemberName.setError(Html.fromHtml(sRequired));
			focusView = txtMemberName;
			cancel = true;
		}
		if (TextUtils.isEmpty(txtMemberMobile.getText().toString().trim())) {
			txtMemberMobile.setError(Html.fromHtml(sRequired));
			focusView = txtMemberMobile;
			cancel = true;
		}
		if (TextUtils.isEmpty(txtMemberGuaranteed.getText().toString().trim())) {
			txtMemberGuaranteed.setError(Html.fromHtml(sRequired));
			focusView = txtMemberGuaranteed;
			cancel = true;
		}
		if (TextUtils.isEmpty(txtMemberAddress.getText().toString().trim())) {
			txtMemberAddress.setError(Html.fromHtml(sRequired));
			focusView = txtMemberAddress;
			cancel = true;
		}

		MembersDAL objMembersDAL = new MembersDAL(getApplicationContext());

		TMembers tMembers = objMembersDAL.GetMemberByMemberIdAndCode(mMemberId,
				txtMemberCode.getText().toString());

		if (tMembers != null) {
			String sMessage = "<font color='white'>Entered Code already given to ["
					+ tMembers.getMemberName()
					+ "]. You cannot duplicate.</font>";

			txtMemberCode.setError(Html.fromHtml(sMessage));

			focusView = txtMemberCode;
			cancel = true;
			tMembers = null;
		}
		objMembersDAL = null;

		if (cancel)
			focusView.requestFocus();

		return cancel;
	}

	private void saveMember() {

		long iReturn = -1;

		TMembers tMember = new TMembers();
		tMember.setLineId(mLineId);
		if (mMemberId > 0)
			tMember.setMemberId(mMemberId);

		tMember.setMemberCode(txtMemberCode.getText().toString());
		tMember.setMemberName(txtMemberName.getText().toString());
		tMember.setMobileNumber(txtMemberMobile.getText().toString());
		tMember.setGuaranteedBy(txtMemberGuaranteed.getText().toString());
		tMember.setAlternateNo(txtMemEntryAltMobileNo.getText().toString());
		tMember.setAddress(txtMemberAddress.getText().toString());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bitmap = ((BitmapDrawable) mPhotoImageView.getDrawable())
				.getBitmap();
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] photo = baos.toByteArray();
			tMember.setPhoto(photo);
		}

		MembersDAL objMembersDAL = new MembersDAL(getApplicationContext());
		if (mMemberId > 0)
			iReturn = objMembersDAL.Update(tMember);
		else
			iReturn = objMembersDAL.insert(tMember);

		if (iReturn == -1) {
			Toast.makeText(MemberEntryActivity.this, "Unable to Save",
					Toast.LENGTH_LONG).show();
		} else {
			if (mMemberId > 0)
				Toast.makeText(MemberEntryActivity.this,
						"Updated Successfully", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(MemberEntryActivity.this, "Saved Successfully",
						Toast.LENGTH_LONG).show();
		}

	}

	private void doTakePhotoAction() {
		// http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		// http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
		// http://www.damonkohler.com/2009/02/android-recipes.html
		// http://www.firstclown.us/tag/android/
		// The one I used to get everything working:
		// http://groups.google.com/group/android-developers/msg/2ab62c12ee99ba30

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Wysie_Soh: Create path for temp file
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_contact_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);

		try {

			intent.putExtra("return-data", true);
			startActivityForResult(intent, PICK_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			// Do nothing for now
		}
	}

	Bitmap mPhoto;
	Boolean mPhotoChanged;

	// ImageView mPhotoImageView;
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		String ImageFrom = "";
		try {
			switch (requestCode) {

			case CROP_FROM_CAMERA: {
				// Wysie_Soh: After a picture is taken, it will go to
				// PICK_FROM_CAMERA, which will then come here
				// after the image is cropped.

				final Bundle extras = data.getExtras();

				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");

					mPhoto = photo;
					mPhotoChanged = true;
					mPhotoImageView.setImageBitmap(photo);
					// setPhotoPresent(true);
				}

				// Wysie_Soh: Delete the temporary file
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists()) {
					f.delete();
				}

				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				mgr.showSoftInput(mPhotoImageView,
						InputMethodManager.SHOW_IMPLICIT);

				ImageFrom = "CROP_FROM_CAMERA";
				break;
			}

			case PICK_FROM_CAMERA: {

				ImageFrom = "PICK_FROM_CAMERA";
				// Wysie_Soh: After an image is taken and saved to the location
				// of
				// mImageCaptureUri, come here
				// and load the crop editor, with the necessary parameters
				// (96x96,
				// 1:1 ratio)

				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setClassName("com.android.gallery",
						"com.android.camera.CropImage");

				intent.setData(mImageCaptureUri);
				intent.putExtra("outputX", 96);
				intent.putExtra("outputY", 96);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP_FROM_CAMERA);

				break;

			}
			}
		} catch (Exception ex) {
			Toast.makeText(MemberEntryActivity.this,
					ImageFrom + ":" + ex.getMessage(), Toast.LENGTH_LONG)
					.show();

		}
	}

	public void addListenerOnSpinnerItemSelection() {

		cmbLineName = (Spinner) findViewById(R.id.memEntrycmbLineName);
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

}