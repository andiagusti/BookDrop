package es.vicfran.bookdrop.fragments;

import nl.siegmann.epublib.domain.Book;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.adapters.BookListAdapter;

public class BookDetailFragment extends Fragment {
	
	private TextView pathTextView;
	
	public static String ARG_FILE_INFO = "es.vicfran.bookdrop.fragments:file_info";
	
	private Book book;
	
	public static BookDetailFragment buildBookDetailFragment(int bookId) {
		BookDetailFragment bookDetailFragment = new BookDetailFragment();
		
		Bundle args = new Bundle();
		
		// DbxFileInfo doesn't implement Parcelable interface, so we can't
		// put it inside arguments bundle, path info is enough.
		args.putInt(ARG_FILE_INFO, bookId);
		
		bookDetailFragment.setArguments(args);
		
		return bookDetailFragment;
	}
	
	// Official Android doc says that Fragments must have an empty constructor
	public BookDetailFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		pathTextView = (TextView) view.findViewById(R.id.lbl_path);
		
		Bundle args = getArguments();
		if (args != null) {
			try {
				book = BookListAdapter.getBooks().get(args.getInt(BookDetailFragment.ARG_FILE_INFO));
			} catch (DbxPath.InvalidPathException e) {
				book = null;
			}
		}
	}
}
