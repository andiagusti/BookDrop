package es.vicfran.bookdrop.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dropbox.sync.android.DbxFileInfo;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.fragments.BookListFragment.BookListCallbacks;

/**
 * BookListActivity class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListActivity extends FragmentActivity implements BookListCallbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/*
	 * CALLBACKS
	 */
	public void onBookDetail(DbxFileInfo dbxFileInfo) {
		if (findViewById(R.id.fragment_book_detail) != null) {
			// Activity has 2 fragments (list-detail), screen with almost 600dp
			// We can show book list and details of book selected in the same activity
			
		} else {
			// Activity has only one fragment (list) 
			// We can only show book list, when user wants book details, show them in another activity
		}
		
	}
}
