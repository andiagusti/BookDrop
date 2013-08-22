package es.vicfran.bookdrop.adapters;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dropbox.sync.android.DbxFileInfo;

import es.vicfran.bookdrop.R;

/**
 * BookListAdapter is the base adapter for the book list
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListAdapter implements ListAdapter {
	
	private List<DbxFileInfo> files;
	
	private LayoutInflater inflater;
	
	public BookListAdapter(Context context, List<DbxFileInfo> files) {
		this.files = files;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return files.size();
	}
	
	@Override
	public DbxFileInfo getItem(int position) {
		return files.get(position);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.book_list_row, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.bookThumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
			viewHolder.bookNameTextView = (TextView) convertView.findViewById(R.id.lbl_name);
			viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.lbl_last_modified);
			convertView.setTag(viewHolder);
		}
		
		DbxFileInfo file = files.get(position);
		// Get View holder from convert view with view references
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		// TODO : Book thumbnail
		viewHolder.bookNameTextView.setText(file.path.getName());
		viewHolder.dateTextView.setText(getDateString(file.modifiedTime));
		
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
		return files.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	private String getDateString(Date date) {
		DateFormat format = DateFormat.getInstance();
		
		return format.format(date);
	}
}
