package es.vicfran.bookdrop.models;

import java.util.Comparator;

import nl.siegmann.epublib.domain.Book;

import com.dropbox.sync.android.DbxPath;
/**
 * This class uses Paul Siegmann's library for epub management under GNU Lesser General Public License.
 * @url http://www.siegmann.nl/epublib
 */

/**
 * DbxBook model class holds together a Book (epub) and its path in Dropbox (DbxPath)
 * @author Victor de Francisco Domingo
 * @date 08/25/2013
 * @email victor_defran@yahoo.es
 */
public class DbxBook {
	
	private Book book;
	
	private DbxPath dbxPath;
	
	public DbxBook() {}
	
	public DbxBook(Book book, DbxPath dbxPath) {
		this.book = book;
		this.dbxPath = dbxPath;
	}
	
	public static final Comparator<DbxBook> titleComparator = new Comparator<DbxBook> () {
		public int compare(DbxBook dbxBook1, DbxBook dbxBook2) {
			if ((dbxBook1 == null) || (dbxBook2 == null)) return 0;
			if ((dbxBook1.getBook() == null) || (dbxBook2.getBook() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata() == null) || (dbxBook2.getBook().getMetadata() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata().getTitles() == null) || (dbxBook2.getBook().getMetadata().getTitles() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata().getTitles().get(0) == null) || 
					(dbxBook2.getBook().getMetadata().getTitles().get(0) == null)) return 0;
			
			return dbxBook1.getBook().getMetadata().getTitles().get(0)
					.compareTo(dbxBook2.getBook().getMetadata().getTitles().get(0));
		}
	};
	
	public static final Comparator<DbxBook> dateComparator = new Comparator<DbxBook> () { 
		public int compare(DbxBook dbxBook1, DbxBook dbxBook2) {
			if ((dbxBook1 == null) || (dbxBook2 == null)) return 0;
			if ((dbxBook1.getBook() == null) || (dbxBook2.getBook() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata() == null) || (dbxBook2.getBook().getMetadata() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata().getDates() == null) || (dbxBook2.getBook().getMetadata().getDates() == null)) return 0;
			if ((dbxBook1.getBook().getMetadata().getDates().get(0) == null) || 
					(dbxBook2.getBook().getMetadata().getDates().get(0) == null)) return 0;
			
			return dbxBook1.getBook().getMetadata().getDates().get(0).toString()
				.compareTo(dbxBook2.getBook().getMetadata().getDates().get(0).toString());
		}
	};
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public void setDbxPath(DbxPath dbxPath) {
		this.dbxPath = dbxPath;
	}
	
	public DbxPath getDbxPath() {
		return dbxPath;
	}
}
