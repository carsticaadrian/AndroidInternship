package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var correctAnswers = 0
    var cheatTokens = 3

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var answeredQuestion = mutableListOf<Question>()

    private var cheatedQuestion = mutableMapOf<Int, Boolean>()

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    fun answerQuestion() {
        answeredQuestion.add(questionBank[currentIndex])
    }

    fun checkQuestion(): Boolean {
        return questionBank[currentIndex] in answeredQuestion
    }

    fun checkProgress(): Boolean {
        return answeredQuestion.size == questionBank.size
    }

    fun getQuizProgress(): Float {
        return correctAnswers / questionBank.size.toFloat() * 100
    }

    fun addCorrectAnswer() {
        correctAnswers++
    }

    fun addCheatedQuestion(isCheater: Boolean) {
        cheatedQuestion[currentIndex] = isCheater
    }

    fun checkCheating(): Boolean {
        return cheatedQuestion[currentIndex] == true
    }

    fun updateTokens(tokens: Int) {
        cheatTokens = tokens
    }
}