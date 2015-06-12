/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pgrs.activity;

import com.pgrs.myaccount.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DashboardActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onRestart() {
		super.onRestart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	public void onClickHome(View v) {
		goHome(this);
	}

	public void onClickFeature(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.mnuMemberEntry:
			startActivity(new Intent(getApplicationContext(),
					MemberActivity.class));
			break;
		case R.id.mnuLineEntry:
			startActivity(new Intent(getApplicationContext(),
					LineListActivity.class));
			break;
		case R.id.mnuCollection:
			startActivity(new Intent(getApplicationContext(),
					CollectionMainActivity.class));
			break;
		case R.id.mnuPayment:
			Toast.makeText(DashboardActivity.this, "Under Development",
					Toast.LENGTH_LONG).show();

			break;
		case R.id.mnuExpense:

			Toast.makeText(DashboardActivity.this, "Under Construction",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.mnuExit:

			final Builder builder = new Builder(this);
			builder.setTitle("brief");
			builder.setMessage("Do you want to close?");
			builder.setPositiveButton(android.R.string.ok,
					new OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int which) {
							DashboardActivity.this.finish();
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(android.R.string.cancel,
					new OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int which) {
							dialog.dismiss();
						}
					});
			final AlertDialog dialog = builder.create();
			dialog.show();
			break;

		case R.id.mnuAbstract:

			Toast.makeText(DashboardActivity.this, "Under Construction",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

	}

	public void goHome(Context context) {
		final Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	public void setTitleFromActivityLabel(int textViewId) {
		TextView tv = (TextView) findViewById(textViewId);
		if (tv != null)
			tv.setText(getTitle());
	} // end setTitleText

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	} // end toast

	public void trace(String msg) {
		Log.d("Demo", msg);
		toast(msg);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		final Builder builder = new Builder(this);
		builder.setTitle("brief");
		builder.setMessage("Do you want to close?");
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				DashboardActivity.this.finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(android.R.string.cancel,
				new OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						dialog.dismiss();
					}
				});
		final AlertDialog dialog = builder.create();
		dialog.show();
	}
} // end class
