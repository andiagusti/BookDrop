package es.vicfran.bookdrop.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;

public class BookDetailFragment extends Fragment {
	
	private TextView pathTextView;
	
	public static String ARG_FILE_INFO = "es.vicfran.bookdrop.fragments:file_info";
	
	private DbxPath dbxPath;
	
	public static BookDetailFragment buildBookDetailFragment(String path) {
		BookDetailFragment bookDetailFragment = new BookDetailFragment();
		
		Bundle args = new Bundle();
		
		// DbxFileInfo doesn't implement Parcelable interface, so we can't
		// put it inside arguments bundle, path info is enough.
		args.putString(ARG_FILE_INFO, path);
		
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
				dbxPath = new DbxPath(args.getString(ARG_FILE_INFO));
			} catch (DbxPath.InvalidPathException e) {
				dbxPath = null;
			}
		}
		
		if (pathTextView != null) {
			pathTextView.setText((dbxPath != null) ? dbxPath.toString() : "");
		}
	}
}
