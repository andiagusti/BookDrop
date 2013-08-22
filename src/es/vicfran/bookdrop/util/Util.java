package es.vicfran.bookdrop.util;

import android.content.Context;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

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
	
	public static DbxAccountManager getAccountManager(Context context) {
		return DbxAccountManager.getInstance(context.getApplicationContext(), APP_KEY, APP_SECRET);
	}
	
	public static DbxFileSystem getFileSystem(Context context) {
		DbxAccountManager dbxAccountManager = getAccountManager(context);
		if (dbxAccountManager == null) return null;
		DbxAccount dbxAccount = dbxAccountManager.getLinkedAccount();
		try{
			return DbxFileSystem.forAccount(dbxAccount);
		} catch (DbxException.Unauthorized exception) {
			return null;
		}
	}
}
