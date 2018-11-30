package com.lyw.snake.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by CreditEase.
 * User: yiweiliang1
 * Date: 2018/8/9
 */
public class AsyncTaskUtils {

    public static void run(TimerTask r) {
        run(r, 0, TimeUnit.SECONDS);
    }

    public static void run(TimerTask r, long delay, TimeUnit timeUnit) {
        new Timer().schedule(r, timeUnit.toMillis(delay));
    }

}
