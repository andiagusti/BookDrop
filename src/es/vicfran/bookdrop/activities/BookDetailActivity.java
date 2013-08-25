package es.vicfran.bookdrop.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.fragments.BookDetailFragment;

public class BookDetailActivity extends FragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		
		Bundle extras = getIntent().getExtras();
				
		getSupportFragmentManager().beginTransaction()
		.add(R.id.book_detail, BookDetailFragment.buildBookDetailFragment(extras.getString(BookDetailFragment.ARG_FILE_INFO)))
		.commit();
	}
}
