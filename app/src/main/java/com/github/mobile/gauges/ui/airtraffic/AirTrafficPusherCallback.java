package com.github.mobile.gauges.ui.airtraffic;

import static java.lang.System.currentTimeMillis;

import com.emorym.android_pusher.PusherCallback;
import java.util.Queue;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Callback that delivers pushed air traffic events to a {@link Queue}
 */
public class AirTrafficPusherCallback extends PusherCallback {

    private static final String TAG = "ATPC";

    private final Queue<Hit> hits;

    private final int maxSize;

    /**
     * Create callback that pushes hits to given {@link Queue}
     *
     * @param hits
     *            the {@link Queue} to add hits to
     * @param maxSize
     *            the maximum number of hits to retain in the given {@link Queue}
     */
    public AirTrafficPusherCallback(Queue<Hit> hits, final int maxSize) {
        this.hits = hits;
        this.maxSize = maxSize;
    }

    /**
     * Process hit
     *
     * @param hit
     */
    protected void onHit(final Hit hit) {
        hits.add(hit);
        if (hits.size() > maxSize)
            hits.poll();
    }

    @Override
    public void onEvent(JSONObject eventData) {
        try {
            float longitude = (float) eventData.getDouble("longitude");
            float latitude = (float) eventData.getDouble("latitude");
            String id = eventData.getString("site_id");
            onHit(new Hit(id, longitude, latitude, currentTimeMillis()));
        } catch (JSONException e) {
            Log.d(TAG, "JSON exception", e);
        }
    }
}