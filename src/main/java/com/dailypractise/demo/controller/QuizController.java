package com.dailypractise.demo.controller;

import com.dailypractise.demo.entity.Question;
import com.dailypractise.demo.entity.Quiz;
import com.dailypractise.demo.service.QuizService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@Slf4j
public class QuizController {

    private final QuizService quizService;
    private static final String GETALLQUIZESCB = "getAllQuizes";
    private static final String GETSINGLEQUIZCB = "getSingleQuiz";
    private static final String GETALLQUIZESRETRY = "retryFallback";
    private static final String GETALLQUIZESRATELIMITER = "rateLimiterFallback";

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        log.info("Inside method call - createQuiz()");
        return quizService.createQuiz(quiz);
    }

    @GetMapping
    @CircuitBreaker(name = GETALLQUIZESCB, fallbackMethod = "getAllQuizesFallBack")
//    @Retry(name = GETALLQUIZESRETRY)
//    @RateLimiter(name = GETALLQUIZESRATELIMITER, fallbackMethod = "rateLimiterFallback")
    public List<Quiz> getAllQuizes() {
        log.info("Inside method call - getAllQuizes()");
        return quizService.getAllQuizes();
    }

    public List<Quiz> rateLimiterFallback(Exception exception) {
        log.info("Fall back method for getAllQuizes() in case rate limiter criteria is exhausted :- {}", exception.getMessage());
        Question.builder().questionId(1L).question("Fallback Question").quizId(1L).build();
        return List.of(Quiz.builder()
                .id(1L)
                .title("FallBack RateLimiter Title")
                .questions(List.of(Question.builder()
                        .questionId(1L)
                        .question("Fallback RateLimiter Question")
                        .quizId(1L).build()))
                .build());
    }

    public List<Quiz> retryFallback(Exception exception) {
        log.info("Fall back method for getAllQuizes() in case retry attempts are exhausted :- {}", exception.getMessage());
        Question.builder().questionId(1L).question("Fallback Question").quizId(1L).build();
        return List.of(Quiz.builder()
                .id(1L)
                .title("FallBack Retry Title")
                .questions(List.of(Question.builder()
                        .questionId(1L)
                        .question("Fallback Retry Question")
                        .quizId(1L).build()))
                .build());
    }

    public List<Quiz> getAllQuizesFallBack(Exception exception) {
        log.info("Fall back method for getAllQuizes() is executed as service is down :- {}", exception.getMessage());
        Question.builder().questionId(1L).question("Fallback Question").quizId(1L).build();
        return List.of(Quiz.builder()
                .id(1L)
                .title("FallBack CircuitBreaker Title")
                .questions(List.of(Question.builder()
                        .questionId(1L)
                        .question("Fallback CircuitBreaker Question")
                        .quizId(1L).build()))
                .build());
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = GETSINGLEQUIZCB, fallbackMethod = "getQuizFallBack")
    public Quiz getQuiz(@PathVariable("id") Long quizId) {
        log.info("Inside method call - getQuiz()");
        return quizService.getQuiz(quizId);
    }

    public Quiz getQuizFallBack(Long quizId, Exception exception) {
        log.info("Fall back method for getQuiz() is executed as service is down {}: ", exception.getMessage());
        return Quiz.builder()
                .id(1L)
                .title("FallBack Title")
                .questions(List.of(Question.builder()
                        .questionId(1L)
                        .question("Fallback Question")
                        .quizId(1L).build()))
                .build();
    }
}
