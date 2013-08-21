package es.vicfran.bookdrop.activities;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.FileLoader;
import es.vicfran.bookdrop.util.Util;

/**
 * MainActivtiy class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class MainActivity extends ListActivity implements OnMenuItemClickListener, DbxAccountManager.AccountListener, LoaderCallbacks<List<DbxFileInfo>>{

	private DbxAccountManager dbxAccountManager;
	private DbxFileSystem dbxFileSystem = null;

	// Progress dialog that shows sign out progress
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbxAccountManager = Util.getAccountManager(getApplicationContext());
		
		if (dbxAccountManager != null) {
			dbxAccountManager.addListener(this);
			
			dbxFileSystem = Util.getFileSystem(getApplicationContext());
			if (dbxFileSystem == null) {
				Toast.makeText(this, getString(R.string.unauthorized_error), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		if (menu != null) {
			menu.findItem(R.id.action_settings).setOnMenuItemClickListener(this);
			menu.findItem(R.id.action_sign_out).setOnMenuItemClickListener(this);
		}

		return true;
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		switch(menuItem.getItemId()) {
		case R.id.action_settings:
			// TODO
			break;
		case R.id.action_sign_out:
			// If there is any Dropbox account linked, unlink it
			if ((dbxAccountManager != null) && (dbxAccountManager.hasLinkedAccount())){
					progressDialog = buildSignOutProgressDialog();
					
					dbxAccountManager.unlink();
			} else {
				// At this point, there must be a Dropbox account linked, if not, error
				Toast.makeText(this, getString(R.string.account_error), Toast.LENGTH_LONG).show();
			}

			// Anyway, go to sign activity
			backToSignActivityAndFinish();

			return true;
		default:
			break;
		}

		return false;
	}

	@Override
	public void onLinkedAccountChange(DbxAccountManager dbxAccountManager, DbxAccount dbxAccount) {
		if ((progressDialog != null) && (progressDialog.isShowing())) {
			progressDialog.dismiss();
		}
	}
	
	/*
	 * LoaderCallbacks
	 */
	@Override
	public Loader<List<DbxFileInfo>> onCreateLoader(int id, Bundle args) {
		return new FileLoader(this, dbxAccountManager, Util.APP_PATH);
	}
	
	@Override
	public void onLoaderReset(Loader<List<DbxFileInfo>> loader) {
	}
	
	@Override
	public void onLoadFinished(Loader<List<DbxFileInfo>> loader, List<DbxFileInfo> data) {
		if (loader != null) {
			// TODO : set list adapter
		}
	}

	private void backToSignActivityAndFinish() {
		Intent intent = new Intent(this, SignActivity.class);
		startActivity(intent);
		finish();
	}

	private ProgressDialog buildSignOutProgressDialog() {
		return ProgressDialog.show(
				this,
				null,
				getString(R.string.progress_text),
				true,
				true);
	}
}
