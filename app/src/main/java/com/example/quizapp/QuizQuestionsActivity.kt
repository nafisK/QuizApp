package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {


    //Create global variables for the views in the layout
    private var progressBar: ProgressBar?=null
    private var tvProgress: TextView? = null
    private var tvQuestion:TextView? = null
    private var ivImage: ImageView? = null
    private var tvOptionOne:TextView? = null
    private var tvOptionTwo:TextView? = null
    private var tvOptionThree:TextView? = null
    private var tvOptionFour:TextView? = null
    private var buttonSubmit: Button? = null
    /**
     * This function is auto created by Android when the Activity Class is created.
     */


    private var mCurrentPosition: Int = 1 // Default and the first question position
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mUserName : String? = null
    private var mCorrectAnswers : Int = 0
    // END

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar=findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        tvQuestion = findViewById(R.id.tvQuestion)
        ivImage = findViewById(R.id.ivImage)
        tvOptionOne = findViewById(R.id.tvOptionOne)
        tvOptionTwo = findViewById(R.id.tvOptionTwo)
        tvOptionThree = findViewById(R.id.tvOptionThree)
        tvOptionFour = findViewById(R.id.tvOptionFour)

        buttonSubmit = findViewById(R.id.btnSubmit)
        mQuestionsList = Constants.getQuestions()
        // END
        setQuestion()
    }


    private fun setQuestion() {

        val question: Question =


            mQuestionsList!![mCurrentPosition - 1] // Getting the question from the list with the help of current position.
        defaultOptionsView()

        // TODO (STEP 6: Check here if the position of question is last then change the text of the button.)
        // START
        if (mCurrentPosition == mQuestionsList!!.size) {
            buttonSubmit?.text = "FINISH"
        } else {
            buttonSubmit?.text = "SUBMIT"
        }
        // END
        progressBar?.progress =
            mCurrentPosition // Setting the current progress in the progressbar using the position of question
        tvProgress?.text =
            "$mCurrentPosition" + "/" + progressBar?.max // Setting up the progress text

        // Now set the current question and the options in the UI
        tvQuestion?.text = question.question
        ivImage?.setImageResource(question.image)
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.tvOptionOne -> {
                tvOptionOne?.let {
                    selectedOptionView(it, 1)
                }
            }
            R.id.tvOptionTwo -> {
                tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                }
            }
            R.id.tvOptionThree -> {
                tvOptionThree?.let {
                    selectedOptionView(it, 3)
                }
            }
            R.id.tvOptionFour -> {
                tvOptionFour?.let {
                    selectedOptionView(it, 4)
                }
            }

            // TODO(STEP 2: Adding a click event for submit button. And change the questions and check the selected answers.)
            // START
            R.id.btnSubmit->{
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            Toast.makeText(this@QuizQuestionsActivity, "You have successfully completed the quiz.", Toast.LENGTH_SHORT).show()
                            val i  = Intent(this, ResultActivity::class.java)
                            i.putExtra(Constants.USER_NAME, mUserName)
                            i.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            i.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(i)
                            finish()
                        }
                    }
                } else {

                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    // This is to check if the answer is wrong
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }
                    // This is for correct answer
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    if (mCurrentPosition == mQuestionsList!!.size) {
                        buttonSubmit?.text = "FINISH"
                    } else {
                        buttonSubmit?.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    // TODO (STEP 3: Create a function for answer view.)
    // START
    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tvOptionOne?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            3 -> {
                tvOptionThree?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }


    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        tvOptionOne?.let {
            options.add(0, it)
        }
        tvOptionTwo?.let {
            options.add(1, it)
        }
        tvOptionThree?.let {
            options.add(2, it)
        }
        tvOptionFour?.let {
            options.add(3,it)
        }
        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }
}
// END