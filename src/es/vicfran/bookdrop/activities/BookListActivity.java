package es.vicfran.bookdrop.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import es.vicfran.bookdrop.R;

/**
 * BookListActivity class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class BookListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
