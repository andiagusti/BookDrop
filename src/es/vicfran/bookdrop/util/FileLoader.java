package es.vicfran.bookdrop.util;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

/**
 * FileLoader class is an AsynTask that loads folder content asynchronously
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class FileLoader extends AsyncTaskLoader<List<DbxFileInfo>> {
	
	private DbxAccountManager dbxAccountManager;
	// Folder to list its content path
	private DbxPath dbxPath;
	private List<DbxFileInfo> files = null;
	
	private Context context;
	
	public FileLoader(Context context, DbxAccountManager dbxAccountManager, DbxPath dbxPath) {
		super(context);
		
		this.context = context;
		
		this.dbxAccountManager = dbxAccountManager;
		this.dbxPath = dbxPath;
	}
	
	@Override
	protected void onStartLoading() {
		// If there are results, deliver them
		if (files != null) {
			deliverResult(files);
		}
		
		// If content has changed or there aren't results, force asynchronous load
		if (takeContentChanged() || files == null) {
			forceLoad();
		}
		
		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		
		if (dbxFileSystem != null) {
			dbxFileSystem.addPathListener(pathListener, dbxPath, DbxFileSystem.PathListener.Mode.PATH_ONLY);
		}
	}
	
	@Override
	protected void onStopLoading() {
		cancelLoad();

		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		
		// Listener isn't needed
		if (dbxFileSystem != null) {
			dbxFileSystem.removePathListener(pathListener, dbxPath, DbxFileSystem.PathListener.Mode.PATH_ONLY);
		}		
	}
	
	@Override
	protected void onReset() {
		files = null;
		onStopLoading();
	}
	
	@Override
	public List<DbxFileInfo> loadInBackground() {
		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		List<DbxFileInfo> files = null;
		
		if (dbxFileSystem != null) {
			try {
				files = dbxFileSystem.listFolder(dbxPath);
			} catch (DbxException exception) {}
		}
		
		return files;
	}
	
	@Override
	public void deliverResult(List<DbxFileInfo> files) {
		if (isReset()) {
			return;
		}
		
		this.files = files;
		
		if (isStarted()) {
			super.deliverResult(files);
		}
	}
	
	
	/*
	 * Dpx listeners
	 */
	// If folder content change, reload
	private DbxFileSystem.PathListener pathListener = new DbxFileSystem.PathListener() {
		@Override
		public void onPathChange(DbxFileSystem dbxFileSystem, DbxPath dbxPath, Mode registeredMode) {
			onContentChanged();
		}
	};

}