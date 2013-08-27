package es.vicfran.bookdrop.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import android.content.Context;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.models.DbxBook;

/**
 * Utility class with some util data & methods
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public final class Util {
	// Private constructor to prevent against class instantiation
	private Util() {}
	
	// Be aware that it isn't a good way to store this info, 
	// because it's sensitive data and it must be stored in a more
	// secure way, for example obfuscated or encrypted.
	//
	// For more security, this data is private to prevent its use outside this class
	private static final String APP_KEY = "8z1wqhkohai8aoz";
	private static final String APP_SECRET = "ny1yoknc71nkwfn";
	
	// Folder of this app on Dropbox account
	public static final String APP_FOLDER = "ebooks";
	public static final DbxPath APP_PATH = new DbxPath(DbxPath.ROOT, APP_FOLDER);
	
	public static final String EBOOK_EXTENSION = "epub";
	
	public static DbxAccountManager getAccountManager(Context context) {
		return DbxAccountManager.getInstance(context.getApplicationContext(), APP_KEY, APP_SECRET);
	}
	
	public static DbxFileSystem getFileSystem(Context context) {
		DbxAccountManager dbxAccountManager = getAccountManager(context);
		if (dbxAccountManager == null) return null;
		DbxAccount dbxAccount = dbxAccountManager.getLinkedAccount();
		if (dbxAccount != null) {
			try{
				return DbxFileSystem.forAccount(dbxAccount);
			} catch (DbxException.Unauthorized exception) {
				return null;
			}
		}
		return null;
	}
	
	
	// Assume that file name is in the form
	// 		filename.*
	public static String getFileName(String file) {
		return file.split("\\.", 2)[0];
	}
	
	public static String getFileExtension(String file) {
		return file.split("\\.", 2)[1];
	}
	
	/*
	 * This method takes a list of DbxFileInfos and selects those
	 * with |extension| extension
	 */
	public static List<DbxFileInfo> getFiles(List<DbxFileInfo> files, String extension) {
		List<DbxFileInfo> selected = new ArrayList<DbxFileInfo>();
		for(DbxFileInfo file : files) {
			if (Util.getFileExtension(file.path.getName()).equals(extension)) {
				selected.add(file);
			}
		}
		
		return selected;
	}
	
	/*
	 * Gets Book list from DbxFileInfo files
	 */
	public static List<DbxBook> getDbxBooks(Context context, List<DbxFileInfo> files) {
		List<DbxBook> books = new ArrayList<DbxBook>();
		
		for (DbxFileInfo dbxFileInfo : files) {
			books.add(getDbxBook(context, dbxFileInfo));
		}
		
		return books;
	}
	
	/*
	 * Gets Book from DbxFileInfo
	 */
	public static DbxBook getDbxBook(Context context, DbxFileInfo dbxFileInfo) {
		DbxFileSystem dbxFileSystem = getFileSystem(context);
		if (dbxFileSystem == null) return null;
		
		DbxFile dbxFile = null;
		DbxBook dbxBook = null;
		Book book = null;
		try {
			dbxFile = dbxFileSystem.open(dbxFileInfo.path);
			EpubReader reader = new EpubReader();
			book = reader.readEpub(dbxFile.getReadStream());
			
			dbxBook = new DbxBook(book, dbxFileInfo.path);
		} catch (DbxException e) {
			return  null;
		} catch (IOException e) {
			return null;
		} finally {
			dbxFile.close();
		}
		
		return dbxBook;
	}
}
