package com.dailypractise.demo.controller;

import com.dailypractise.demo.entity.Question;
import com.dailypractise.demo.entity.Quiz;
import com.dailypractise.demo.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@Slf4j
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        log.info("Inside method call - createQuiz()");
        return quizService.createQuiz(quiz);
    }

    @GetMapping
    public List<Quiz> getAllQuizes() {
        log.info("Inside method call - getAllQuizes()");
        return quizService.getAllQuizes();
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable("id") Long quizId) {
        log.info("Inside method call - getQuiz()");
        return quizService.getQuiz(quizId);
    }


}
