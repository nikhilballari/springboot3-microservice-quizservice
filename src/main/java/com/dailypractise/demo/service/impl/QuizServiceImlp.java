package com.dailypractise.demo.service.impl;

import com.dailypractise.demo.entity.Quiz;
import com.dailypractise.demo.repository.QuizRepository;
import com.dailypractise.demo.service.QuestionClient;
import com.dailypractise.demo.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuizServiceImlp implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionClient questionClient;

    public QuizServiceImlp(QuizRepository quizRepository, QuestionClient questionClient) {
        this.quizRepository = quizRepository;
        this.questionClient = questionClient;
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        log.info("Inside method call - createQuiz()");
        return quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> getAllQuizes() {
        log.info("Inside method call - getAllQuizes()");
        List<Quiz> quizzes = quizRepository.findAll();

        List<Quiz> newQuizList = quizzes.stream().map(quiz -> {
            quiz.setQuestions(questionClient.getQuestionsForQuiz(quiz.getId()));
            return quiz;
        }).collect(Collectors.toList());
        return newQuizList;
    }

    @Override
    public Quiz getQuiz(Long id) {
        log.info("Inside method call - getQuiz()");
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setQuestions(questionClient.getQuestionsForQuiz(quiz.getId()));
        return quiz;
    }
}
