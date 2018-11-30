package com.lyw.snake.object;

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
    private SnakeConstant nextDirection;

    public Snake(int headX, int headY) {
        this(new Point(headX, headY));
    }

    private Snake(Point headPoint) {
        this(headPoint, 4, SnakeConstant.DIRECTION_RIGHT);
    }

    private Snake(Point headPoint, int length, SnakeConstant direction) {
        this.id = UUID.randomUUID().toString();
        this.body = new ArrayList<>();
        this.body.add(headPoint);
        for (int i = 1; i <= length; ++i) {
            Point nextBody = new Point(headPoint.getX(), headPoint.getY() - i);
            this.body.add(nextBody);
        }
        this.direction = direction;
        this.nextDirection = direction;
    }

    public void move() {
        direction = nextDirection;
        for (int i = body.size() - 1; i > 0; --i) {
            Point forward = body.get(i - 1);
            Point backward = body.get(i);
            backward.setX(forward.getX());
            backward.setY(forward.getY());
        }
        Point head = body.get(0);
        switch (direction) {
            case DIRECTION_UP:
                head.setX(head.getX() - 1);
                break;
            case DIRECTION_RIGHT:
                head.setY(head.getY() + 1);
                break;
            case DIRECTION_DOWN:
                head.setX(head.getX() + 1);
                break;
            case DIRECTION_LEFT:
                head.setY(head.getY() - 1);
                break;
        }
    }

}
