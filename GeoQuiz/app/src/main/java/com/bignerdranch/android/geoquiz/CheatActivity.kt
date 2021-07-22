package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val TAG = "CheatActivity"
private const val KEY_ANSWER = "answer"
private const val KEY_TOKEN = "token"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.bignerdranch.android.geoquiz.answer_is_true"

const val EXTRA_REMAINING_TOKENS =
    "com.bignerdranch.android.geoquiz.remaining_tokens"
const val EXTRA_ANSWER_SHOWN = "com.bignerdbranch.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var deviceAPITextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false
    private var isAnswerShown = false

    private var remainingTokens = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        deviceAPITextView = findViewById(R.id.device_API_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        isAnswerShown = savedInstanceState?.getBoolean(KEY_ANSWER) ?: false
        remainingTokens =
            savedInstanceState?.getInt(KEY_TOKEN) ?: intent.getIntExtra(EXTRA_REMAINING_TOKENS, 3)

        deviceAPITextView.text = "API Level " + Build.VERSION.SDK_INT

        setAnswerShownResult()
        setupListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ANSWER, isAnswerShown)
        outState.putInt(KEY_TOKEN, remainingTokens)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, tokens: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_REMAINING_TOKENS, tokens)
            }
        }
    }

    private fun setupListeners() {
        showAnswerButton.setOnClickListener {
            showAnswer()
        }
    }

    private fun showAnswer() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }

        answerTextView.setText(answerText)
        isAnswerShown = true
        remainingTokens--
        setAnswerShownResult()
    }

    private fun setAnswerShownResult() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_REMAINING_TOKENS, remainingTokens)
        }
        setResult(Activity.RESULT_OK, data)
    }
}