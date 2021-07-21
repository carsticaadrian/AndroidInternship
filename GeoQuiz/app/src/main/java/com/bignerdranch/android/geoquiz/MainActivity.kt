package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var previousButton: ImageButton

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        quizViewModel.currentIndex = currentIndex

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
            quizViewModel.moveToNext()
            updateQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("TAG", "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        if (quizViewModel.checkQuestion()) {
            disableButtons()
        } else {
            enableButtons()
        }

        if (quizViewModel.checkProgress()) {
            showProgress()
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.addCorrectAnswer()
        } else {
            messageResId = R.string.incorrect_toast
        }

        disableButtons()

        quizViewModel.answerQuestion()

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
        Toast.makeText(
            this,
            "Congratulation! Your score is ${quizViewModel.getQuizProgress()}%",
            Toast.LENGTH_SHORT
        ).show()
    }
}
