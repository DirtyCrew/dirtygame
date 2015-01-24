package com.dirtycrew.dirtyame;

/**
 * Created by Kern on 1/24/2015.
 */
public class Timer{

    protected long beginTime;
    protected long waitTimer;
    EventHandler e;
    EventHandler.Event event;

    public Timer(long ms, EventHandler handler,EventHandler.Event rcvEvent)
    {
        beginTime = System.currentTimeMillis();
        event = rcvEvent;
        waitTimer = ms;
        e = handler;
    }

    public void update()
    {
        long endTime = System.currentTimeMillis();

        if((endTime - beginTime) > waitTimer)
        {
            e.fireEvent(event);
        }
    }


}
