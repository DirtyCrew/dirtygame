package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * Created by sturm on 1/23/15.
 */
public class DLog {
    enum Channel {
        GAME,
        GRAPHICS,
        PHYSICS
    }


    static {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public static void debug(String msg, Object... objs) {
        debug(Channel.GAME, msg, objs);
    }
    public static void debug(Channel channel, String msg, Object... objs) {
        String tag = channel.name();
        Gdx.app.debug(tag, buildMessage(msg, objs));
    }

    public static void error(String msg, Object... objs) {
        error(Channel.GAME, msg, objs);
    }

    public static void error(Channel channel, String msg, Object... objs) {
        String tag = channel.name();
        Gdx.app.error(tag, buildMessage(msg, objs));
    }

    private static String buildMessage(String message, Object... objects) {
        int start = 0;
        int index = 0;

        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < objects.length; ++i) {
            index = message.indexOf("{}", start);
            if(index == -1) {
                break;
            }
            buffer.append(message.substring(start, index));
            String objString = objects[i].toString();
            buffer.append(objString);
            start = index+2;
        }

        buffer.append(message.substring(start, message.length()));
        return buffer.toString();
    }

}
