package com.neha.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends Activity {

	private boolean mExtraIsComputerFirst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onRadioButtonClicked(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.radio_computer:
	            if (checked) {
	            	mExtraIsComputerFirst = true;
	            }
	            break;
	        case R.id.radio_human:
	            if (checked) {
	            	mExtraIsComputerFirst = false;
	            }
	            break;
	    }
	}
	
	public void onStartGameClicked(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GameActivity.EXTRA_COMPUTER_FIRST_PLAYER, mExtraIsComputerFirst);
		startActivity(intent);
	}

}
