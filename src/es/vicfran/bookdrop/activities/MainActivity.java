package es.vicfran.bookdrop.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.dropbox.sync.android.DbxAccountManager;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.Util;

/**
 * MainActivtiy class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class MainActivity extends Activity implements OnMenuItemClickListener {

	private DbxAccountManager dbxAccountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), Util.APP_KEY, Util.APP_SECRET);
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
			if (dbxAccountManager != null) {
				if (dbxAccountManager.hasLinkedAccount()) {
					// TODO : Progress bar or dialog with progress
					dbxAccountManager.unlink();
				}
			}

			Intent intent = new Intent(this, SignActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			return false;
		}

		return true;
	}

}
