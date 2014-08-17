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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.jobs.updateUserProfile;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.path.android.jobqueue.JobManager;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateProfileActivity extends Activity implements ImageChooserListener{

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    private static final int SELECT_PHOTO = 100;
    ImageView proPic;
    String path;
    private Uri mImageUri;
    SharedPreferences preferences;
    private BroadcastReceiver mReceiver = null;
    Uri selectedImage;
    InputStream inpStream;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;
    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createprofile);

        preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        proPic = (ImageView) findViewById(R.id.imgProPic);
        final EditText profileNameEditText = (EditText) findViewById(R.id.profileFullName);
        ImageButton continueButton = (ImageButton) findViewById(R.id.btnContinue);
        TextView lblProfile = (TextView)findViewById(R.id.lblProfile);
        TextView lblProfilePic = (TextView)findViewById(R.id.lblProfilePic);

        //Set up fonts
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        jobManager = MyApplication.getInstance().getJobManager();
        profileNameEditText.setTypeface(tf);
        lblProfile.setTypeface(btf);
        lblProfilePic.setTypeface(tf);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("fullName",profileNameEditText.getText().toString());
                edit.commit();
                jobManager.addJobInBackground(new updateUserProfile());
                if(getIntent().hasExtra("from"))
                {
                    finish();
                }
                Intent intent = new Intent(CreateProfileActivity.this,ConversationActivity.class);
                intent.putExtra("conversationId","Q83GjTwRCk4FNTSEJ");
                startActivity(intent);
                finish();
            }
        });

        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });



    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(CreateProfileActivity.this,
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

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals("SASURL"))
                {
                    Log.e("SASURL",intent.getExtras().getString("SASURL"));
                    String[] imageUrlArray = intent.getExtras().getString("SASURL").split("\\?");
                    String imageUrl = imageUrlArray[0];
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("profileImageUrl",imageUrl);
                    edit.commit();
                    (new ImageUploaderTask(intent.getExtras().getString("SASURL"),path)).execute();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("SASURL"));
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
    protected void onPause() {
        super.onPause();

        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onImageChosen(final ChosenImage chosenImage) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (chosenImage != null) {

                    proPic.setImageURI(Uri.parse(new File(chosenImage.getFileThumbnail()).toString()));
                    path = chosenImage.getFilePathOriginal();
                    preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

                    String filename = preferences.getString("boutlineUserId", "");
                    filename=filename+".png";
                    MyDDPState.getInstance().getSASURL(filename);

                }
            }
        });

    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(CreateProfileActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    class ImageUploaderTask extends AsyncTask<Void, Void, Boolean> {
            private String mUrl;
            private String path;
            public ImageUploaderTask(String url,String imagePath) {
            mUrl = url;
            path = imagePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                FileInputStream fis = new FileInputStream(path);
                int bytesRead = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                while ((bytesRead = fis.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }
                byte[] bytes = bos.toByteArray();

                // Post our image data (byte array) to the server
                URL url = new URL(mUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.addRequestProperty("x-ms-blob-type", "BlockBlob");
                urlConnection.addRequestProperty("Content-Type", "image/png");
                urlConnection.setRequestProperty("Content-Length", "" + bytes.length);

                // Write image data to server
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.write(bytes);
                wr.flush();
                wr.close();
                int response = urlConnection.getResponseCode();
                //If we successfully uploaded, return true
                if (response == 201
                        && urlConnection.getResponseMessage().equals("Created")) {
                    Log.e("Success","success"+urlConnection.getResponseMessage());

                    return true;
                }
                else
                {
                    Log.e("Failed","failed"+urlConnection.getResponseMessage());
                }
            } catch (Exception ex) {
                Log.e("IMAGE UPLOAD", ex.getMessage());
            }
            return false;
        }
    }
}
