package es.vicfran.bookdrop.activities;

import es.vicfran.bookdrop.R;
import es.vicfran.bookdrop.R.layout;
import es.vicfran.bookdrop.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * MainActivtiy class represents main app screen
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
