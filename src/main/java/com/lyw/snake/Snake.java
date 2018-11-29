package com.lyw.snake;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
@Getter
@Setter
public class Snake {

    public enum SnakeConstant {
        DIRECTION_UP,
        DIRECTION_RIGHT,
        DIRECTION_DOWN,
        DIRECTION_LEFT
    }

    private String id;

    private List<Point> body;
    private SnakeConstant direction;

    public Snake(int headX, int headY) {
        new Snake(new Point(headX, headY));
    }

    private Snake(Point headPoint) {
        new Snake(headPoint, 4, SnakeConstant.DIRECTION_RIGHT);
    }

    private Snake(Point headPoint, int length, SnakeConstant direction) {
        this.id = UUID.randomUUID().toString();
        this.body = new ArrayList<>();
        this.body.add(headPoint);
        for (int i = 1; i <= length; ++i) {
            Point nextBody = new Point(headPoint.getX() - i, headPoint.getY());
            this.body.add(nextBody);
        }
        this.direction = direction;
    }

}
