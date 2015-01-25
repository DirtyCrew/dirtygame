package com.dirtycrew.dirtyame;


import java.util.*;
import java.util.Map;

/**
 * Created by sturm on 1/24/15.
 */
public class BetterThanBrandonsTimer {

    private class Pair {
        long duration;
        long start;
        int lower;
        int upper;

        public Pair(long duration, long start) {
            this.duration = duration;
            this.start = start;
        }

        public Pair(long duration, long start, int lower, int upper) {
            this.duration = duration;
            this.start = start;
            this.lower = lower;
            this.upper = upper;
        }
    }

    public interface TimerListener {
        public void onTimerExpired();
    }


    private final Map<TimerListener, Pair> timers;
    private final Map<TimerListener, Pair> recur;
    private final Map<TimerListener, Pair> recur2;

    public BetterThanBrandonsTimer() {
        timers = new HashMap<TimerListener, Pair>();
        recur = new HashMap<TimerListener, Pair>();
        recur2 = new HashMap<TimerListener, Pair>();
    }

    public void startTimer(long duration, TimerListener callback) {
        timers.put(callback, new Pair(duration, System.currentTimeMillis()));
    }

    public void clearTimer(TimerListener listener) {
        timers.remove(listener);
        recur.remove(listener);
    }

    public void startRecurringTimer(long duration, TimerListener callback) {
        recur2.put(callback, new Pair(duration, System.currentTimeMillis()));
    }


    public void startRecurringRandomTimer(int upper, int lower, TimerListener callback) {
        recur.put(callback, new Pair(new Random().nextInt(upper - lower) + lower, System.currentTimeMillis(), lower, upper));
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

        for(Map.Entry<TimerListener, Pair> entry : recur.entrySet()) {
            Pair p = entry.getValue();
            if(System.currentTimeMillis() - p.start > p.duration) {
                entry.getKey().onTimerExpired();
                p.duration = new Random().nextInt(p.upper - p.lower) + p.lower;
                p.start = System.currentTimeMillis();
            }
        }

        for(Map.Entry<TimerListener, Pair> entry : recur2.entrySet()) {
            Pair p = entry.getValue();
            if(System.currentTimeMillis() - p.start > p.duration) {
                entry.getKey().onTimerExpired();
                p.start = System.currentTimeMillis();
            }
        }
    }
}
