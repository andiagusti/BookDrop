package es.vicfran.bookdrop.util;

import android.content.Context;

import com.dropbox.sync.android.DbxAccountManager;

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
	
	public static DbxAccountManager getAccountManager(Context context) {
		return DbxAccountManager.getInstance(context, APP_KEY, APP_SECRET);
	}
}
