package com.example.recipeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class for Volley requests
 */
public class VolleySingleton {
    /** instance of VolleySingleton (needed for any Singleton class) */
    private static VolleySingleton instance;
    /** queue of requests for Volley */
    private RequestQueue requestQueue;
    /** ImageLoader for Volley requests */
    private ImageLoader imageLoader;
    /** Context for Singleton */
    private static Context ctx;

    /**
     * Constructor for VolleySingleton
     * (private because it is a Singleton class)
     * @param context given context
     */
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * Gets the instance of VolleySingleton, if instance is null
     * construct new VolleySingleton with given context
     * @param context given context
     * @return instance
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * Gets current RequestQueue
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds request to the queue
     * @param req request to be added to the queue
     * @param <T> type of request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Getter for imageLoader
     * @return imageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

