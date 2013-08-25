package es.vicfran.bookdrop.util;

import java.util.List;

import nl.siegmann.epublib.domain.Book;
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
public class FolderContentLoader extends AsyncTaskLoader<List<Book>> {
	
	public static final String TAG = FolderContentLoader.class.getName();
	
	private DbxAccountManager dbxAccountManager;
	// Folder to list its content path
	private DbxPath dbxPath;
	private List<Book> books = null;
	
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
		if (books != null) {
			deliverResult(books);
		}
		
		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		
		if (dbxFileSystem != null) {
			dbxFileSystem.addPathListener(pathListener, dbxPath, DbxFileSystem.PathListener.Mode.PATH_ONLY);
		}
		
		// If content has changed or there aren't results, force asynchronous load
		if (takeContentChanged() || books == null) {
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
		books = null;
		onStopLoading();
	}
	
	@Override
	public List<Book> loadInBackground() {
		DbxFileSystem dbxFileSystem = Util.getFileSystem(context);
		
		if (dbxFileSystem != null) {
			try {
				List<DbxFileInfo> files = Util.getFiles(dbxFileSystem.listFolder(dbxPath), Util.EBOOK_EXTENSION);
				// Get book instances from dropbox file instances
				books = Util.getBooks(context, files);
			} catch (DbxException exception) {
				books = null;
			}
		}
		
		return books;
	}
	
	@Override
	public void deliverResult(List<Book> files) {
		if (isReset()) {
			return;
		}
		
		this.books = files;
		
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
