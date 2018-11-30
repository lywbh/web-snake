package com.lyw.snake.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyw.snake.GameLoop;
import com.lyw.snake.object.Snake;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
@ServerEndpoint(value = "/snake")
@Component
public class SnakeHandleSocket {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<SnakeHandleSocket> socketSet = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        socketSet.add(this);
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        socketSet.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonMsg = JSONObject.parseObject(message);
        String snakeId = jsonMsg.getString("snakeId");
        Snake mySnake = GameLoop.snakeGameMap.getSnake(snakeId);
        if (mySnake == null) {
            return;
        }
        String action = jsonMsg.getString("action");
        switch (action) {
            case "38":
                if (mySnake.getDirection() != Snake.SnakeConstant.DIRECTION_DOWN) {
                    mySnake.setNextDirection(Snake.SnakeConstant.DIRECTION_UP);
                }
                break;
            case "39":
                if (mySnake.getDirection() != Snake.SnakeConstant.DIRECTION_LEFT) {
                    mySnake.setNextDirection(Snake.SnakeConstant.DIRECTION_RIGHT);
                }
                break;
            case "40":
                if (mySnake.getDirection() != Snake.SnakeConstant.DIRECTION_UP) {
                    mySnake.setNextDirection(Snake.SnakeConstant.DIRECTION_DOWN);
                }
                break;
            case "37":
                if (mySnake.getDirection() != Snake.SnakeConstant.DIRECTION_RIGHT) {
                    mySnake.setNextDirection(Snake.SnakeConstant.DIRECTION_LEFT);
                }
                break;
        }
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    private void sendAsync(String message) {
        try {
            this.session.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            // 发送失败的话直接忽略
        }
    }

    public static void broadcast(String message) {
        for (SnakeHandleSocket item : socketSet) {
            item.sendAsync(message);
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        SnakeHandleSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        SnakeHandleSocket.onlineCount--;
    }

}
