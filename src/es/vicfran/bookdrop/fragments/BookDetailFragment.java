package es.vicfran.bookdrop.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.Util;

public class BookDetailFragment extends Fragment {
	
	private TextView titleTextView;
	private TextView authorTextView;
	private ImageView coverImageView;
	
	public static String ARG_FILE_INFO = "es.vicfran.bookdrop.fragments:file_info";
	
	private DbxPath dbxPath;
	private DbxFile file;
	private Book book = null;
	
	public static BookDetailFragment buildBookDetailFragment(String path) {
		BookDetailFragment bookDetailFragment = new BookDetailFragment();
		
		Bundle args = new Bundle();
		
		// DbxFileInfo doesn't implement Parcelable interface, so we can't
		// put it inside arguments bundle, path info is enough.
		args.putString(ARG_FILE_INFO, path);
		
		bookDetailFragment.setArguments(args);
		
		return bookDetailFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if (args != null) {
			try {
				dbxPath = new DbxPath(args.getString(ARG_FILE_INFO));
			} catch (DbxPath.InvalidPathException e) {
				dbxPath = null;
			}
		}
		
		DbxFileSystem dbxFileSystem = Util.getFileSystem(getActivity());
		
		try {
			file = dbxFileSystem.open(dbxPath);
			InputStream is = file.getReadStream();
			EpubReader e = new EpubReader();
			book = e.readEpub(is);
		} catch (DbxException e) {
			
		} catch (IOException e) {
			
		}		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if (file != null) {
			file.close();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		titleTextView = (TextView) view.findViewById(R.id.lbl_title);
		authorTextView = (TextView) view.findViewById(R.id.lbl_author);
		coverImageView = (ImageView) view.findViewById(R.id.img_book_cover);
		
		if (book != null) {
			titleTextView.setText(book.getMetadata().getFirstTitle());
			authorTextView.setText(parseAuthors(book.getMetadata().getAuthors()));
			
			Drawable thumbDrawable;
			try {
				thumbDrawable = Drawable.createFromStream(book.getCoverImage().getInputStream(), "");
			} catch (IOException e) {
				thumbDrawable = null;
			}
			
			coverImageView.setImageDrawable((thumbDrawable != null) ? 
												thumbDrawable : 
												getActivity().getResources().getDrawable(R.drawable.ic_launcher)); 
		}
	}
	
	private String parseAuthors(List<Author> authors) {
		String authorsString = "";
		
		for(Author author : authors) {
			authorsString = authorsString.concat(author + ", ");
		}
		
		return authorsString.substring(0, authorsString.length() - 2);
	}
}
