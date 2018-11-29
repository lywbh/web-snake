package com.lyw.snake;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
@ServerEndpoint(value = "/game/snake")
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
    public void onMessage(String message, Session session) {
        JSONObject jsonMsg = JSONObject.parseObject(message);
        String snakeId = jsonMsg.getString("snakeId");
        if (snakeId == null) {
            return;
        }
        Snake mySnake = GameLoop.snakeGameMap.getSnake(snakeId);
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
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    private void send(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void broadcast(String message) {
        for (SnakeHandleSocket item : socketSet) {
            try {
                item.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
