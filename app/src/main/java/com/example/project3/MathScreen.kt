package com.example.project3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MathScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class MathScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var operandList: ArrayList<OperandPair> = ArrayList()
    var numQuestions = 1
    var difficultyLevel = DifficultyLevel.EASY
    var operationMode = OperationMode.ADDITION
    var numCorrect = 0

    val args: MathScreenArgs by navArgs()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun ToEndScreen()
    {
        val fm = parentFragmentManager
        if (fm != null)
        {
            val startScreen = fm.findFragmentById(R.id.nav_host_fragment) as MathScreen
            val navController = startScreen.findNavController()
            val action = MathScreenDirections.actionMathScreenToMathEndScreen(numCorrect, numQuestions)
            navController.navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        numQuestions = args.numQuestions
        difficultyLevel = args.difficulty
        operationMode = args.operationmode
        Reset()
        StartMathScreen()
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_math_screen, container, false)
        val answerText = view.findViewById<TextInputEditText>(R.id.textInputEditText)
        val operand1Text = view.findViewById<TextView>(R.id.textOp1)
        val operand2Text = view.findViewById<TextView>(R.id.textOp2)
        val operationText = view.findViewById<TextView>(R.id.textOperation)
        val mathapp = MathApp.instance

        val firstOpPair = operandList.last()
        operand1Text.setText(firstOpPair.first.toInt().toString())
        operand2Text.setText(firstOpPair.second.toInt().toString())

        var operationStr = ""
        if (operationMode == OperationMode.ADDITION) operationStr = "+"
        if (operationMode == OperationMode.MULTIPLICATION) operationStr = "*"
        if (operationMode == OperationMode.DIVISION) operationStr = "/"
        if (operationMode == OperationMode.SUBTRACTION) operationStr = "-"
        operationText.setText(operationStr)


        // end this screen
        val doneButton = view.findViewById<Button>(R.id.bDone)
        doneButton.setOnClickListener {
            if (ProcessAnswer(answerText.text.toString()))
            {
                ToEndScreen()
            }
            else
            {
                val nextOperands = operandList.last()
                operand1Text.setText(nextOperands.first.toInt().toString())
                operand2Text.setText(nextOperands.second.toInt().toString())
            }
            answerText.setText("")
        }

        return view
    }

    fun NumberInDifficultyRange() : Float
    {
        var num = 0
        if (difficultyLevel == DifficultyLevel.EASY)
        {
            num = (0 until 10).random()
        }
        else if (difficultyLevel == DifficultyLevel.MEDIUM)
        {
            num = (0 until 25).random()
        }
        else if (difficultyLevel == DifficultyLevel.HARD)
        {
            num = (0 until 50).random()
        }
        return num.toFloat()
    }

    // called when the user hits "Start" on the beginning screen.
    // sets up "numQuestions" number of problems
    fun StartMathScreen()
    {
        for (i in 0 until numQuestions)
        {
            var firstNum = NumberInDifficultyRange()
            var secondNum = NumberInDifficultyRange()
            if (operationMode == OperationMode.DIVISION)
            {
                while (secondNum == 0.0f)
                {
                    secondNum = NumberInDifficultyRange()
                }
            }
            operandList.add(OperandPair(firstNum, secondNum))
        }
    }


    fun Reset()
    {
        operandList.clear()
        numCorrect = 0
    }

    fun Float.sameValueAs(other: Float) : Boolean {
        return (Math.abs(this - other) < 0.01)
    }

    fun ProcessAnswer(answer: String) : Boolean
    {
        val answerNumOrNull = answer.toFloatOrNull()
        if (answerNumOrNull == null)
        {
            Log.w("[WARNING]","User entered non-number")
            return false
        }

        if (operandList.size > 0)
        {
            val opPair: OperandPair = operandList.last()
            val answer = ComputeAnswer(opPair.first, opPair.second)
            if (answer.sameValueAs(answerNumOrNull))
            {
                numCorrect += 1
            }
            operandList.removeLast() // go to next question even if we get it wrong
        }

        return operandList.isEmpty()
    }


    fun ComputeAnswer(operand1: Float, operand2: Float) : Float
    {
        var answer = 0.0f
        if (operationMode == OperationMode.ADDITION)
        {
            answer = operand1 + operand2
        }
        else if (operationMode == OperationMode.MULTIPLICATION)
        {
            answer = operand1 * operand2
        }
        else if (operationMode == OperationMode.DIVISION)
        {
            answer = operand1 / operand2
        }
        else if (operationMode == OperationMode.SUBTRACTION)
        {
            answer = operand1 - operand2
        }
        return answer
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MathScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MathScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}