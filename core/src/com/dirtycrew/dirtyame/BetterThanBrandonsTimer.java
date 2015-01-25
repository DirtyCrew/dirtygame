package com.dirtycrew.dirtyame;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by sturm on 1/24/15.
 */
public class BetterThanBrandonsTimer {

    private class Pair {
        long duration;
        long start;

        public Pair(long duration, long start) {
            this.duration = duration;
            this.start = start;
        }
    }

    public interface TimerListener {
        public void onTimerExpired();
    }


    private final Map<TimerListener, Pair> timers;


    public BetterThanBrandonsTimer() {
        timers = new HashMap<TimerListener, Pair>();
    }

    public void startTimer(long duration, TimerListener callback) {
        timers.put(callback, new Pair(duration, System.currentTimeMillis()));
    }

    public void update(float delta) {
        List<TimerListener> toRemove = new ArrayList<TimerListener>();
        for(Map.Entry<TimerListener, Pair> entry : timers.entrySet()) {
            Pair p = entry.getValue();
            if(System.currentTimeMillis() - p.start > p.duration) {
                entry.getKey().onTimerExpired();
                toRemove.add(entry.getKey());
            }
        }

        for(TimerListener listener : toRemove) {
            timers.remove(listener);
        }

    }
}
