/** 
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds -
 *  ActionBar does not exist - 
 *  Activity in fullscreen mode 
 *  slide to next should be animated and shown -
 *  Back button closes the app -
 *  Take a screenshot of the activity with and without drawer  
 *  Fonts are defined and assigned -
 *  swiping left should bring next wtactivity2 from right -
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.R;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.jobs.GetSASURL;
import com.boutline.sports.jobs.updateUserProfile;
import com.boutline.sports.models.Message;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.path.android.jobqueue.JobManager;

import java.io.File;

public class SettingsActivity extends Activity implements ImageChooserListener {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	Animation mouseAnim;
    SharedPreferences preferences;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;
    JobManager jobManager;
    ImageView proPic;
    ImageButton Go;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		// Set up UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

        TextView txtEmailId = (TextView)findViewById(R.id.txtEmailId);
        TextView lblLogout = (TextView)findViewById(R.id.lblLogout);
        TextView profileFullName = (TextView)findViewById(R.id.profileFullName);
        TextView lblProfile = (TextView)findViewById(R.id.lblProfile);
        final EditText profileNameEditText = (EditText) findViewById(R.id.profileFullName);
        proPic = (ImageView) findViewById(R.id.imgProPic);
        Go = (ImageButton) findViewById(R.id.btnUpdate);

		//Set up fonts		
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        txtEmailId.setTypeface(btf);
        profileFullName.setTypeface(btf);
        lblProfile.setTypeface(btf);
        lblLogout.setTypeface(tf);

        preferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        String profileImageUrl = preferences.getString("profileImageUrl","");

        if(profileImageUrl!="")
        {
            AQuery aq = new AQuery(getApplicationContext());
            ImageOptions options = new ImageOptions();
            aq.id(proPic).image(profileImageUrl, true, true, 200, 0);
        }

        String fullname = preferences.getString("fullName","");
        String email = preferences.getString("email","");
        profileFullName.setText(fullname);
        txtEmailId.setText(email);

        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        lblLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logout();

            }
        });

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profileNameEditText.getText().toString().matches("")==true)
                {
                    Toast.makeText(getApplicationContext(),"Please enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("fullName",profileNameEditText.getText().toString());
                edit.commit();
                jobManager = MyApplication.getInstance().getJobManager();
                jobManager.addJobInBackground(new updateUserProfile());
                finish();
            }
        });
    }

    private void chooseImage() {

        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(SettingsActivity.this,
                ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            String  filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
        }
    }

    @Override
    public void onImageChosen(final ChosenImage chosenImage) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (chosenImage != null) {
                    final long unixTime = System.currentTimeMillis() / 1000L;
                    proPic.setImageURI(Uri.parse(new File(chosenImage.getFileThumbnail()).toString()));
                    String path = chosenImage.getFilePathOriginal();
                    preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
                    String filename = preferences.getString("boutlineUserId", "");
                    filename=filename+unixTime+".png";
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new GetSASURL(filename,path));
                }
            }
        });

    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(SettingsActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}

    public void logout()
    {
        preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        getApplicationContext().deleteDatabase("boutdb");
        Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
