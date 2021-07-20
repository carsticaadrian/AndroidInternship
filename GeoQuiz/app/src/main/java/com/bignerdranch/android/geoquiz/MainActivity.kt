package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var previousButton: ImageButton

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var answeredQuestion = mutableListOf<Question>()

    private var currentIndex = 0
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        previousButton = findViewById(R.id.previous_button)

        setupClickListeners()
        updateQuestion()
    }

    private fun setupClickListeners() {
        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        previousButton.setOnClickListener {
            setCurrentIndex()
            updateQuestion()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        if (questionBank[currentIndex] in answeredQuestion) {
            disableButtons()
        } else {
            enableButtons()
        }

        if (answeredQuestion.size == questionBank.size) {
            showProgress()
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            correctAnswers++
        } else {
            messageResId = R.string.incorrect_toast
        }

        disableButtons()

        answeredQuestion.add(questionBank[currentIndex])

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun disableButtons() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }

    private fun enableButtons() {
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }

    private fun showProgress() {
        val quizProgress: Float = correctAnswers / questionBank.size.toFloat() * 100

        Toast.makeText(
            this,
            "Congratulation! Your score is $quizProgress%",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setCurrentIndex() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }
}
