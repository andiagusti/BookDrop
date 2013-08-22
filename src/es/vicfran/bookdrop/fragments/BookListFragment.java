package es.vicfran.bookdrop.fragments;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.activities.SignActivity;
import es.vicfran.bookdrop.util.FolderContentLoader;
import es.vicfran.bookdrop.util.Util;

/**
 * BookListFragment represents a fragment with a book list
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListFragment extends Fragment implements DbxAccountManager.AccountListener, LoaderCallbacks<List<DbxFileInfo>>, 
			OnMenuItemClickListener {
	
	public static final String TAG = BookListFragment.class.getName();
	
	private final int LOADER_ID = 0;
	
	private ListView bookListView;
	
	private DbxAccountManager dbxAccountManager;
	private DbxFileSystem dbxFileSystem;
	
	// Progress dialog that shows sign out progress
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		bookListView = (ListView) view.findViewById(android.R.id.list);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// Ensure that loader is initialized and active at activity start
		// If any loader already exists, use it
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setHasOptionsMenu(true);
				
		dbxAccountManager = Util.getAccountManager(activity);
		
		if (dbxAccountManager != null) {
			dbxAccountManager.addListener(this);
			
			dbxFileSystem = Util.getFileSystem(activity);
			if (dbxFileSystem == null) {
				Toast.makeText(activity, getString(R.string.unauthorized_error), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
		inflater.inflate(R.menu.main, menu);
		
		if (menu != null) {
			menu.findItem(R.id.action_refresh).setOnMenuItemClickListener(this);
			menu.findItem(R.id.action_settings).setOnMenuItemClickListener(this);
			menu.findItem(R.id.action_sign_out).setOnMenuItemClickListener(this);
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		switch(menuItem.getItemId()) {
		case R.id.action_refresh:
			// TODO
			break;
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
				Toast.makeText(getActivity(), getString(R.string.account_error), Toast.LENGTH_LONG).show();
			}

			// Anyway, go to sign activity
			backToSignActivityAndFinish();

			return true;
		default:
			break;
		}

		return false;
	}
	
	private void backToSignActivityAndFinish() {
		Intent intent = new Intent(getActivity(), SignActivity.class);
		startActivity(intent);
		getActivity().finish();
	}

	private ProgressDialog buildSignOutProgressDialog() {
		return ProgressDialog.show(
				getActivity(),
				null,
				getString(R.string.progress_text),
				true,
				true);
	}
	
	
	/*
	 * CALLBACKS
	 */
	
	// DbxCallbacks
	@Override
	public void onLinkedAccountChange(DbxAccountManager dbxAccountManager, DbxAccount dbxAccount) {
		if ((progressDialog != null) && (progressDialog.isShowing())) {
			progressDialog.dismiss();
		}
	}
	
	// LoaderCallbacks
	@Override
	public Loader<List<DbxFileInfo>> onCreateLoader(int id, Bundle args) {
		return new FolderContentLoader(getActivity(), dbxAccountManager, Util.APP_PATH);
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
}
