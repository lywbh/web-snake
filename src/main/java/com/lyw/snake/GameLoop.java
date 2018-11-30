package com.lyw.snake;

import com.alibaba.fastjson.JSONObject;
import com.lyw.snake.controller.SnakeHandleSocket;
import com.lyw.snake.object.SnakeGameMap;

import java.util.concurrent.TimeUnit;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
public class GameLoop {

    public static SnakeGameMap snakeGameMap = SnakeGameMap.init();

    private static void mainLoop() throws InterruptedException {
        while (true) {
            snakeGameMap.next();
            SnakeHandleSocket.broadcast(JSONObject.toJSONString(snakeGameMap.gameMap));
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    public static void start() {
        try {
            System.out.println("游戏初始化...");
            mainLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
