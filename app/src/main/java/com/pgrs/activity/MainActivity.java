package com.pgrs.activity;

import com.pgrs.myaccount.R;

import android.os.Bundle;


public class MainActivity extends DashboardActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
		// closing transition animations
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_open_translate);
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

} // end class
