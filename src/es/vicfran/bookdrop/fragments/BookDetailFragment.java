package es.vicfran.bookdrop.fragments;


import java.io.IOException;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Resource;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.adapters.BookListAdapter;
import es.vicfran.bookdrop.models.DbxBook;

/**
 * This class uses Paul Siegmann's library for epub management under GNU Lesser General Public License.
 * @url http://www.siegmann.nl/epublib
 */


/**
 * BookDetailFragment class shows book details inside a fragment
 * @author Victor de Francisco Domingo
 * @date 08/24/2013
 * @email victor_defran@yahoo.es
 */
public class BookDetailFragment extends Fragment {
	
	private TextView titleTextView;
	private TextView authorTextView;
	private ImageView coverImageView;
	
	public static final String ARG_FILE_PATH = "es.vicfran.bookdrop.fragments:file_info";
	public static final String ARG_BOOK_ID = "es.vicfran.bookdrop.fragments:book_id";
	
	private DbxBook dbxBook;
	
	public static BookDetailFragment buildBookDetailFragment(int bookId) {

		BookDetailFragment bookDetailFragment = new BookDetailFragment();
		
		Bundle args = new Bundle();
		
		// DbxFileInfo doesn't implement Parcelable interface, so we can't
		// put it inside arguments bundle, path info is enough.
		args.putInt(ARG_BOOK_ID, bookId);
		
		bookDetailFragment.setArguments(args);
		
		return bookDetailFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		
		dbxBook = BookListAdapter.getBooks().get(args.getInt(ARG_BOOK_ID));
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
		
		if (dbxBook != null) {
			titleTextView.setText(dbxBook.getBook().getMetadata().getFirstTitle());
			authorTextView.setText(parseAuthors(dbxBook.getBook().getMetadata().getAuthors()));
			
			Drawable thumbDrawable = null;
			try {
				Resource coverImage = dbxBook.getBook().getCoverImage();
				if (coverImage != null) {
					thumbDrawable = Drawable.createFromStream(coverImage.getInputStream(), "");
				}
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
		
		if ((authors == null) || (authors.size() == 0)) {
			return authorsString;
		}
		
		for(Author author : authors) {
			authorsString = authorsString.concat(author + ", ");
		}
		
		return authorsString.substring(0, authorsString.length() - 2);
	}
}
