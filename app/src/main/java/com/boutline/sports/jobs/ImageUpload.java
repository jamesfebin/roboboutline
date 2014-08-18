package com.boutline.sports.jobs;

import android.util.Log;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotLoggedIn;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 13/07/14.
 */
public class ImageUpload extends Job  {
    public static final int PRIORITY = 1;
    private String TAG="UpdateProfile Job";
    JobManager jobManager;
    String filepath;
    String SASURL;

    public ImageUpload(String SASURL,String filepath)
    {

        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("SettingsPageUpdate"));
        this.filepath = filepath;
        this.SASURL = SASURL;
    }



    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        FileInputStream fis = new FileInputStream(filepath);
        int bytesRead = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        while ((bytesRead = fis.read(b)) != -1) {
            bos.write(b, 0, bytesRead);
        }
        byte[] bytes = bos.toByteArray();

        // Post our image data (byte array) to the server
        URL url = new URL(SASURL);
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
            Log.e("Success", "success" + urlConnection.getResponseMessage());

        }
        else
        {
            Log.e("Failed","failed"+urlConnection.getResponseMessage());
        }



    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

        MyDDPState.getInstance().connectIfNeeded();


    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

        return true;
    }

    @Override
    protected int getRetryLimit() {

        return 25;
    }


}
