package com.lyw.snake.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lyw.
 * User: yiweiliang1
 * Date: 2018/11/29
 */
@Getter
@Setter
@AllArgsConstructor
public class Point {

    private int x;
    private int y;

    public boolean equals(Point point) {
        return this.x == point.x && this.y == point.y;
    }

}
