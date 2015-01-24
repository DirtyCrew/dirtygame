package com.dirtycrew.dirtyame;

import javax.swing.event.EventListenerList;
import java.util.*;

/**
 * Created by Kern on 1/23/2015.
 */
public class EventHandler {

    Map<Class<?>,List<EventListener>> eventHandlers = new HashMap<Class<?>, List<EventListener>>();

    public interface EventListener
    {
        void onEvent(Event e);
    };


    public static class Event
    {
        public String state;

        public String getState()
        {
            return state;
        }

        public void setState(String s)
        {
            state = s;
        }
    };



    public void suscribe(Event e, EventListener listener)
    {
        if(!eventHandlers.containsKey(e.getClass()))
        {
            eventHandlers.put(e.getClass(), new ArrayList<EventListener>());
        }

        eventHandlers.get(e.getClass()).add(listener);
    }

    public void unsuscribe(Event e,EventListener listener)
    {
        if(!eventHandlers.containsKey(e.getClass()))
        {
            return;
        }
        eventHandlers.get(e.getClass()).remove(listener);
    }


    public void fireEvent(Event event)
    {
        for(EventListener listener : eventHandlers.get(event.getClass()))
        {
            listener.onEvent(event);
            DLog.debug("On Event {}", event);
        }
    }



}
