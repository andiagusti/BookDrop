package es.vicfran.bookdrop.adapters;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dropbox.sync.android.DbxFileInfo;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.util.Util;

/**
 * BookListAdapter is the base adapter for the book list
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListAdapter implements ListAdapter {
	
	public interface BookItemListCallbacks {
		public void onBookItemClick(int bookId);
	}
	
	// Source data with books
	private static List<Book> books;
	
	private LayoutInflater inflater;
	
	private BookItemListCallbacks callbacks;
	
	public BookListAdapter(Context context, Fragment fragment, List<Book> books) {
		this.books = books;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (fragment instanceof BookItemListCallbacks) {
			callbacks = (BookItemListCallbacks) fragment;
		}
	}
	
	@Override
	public int getCount() {
		return books.size();
	}
	
	@Override
	public Book getItem(int position) {
		return books.get(position);
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	@Override
	public int getItemViewType(int position) {
		// Every row has the same layout, don't need to worry about view types
		return 0;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.book_list_row, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.bookThumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
			viewHolder.bookNameTextView = (TextView) convertView.findViewById(R.id.lbl_name);
			viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.lbl_last_modified);
			convertView.setTag(viewHolder);
		}
		
		final Book book = books.get(position);
		// Get View holder from convert view with view references
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		// TODO : Book thumbnail
		viewHolder.bookNameTextView.setText(book.getMetadata().getFirstTitle());
		//viewHolder.dateTextView.setText(getDateString(book.getMetadata().getDates().get(0));
		
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
	public boolean isEmpty() {
		return books.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	private String getDateString(Date date) {
		DateFormat format = DateFormat.getInstance();
		
		return format.format(date);
	}
	
	public void dateSort() {
		
	}
	
	public static List<Book> getBooks() {
		return books;
	}
}
