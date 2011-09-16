package com.wingman;

import com.wingman.models.PersonModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ToggleButton;

public class AddUserActivity extends Activity{
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.adduser);

		// Add comment to the person
		//pm.setCOMMENT("ROFLCOPTER LUJERRRRRR");
		
		//i.putExtra("PersonModel", pm);
		//setResult(RESULT_OK, i);
		//finish();
		final Button buttonOk = (Button) findViewById(R.id.buttonOk);
		final Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
		
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = getIntent();
				Bundle extras = i.getExtras();
				
				PersonModel orgPm = (PersonModel) extras.get("PersonModel");
				PersonModel pm = new PersonModel(orgPm.getPhoto(), orgPm.getSelectedFace());
				
				pm.setID_DESCRIPTION(orgPm.getID_DESCRIPTION());
				pm.setID_PERSON(orgPm.getID_PERSON());
				pm.setIdTag(orgPm.getIdTag());
				pm.setDate(orgPm.getDate());
				EditText firstNameText = (EditText) findViewById(R.id.editText1);
				EditText lastNameText = (EditText) findViewById(R.id.editText2);
				EditText descriptionText = (EditText) findViewById(R.id.editText3);
				RatingBar rating = (RatingBar) findViewById(R.id.ratingBar1);
				ToggleButton toggleSingle = (ToggleButton) findViewById(R.id.toggleButton1);
				
				// Add the user input to the PersonModel
				pm.setFirstName(firstNameText.getText().toString());
				pm.setLastName(lastNameText.getText().toString());
				pm.setComment(descriptionText.getText().toString());
				pm.setRating(rating.getRating());
				
				if(toggleSingle.isChecked()){
					pm.setSingle(1);
				} else {
					pm.setSingle(0);
				}
				
				Intent myIntent = new Intent();
				myIntent.putExtra("UserInput", pm);
				setResult(RESULT_OK, myIntent);
				finish();
			}
		});
		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
