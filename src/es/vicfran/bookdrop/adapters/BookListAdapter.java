package es.vicfran.bookdrop.adapters;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.models.DbxBook;

/**
 * BookListAdapter is the base adapter for the book list
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListAdapter extends ArrayAdapter<DbxBook> {
	
	public interface BookItemListCallbacks {
		public void onBookItemClick(int bookId);
	}

	private LayoutInflater inflater;
	
	private BookItemListCallbacks callbacks;
	
	private static List<DbxBook> list;
	
	public BookListAdapter(Context context, int resource, int textViewResource, List<DbxBook> books, Fragment fragment) {
		super(context, resource, books);
		
		this.list = books;
				
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (fragment instanceof BookItemListCallbacks) {
			callbacks = (BookItemListCallbacks) fragment;
		}
	}

	// View holder pattern avoids findViewById() calls for reused convert views.
	// This is faster than traditional method (~15% faster).
	static class ViewHolder {
		public ImageView bookThumbnailImageView;
		public TextView bookNameTextView;
		public TextView dateTextView;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if ((getItem(position) == null) || (getItem(position).getBook() == null)) {
			return null;
		}
		
		final DbxBook dbxBook = getItem(position);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.book_list_row, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.bookThumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
			viewHolder.bookNameTextView = (TextView) convertView.findViewById(R.id.lbl_name);
			convertView.setTag(viewHolder);
		}
		
		// Get View holder from convert view with view references
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		// TODO : Book thumbnail
		viewHolder.bookNameTextView.setText(dbxBook.getBook().getMetadata().getFirstTitle());
		
		if (callbacks != null) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					callbacks.onBookItemClick(position);
				}
			});
		}
		
		return convertView;
	}
	
	
	@Override
	public boolean isEnabled(int position) {
		return true;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	private String getDateString(Date date) {
		DateFormat format = DateFormat.getInstance();
		
		return format.format(date);
	}
	
	public static List<DbxBook> getBooks() {
		if (list == null) {
			list = new ArrayList<DbxBook>();
		}
		
		return list;
	}
}
