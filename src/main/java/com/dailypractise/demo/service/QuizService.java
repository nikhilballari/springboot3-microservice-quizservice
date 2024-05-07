package com.dailypractise.demo.service;

import com.dailypractise.demo.entity.Quiz;

import java.util.List;

public interface QuizService {

    Quiz createQuiz(Quiz quiz);

    List<Quiz> getAllQuizes();

    Quiz getQuiz(Long id);
}
