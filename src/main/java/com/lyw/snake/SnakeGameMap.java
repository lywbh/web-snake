package com.lyw.snake;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
public class SnakeGameMap {

    public static class MapConstant {
        static int EMPTY_BLOCK = 0;
        static int FOOD_BLOCK = 1;
        static int SNAKE_BLOCK = 2;
    }

    private static int mapWidth = 100;
    private static int mapHeight = 100;
    public int[][] gameMap;

    private Set<Snake> snakes;
    private Set<Point> foods;

    private SnakeGameMap() {
        gameMap = new int[mapWidth][mapHeight];
        for (int i = 0; i < mapWidth; ++i) {
            for (int j = 0; j < mapHeight; ++j) {
                gameMap[i][j] = MapConstant.EMPTY_BLOCK;
            }
        }
        snakes = new HashSet<>();
        foods = new HashSet<>();
    }

    public static SnakeGameMap init() {
        return new SnakeGameMap();
    }

    public synchronized void next() {
        // 根据snakes和foods，计算下一帧地图上的情况，生成新的gameMap
        snakesMove();
        snakeImpactDetect();
        foodEatDetect();
    }

    private void snakesMove() {
        // 所有的蛇前进一格
        for (Snake snake : snakes) {
            for (int i = 0; i < snake.getBody().size() - 1; ++i) {
                Point forward = snake.getBody().get(i);
                Point backward = snake.getBody().get(i + 1);
                backward.setX(forward.getX());
                backward.setY(forward.getY());
            }
            Point head = snake.getBody().get(0);
            switch (snake.getDirection()) {
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

    private void snakeImpactDetect() {
        // 蛇头蛇身碰撞检测
        Set<Snake> snake2Destroy = new HashSet<>();
        for (Snake snake : snakes) {
            Point head = snake.getBody().get(0);
            // 判断是否撞到自己
            for (int i = 1; i < snake.getBody().size(); ++i) {
                if (head.equals(snake.getBody().get(i))) {
                    snake2Destroy.add(snake);
                }
            }
            // 判断是否撞到别人
            for (Snake snakeOther : snakes) {
                if (snakeOther != snake) {
                    for (Point otherBody : snakeOther.getBody()) {
                        if (head.equals(otherBody)) {
                            snake2Destroy.add(snake);
                        }
                    }
                }
            }
        }
        for (Snake destroy : snake2Destroy) {
            snakes.remove(destroy);
        }
        // TODO 蛇没了要返回没了的ID，不然前端都不知道自己没了，这个后边再加
    }

    private void foodEatDetect() {
        // 蛇头食物碰撞检测
        Set<Point> food2Destroy = new HashSet<>();
        for (Snake snake : snakes) {
            Point head = snake.getBody().get(0);
            for (Point foodPoint : foods) {
                if (head.equals(foodPoint)) {
                    food2Destroy.add(foodPoint);
                }
            }
        }
        for (Point destroy: food2Destroy) {
            foods.remove(destroy);
        }
    }

    public synchronized String joinGame() {
        Random rand = new Random();
        int snakeX = rand.nextInt(mapHeight);
        int snakeY = rand.nextInt(mapWidth);
        Snake newSnake = new Snake(snakeX, snakeY);
        snakes.add(newSnake);
        return newSnake.getId();
    }

    public Snake getSnake(String id) {
        for (Snake snake : snakes) {
            if (StringUtils.equals(snake.getId(), id)) {
                return snake;
            }
        }
        return null;
    }

}
