package es.vicfran.bookdrop.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import es.vicfran.bookdrop.R;

/**
 * SignActivity class represents splash screen where user can sign in into Dropbox account
 * @author Victor de Francisco Domingo
 * @date 08/21/2013
 * @email victor_defran@yahoo.es
 */
public class SignActivity extends Activity implements OnClickListener {
	
	private Button signButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		
		signButton = (Button) findViewById(R.id.btn_sign);
		
		signButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn_sign:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
	    default:
	    	break;
		}
	}

}
