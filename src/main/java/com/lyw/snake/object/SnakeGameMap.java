package com.lyw.snake.object;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
public class SnakeGameMap {

    public static class MapConstant {
        static int EMPTY_BLOCK = 0;
        static int SNAKE_BLOCK = 1;
        static int FOOD_BLOCK = 2;
    }

    private static int mapWidth = 100;
    private static int mapHeight = 100;
    public int[][] gameMap;

    private Set<Snake> snakes;
    private Set<Point> foods;

    private SnakeGameMap() {
        gameMap = new int[mapWidth][mapHeight];
        clearMap();
        snakes = new HashSet<>();
        foods = new HashSet<>();
    }

    public static SnakeGameMap init() {
        SnakeGameMap map = new SnakeGameMap();
        for (int i = 0; i < 30; ++i) {
            map.createFood();
        }
        return map;
    }

    private void clearMap() {
        for (int i = 0; i < mapWidth; ++i) {
            for (int j = 0; j < mapHeight; ++j) {
                gameMap[i][j] = MapConstant.EMPTY_BLOCK;
            }
        }
    }

    public void next() {
        // 根据snakes和foods，计算下一帧地图上的情况，生成新的gameMap
        snakes.forEach(Snake::move);
        snakeImpactDetect();
        foodEatDetect();
        drawMap();
    }

    private void snakeImpactDetect() {
        // 蛇头蛇身碰撞检测
        Set<Snake> snake2Destroy = new HashSet<>();
        for (Snake snake : snakes) {
            Point head = snake.getBody().get(0);
            // 判断是否撞到边界
            if (head.getX() < 0 || head.getX() >= mapWidth || head.getY() < 0 || head.getY() >= mapHeight) {
                snake2Destroy.add(snake);
            }
            // 判断是否撞到自己
            for (int i = 1; i < snake.getBody().size(); ++i) {
                if (head.equals(snake.getBody().get(i))) {
                    snake2Destroy.add(snake);
                }
            }
            // 判断是否撞到别人
            Map<Snake, Snake> winLose = new HashMap<>();
            for (Snake snakeOther : snakes) {
                if (snakeOther != snake) {
                    for (Point otherBody : snakeOther.getBody()) {
                        if (head.equals(otherBody)) {
                            if (snake.getBody().size() > snakeOther.getBody().size()) {
                                // 自己比较长，把对方吃了
                                snake2Destroy.add(snakeOther);
                                winLose.put(snake, snakeOther);
                            } else if (snake.getBody().size() < snakeOther.getBody().size()) {
                                // 自己比较短，被吃了
                                snake2Destroy.add(snake);
                                winLose.put(snakeOther, snake);
                            } else {
                                // 一样长，同归于尽
                                snake2Destroy.add(snake);
                                snake2Destroy.add(snakeOther);
                            }
                        }
                    }
                }
            }
            // 胜者获得败者的长度
            for (Map.Entry<Snake, Snake> wl : winLose.entrySet()) {
                Snake win = wl.getKey();
                Snake lose = wl.getValue();
                Point winTail = win.getBody().get(win.getBody().size() - 1);
                for (int i = 0; i < lose.getBody().size(); ++i) {
                    win.getBody().add(new Point(winTail.getX(), winTail.getY()));
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
            List<Point> snakeBody = snake.getBody();
            for (Point foodPoint : foods) {
                if (foodPoint.equals(snakeBody.get(0))) {
                    food2Destroy.add(foodPoint);
                    Point snakeTail = snakeBody.get(snakeBody.size() - 1);
                    snakeBody.add(new Point(snakeTail.getX(), snakeTail.getY()));
                }
            }
        }
        for (Point destroy : food2Destroy) {
            foods.remove(destroy);
            createFood();
        }
    }

    private void drawMap() {
        clearMap();
        for (Snake snake : snakes) {
            for (Point body : snake.getBody()) {
                gameMap[body.getX()][body.getY()] = MapConstant.SNAKE_BLOCK;
            }
        }
        for (Point food : foods) {
            gameMap[food.getX()][food.getY()] = MapConstant.FOOD_BLOCK;
        }
    }

    public synchronized String joinGame() {
        Random rand = new Random();
        int snakeX = rand.nextInt(mapHeight - 20) + 10;
        int snakeY = rand.nextInt(mapWidth - 20) + 10;
        Snake newSnake = new Snake(snakeX, snakeY);
        snakes.add(newSnake);
        return newSnake.getId();
    }

    private void createFood() {
        Random rand = new Random();
        int foodX = rand.nextInt(mapHeight);
        int foodY = rand.nextInt(mapWidth);
        Point food = new Point(foodX, foodY);
        foods.add(food);
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
