package es.vicfran.bookdrop.fragments;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.Util;

public class BookDetailFragment extends Fragment {
	
	private TextView pathTextView;
	private ImageView thumbnailImageView;
	
	public static String ARG_FILE_INFO = "es.vicfran.bookdrop.fragments:file_info";
	
	private DbxPath dbxPath;
	private DbxFile thumb = null;
	private Drawable thumbDrawable = null;
	
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
		
		Context context = getActivity();
		DbxFileSystem dbxFileSystem = Util.getFileSystem(getActivity());
		
		if (dbxFileSystem != null) {
			try{
				thumb = dbxFileSystem.openThumbnail(dbxPath, DbxFileSystem.ThumbSize.M, DbxFileSystem.ThumbFormat.PNG);
			} catch (DbxException.NotFound e) {
				Toast.makeText(context, context.getString(R.string.thumb_not_found_error), Toast.LENGTH_SHORT).show();
			} catch (DbxException.InvalidParameter e) {
				Toast.makeText(context, context.getString(R.string.thumb_not_file_error), Toast.LENGTH_SHORT).show();
			} catch (DbxException.NoThumb e) {
				Toast.makeText(context, context.getString(R.string.thumb_not_available_error), Toast.LENGTH_SHORT).show();
			} catch (DbxException e) {
				Toast.makeText(context, context.getString(R.string.thumb_error), Toast.LENGTH_SHORT).show();
			}
		}
		
		if ((thumb != null) && (thumb.isThumb())) {
			try {
				thumbDrawable = Drawable.createFromStream(thumb.getReadStream(), "");
			} catch (IOException e) {
				thumbDrawable = null;
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		pathTextView = (TextView) view.findViewById(R.id.lbl_path);
		thumbnailImageView = (ImageView) view.findViewById(R.id.thumbnail);
		
		pathTextView.setText((dbxPath != null) ? dbxPath.toString() : "");
		if (thumbDrawable != null) {
			thumbnailImageView.setImageDrawable(thumbDrawable);
		}
	}
}
