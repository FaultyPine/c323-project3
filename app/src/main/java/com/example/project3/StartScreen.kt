package com.example.project3

//import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val args: StartScreenArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun SetGradeText(gradeText: TextView, numQuestions: Int, numCorrect: Int, operationMode: OperationMode)
    {
        var operationStr = "Addition"
        if (operationMode == OperationMode.ADDITION) operationStr = "Addition"
        if (operationMode == OperationMode.MULTIPLICATION) operationStr = "Multiplication"
        if (operationMode == OperationMode.DIVISION) operationStr = "Division"
        if (operationMode == OperationMode.SUBTRACTION) operationStr = "Subtraction"

        val gradePercent = numCorrect.toFloat() / numQuestions.toFloat()
        var gradeMsg = ""
        if (gradePercent < 0.8)
        {
            gradeMsg = "You got $numCorrect out of $numQuestions in $operationStr. You need more practice!"
            gradeText.setTextColor(Color.RED)
        }
        else
        {
            gradeMsg = "You got $numCorrect out of $numQuestions in $operationStr. Good work!"
            gradeText.setTextColor(Color.GREEN)
        }
        gradeText.setText(gradeMsg)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start_screen, container, false)

        val prevNumQuestions = args.numQuestions
        val prevNumCorrect = args.numCorrect
        val operationMode = args.operationMode
        val gradeText = view.findViewById<TextView>(R.id.textGrade)
        if (prevNumQuestions != 0)
        {
            // we just came from a math screen, display the grade
            println(prevNumQuestions)
            println(prevNumCorrect)
            SetGradeText(gradeText, prevNumQuestions, prevNumCorrect, operationMode)
        }
        else
        {
            gradeText.setText("")
        }
        //val prevNumQuestions = arguments?.getInt("numQuestions")
        //val prevNumCorrect = arguments?.getInt("numCorrect")


        var mathapp = MathApp.instance

        // more or less questions
        val numQuestionsText = view.findViewById<TextView>(R.id.textNumQuestions)
        numQuestionsText.setText(mathapp.numQuestions.toString())

        val moreQuestionsButton = view.findViewById<Button>(R.id.bMoreQuestions)
        moreQuestionsButton.setOnClickListener {
            mathapp.numQuestions += 1
            numQuestionsText.setText(mathapp.numQuestions.toString())
        }
        val lessQuestionsButton = view.findViewById<Button>(R.id.bLessQuestions)
        lessQuestionsButton.setOnClickListener{
            mathapp.numQuestions = Math.max(1, mathapp.numQuestions-1)
            numQuestionsText.setText(mathapp.numQuestions.toString())
        }
        // ----------------------------------------
        // easy, medium, hard
        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.radioGroupDifficulty)
        difficultyRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.bEasy) {
                mathapp.difficultyLevel = DifficultyLevel.EASY
            } else if (checkedId == R.id.bMedium) {
                mathapp.difficultyLevel = DifficultyLevel.MEDIUM
            } else if (checkedId == R.id.bHard) {
                mathapp.difficultyLevel = DifficultyLevel.HARD
            }
        })
        // ------------------------------------------
        // addition/subtraction/mult/div
        val operationRadioGroup = view.findViewById<RadioGroup>(R.id.radioGroupOperations)
        operationRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.bAddition) {
                mathapp.operationMode = OperationMode.ADDITION
            } else if (checkedId == R.id.bMultiplication) {
                mathapp.operationMode = OperationMode.MULTIPLICATION
            } else if (checkedId == R.id.bDivision) {
                mathapp.operationMode = OperationMode.DIVISION
            } else if (checkedId == R.id.bSubtraction) {
                mathapp.operationMode = OperationMode.SUBTRACTION
            }
        })

        // -------------------------------------------
        // start button
        val startButton = view.findViewById<Button>(R.id.bStart)
        startButton.setOnClickListener {
            val operationButtonId = operationRadioGroup.checkedRadioButtonId
            if (operationButtonId == R.id.bAddition) {
                mathapp.operationMode = OperationMode.ADDITION
            } else if (operationButtonId == R.id.bMultiplication) {
                mathapp.operationMode = OperationMode.MULTIPLICATION
            } else if (operationButtonId == R.id.bDivision) {
                mathapp.operationMode = OperationMode.DIVISION
            } else if (operationButtonId == R.id.bSubtraction) {
                mathapp.operationMode = OperationMode.SUBTRACTION
            }
            mathapp.StartMathScreen()
            val fm = parentFragmentManager
            if (fm != null)
            {
                val startScreen = fm.findFragmentById(R.id.nav_host_fragment) as StartScreen
                val navController = startScreen.findNavController()
                navController.navigate(R.id.action_startScreen_to_mathScreen)
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}