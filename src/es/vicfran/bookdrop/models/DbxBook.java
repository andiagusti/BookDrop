package es.vicfran.bookdrop.models;

import nl.siegmann.epublib.domain.Book;

import com.dropbox.sync.android.DbxPath;

public class DbxBook {
	
	private Book book;
	
	private DbxPath dbxPath;
	
	public DbxBook() {}
	
	public DbxBook(Book book, DbxPath dbxPath) {
		this.book = book;
		this.dbxPath = dbxPath;
	}
	
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
