import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * Created by sturm on 1/23/15.
 */
public class Logger {
    private final static String TAG = "Dirty";
    
    static {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public static void debug(String msg, Object... objs) {
        Gdx.app.debug(TAG, buildMessage(msg, objs));
    }
    public static void error(String msg, Object... objs) {
        Gdx.app.error(TAG, buildMessage(msg, objs));
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
