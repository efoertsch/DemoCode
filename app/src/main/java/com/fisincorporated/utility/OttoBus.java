
package com.fisincorporated.utility;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Send events via bus. Note events on ui thread. Modify as needed
 * see http://square.github.io/otto/
 * see http://stackoverflow.com/questions/15431768/how-to-send-event-from-service-to-activity-with-otto-event-bus
 *
 * @author ef1006345
 */
public class OttoBus extends Bus {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    OttoBus.super.post(event);
                }
            });
        }
    }
}
