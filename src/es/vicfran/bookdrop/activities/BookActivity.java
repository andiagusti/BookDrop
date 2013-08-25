package es.vicfran.bookdrop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.fragments.BookDetailFragment;
import es.vicfran.bookdrop.fragments.BookListFragment.BookListCallbacks;
import es.vicfran.bookdrop.models.DbxBook;

/**
 * BookListActivity class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookActivity extends FragmentActivity implements BookListCallbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/*
	 * CALLBACKS
	 */
	public void onBookDetail(int bookId) {
		if (findViewById(R.id.book_detail_fragment) != null) {
			// Activity has 2 fragments (list-detail), screen with almost 600dp
			// We can show book list and details of book selected in the same activity
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.book_detail_fragment, BookDetailFragment.buildBookDetailFragment(bookId))
			.commit();
		} else {
			// Activity has only one fragment (list) 
			// We can only show book list, when user wants book details, show them in another activity
			Intent intent = new Intent(this, BookDetailActivity.class);
			intent.putExtra(BookDetailFragment.ARG_BOOK_ID, bookId);
			startActivity(intent);
		}
	}
}
