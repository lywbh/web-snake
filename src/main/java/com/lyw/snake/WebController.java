package com.lyw.snake;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/game")
    public String game(Model model) {
        String snakeId = GameLoop.snakeGameMap.joinGame();
        model.addAttribute("snakeId", snakeId);
        return "snake";
    }

}
