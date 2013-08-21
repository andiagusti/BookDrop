package es.vicfran.bookdrop.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.Util;

/**
 * SignActivity class represents splash screen where user can sign in into Dropbox account
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class SignActivity extends Activity implements OnClickListener {
	
	// Request code to communicate with Dropbox authentication activity
	private final int DBX_REQUEST_CODE = 0;

	// Key used to maintain authentication error state between Activity life cycle
	private final String AUTHENTICATION_ERROR_KEY = "es.vicfran.bookdrop:authentication_error";

	private DbxAccountManager dbxAccountManager;

	private boolean authenticationError;

	private Button signButton;
	private ProgressBar progressBar;
	private TextView errorTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		signButton  	= (Button) findViewById(R.id.btn_sign);
		progressBar 	= (ProgressBar) findViewById(R.id.progress_bar);
		errorTextView 	= (TextView) findViewById(R.id.lbl_error);

		signButton.setOnClickListener(this);

		dbxAccountManager = Util.getAccountManager(getApplicationContext());
	}

	@Override
	public void onResume() {
		super.onResume();

		// If user has already sign in into Dropbox, go to Main activity and finish
		if (dbxAccountManager != null) {
			if (dbxAccountManager.hasLinkedAccount()) {
				startMainActivityAndFinish();
			}
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState == null) return;

		authenticationError = savedInstanceState.getBoolean(AUTHENTICATION_ERROR_KEY);
		errorTextView.setVisibility(authenticationError ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == DBX_REQUEST_CODE) {
			progressBar.setVisibility(View.INVISIBLE);

			if (resultCode != Activity.RESULT_OK) {
				authenticationError = true;
				errorTextView.setVisibility(View.VISIBLE);
			} else {
				authenticationError = false;
				startMainActivityAndFinish();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState == null) {
			outState = new Bundle();
		}

		// Store authentication error state
		outState.putBoolean(AUTHENTICATION_ERROR_KEY, authenticationError);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn_sign:
			if (dbxAccountManager != null) {
				dbxAccountManager.startLink(this, DBX_REQUEST_CODE);
				progressBar.setVisibility(View.VISIBLE);	
			}
			break;
	    default:
	    	break;
		}
	}

	// Because transition to MainActivity happens many times, encapsulate it in one method
	private void startMainActivityAndFinish() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
