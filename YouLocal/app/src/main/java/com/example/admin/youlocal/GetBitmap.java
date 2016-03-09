package com.example.admin.youlocal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetBitmap extends AsyncTask<String, String, String> {

    private Bitmap bitmap;
    private URL url;

    public GetBitmap(Bitmap bitmap, URL url) {
        this.bitmap = bitmap;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpURLConnection c = (HttpURLConnection) getUrl().openConnection();
            c.setDoOutput(true);
            c.connect();
            InputStream is = c.getInputStream();
            setBitmap(BitmapFactory.decodeStream(is));

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}