package com.swifty.toptoastbar.UrlImage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Swifty on 24/2/16.
 */
public class URLImageParser implements Html.ImageGetter {
    Context c;
    View container;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     *
     * @param t
     * @param c
     */
    public URLImageParser(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    /**
     * Looks up defType resource ID by name. Name alias is supported. If the prefix "local:" is
     * found, the prefix is striped first.
     *
     * @param ctx
     * @param name    of resource. Could be an alias. Could be prefixed by "local:".
     * @param defType resource type
     * @return resource ID or 0 if not found.
     */
    public int getResourceId(Context ctx, String name, String defType) {
        if (ctx == null || name == null || name.isEmpty()) return 0;
        // remove prefix if any
        String prefix = "local:";
        if (name.startsWith(prefix))
            name = name.substring(prefix.length());

        // lookup the drawable id
        return ctx.getResources().getIdentifier(name, defType, ctx.getPackageName());
    }

    public Drawable getDrawable(String source) {
        if (source.startsWith("local:")) {
            Drawable d = c.getResources().getDrawable(getResourceId(c, source, "drawable"));
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
        URLDrawable urlDrawable = new URLDrawable();
        // get the actual source
        ImageGetterAsyncTask asyncTask =
                new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) return;
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0
                    + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageParser.this.container.invalidate();
        }

        /***
         * Get the Drawable from URL
         *
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            InputStream is = null;
            Drawable drawable = null;
            try {
                is = fetch(urlString);
                BitmapFactory.Options options = new BitmapFactory.Options();
                DisplayMetrics dm = c.getResources().getDisplayMetrics();
                options.inDensity = dm.densityDpi;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    drawable = Drawable.createFromStream(is, urlString);
                } else {
                    drawable = Drawable.createFromResourceStream(c.getResources(), new TypedValue(), is, urlString, options);
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            } catch (Exception e) {
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return drawable;
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
