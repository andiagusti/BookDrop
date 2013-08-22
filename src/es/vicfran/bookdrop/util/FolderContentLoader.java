package es.vicfran.bookdrop.util;

import java.util.ArrayList;
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
public class FolderContentLoader extends AsyncTaskLoader<List<DbxFileInfo>> {
	
	public static final String TAG = FolderContentLoader.class.getName();
	
	private DbxAccountManager dbxAccountManager;
	// Folder to list its content path
	private DbxPath dbxPath;
	private List<DbxFileInfo> files = null;
	
	private Context context;
	
	public FolderContentLoader(Context context, DbxAccountManager dbxAccountManager, DbxPath dbxPath) {
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
		
		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		
		if (dbxFileSystem != null) {
			dbxFileSystem.addPathListener(pathListener, dbxPath, DbxFileSystem.PathListener.Mode.PATH_ONLY);
		}
		
		// If content has changed or there aren't results, force asynchronous load
		if (takeContentChanged() || files == null) {
			forceLoad();
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
		
		if (dbxFileSystem != null) {
			try {
				files = getEpubFiles(dbxFileSystem.listFolder(dbxPath));
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
	 * This method takes a list of DbxFileInfos and selects those
	 * with epub extension
	 */
	private List<DbxFileInfo> getEpubFiles(List<DbxFileInfo> files) {
		List<DbxFileInfo> epubs = new ArrayList<DbxFileInfo>();
		for(DbxFileInfo file : files) {
			if (Util.getFileExtension(file.path.getName()).equals(Util.EBOOK_EXTENSION)) {
				epubs.add(file);
			}
		}
		
		return epubs;
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
