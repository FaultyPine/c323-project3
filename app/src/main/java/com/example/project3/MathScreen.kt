package com.example.project3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import android.media.MediaPlayer
import android.widget.Toast


/*private lateinit var mediaPlayer1: MediaPlayer
private lateinit var mediaPlayer2: MediaPlayer
private lateinit var toast1: Toast
private lateinit var toast2: Toast*/


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


    /*private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var toast1: Toast
    private lateinit var toast2: Toast*/
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
            navController.navigate(R.id.action_mathScreen_to_mathEndScreen)
        }
    }
    fun ToStartScreenWithGrade()
    {
        val mathapp = MathApp.instance
        val numCorrect = mathapp.numCorrect
        val numQuestions = mathapp.numQuestions
        val operationMode = mathapp.operationMode
        val fm = parentFragmentManager
        if (fm != null)
        {
            MathApp.instance.Reset()
            val startScreen = fm.findFragmentById(R.id.nav_host_fragment) as MathScreen
            val navController = startScreen.findNavController()
            val action = MathScreenDirections.actionMathScreenToStartScreen(numQuestions, numCorrect, operationMode)
            navController.navigate(action)
            //val bundle = bundleOf("numCorrect" to numCorrect, "numQuestions" to numQuestions)
            //navController.navigate(R.id.action_mathScreen_to_startScreen, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_math_screen, container, false)
        val answerText = view.findViewById<TextInputEditText>(R.id.textInputEditText)
        val operand1Text = view.findViewById<TextView>(R.id.textOp1)
        val operand2Text = view.findViewById<TextView>(R.id.textOp2)
        val operationText = view.findViewById<TextView>(R.id.textOperation)
        val mathapp = MathApp.instance

        val firstOpPair = mathapp.operandList.last()
        operand1Text.setText(firstOpPair.first.toInt().toString())
        operand2Text.setText(firstOpPair.second.toInt().toString())

        var operationStr = ""
        if (mathapp.operationMode == OperationMode.ADDITION) operationStr = "+"
        if (mathapp.operationMode == OperationMode.MULTIPLICATION) operationStr = "*"
        if (mathapp.operationMode == OperationMode.DIVISION) operationStr = "/"
        if (mathapp.operationMode == OperationMode.SUBTRACTION) operationStr = "-"
        operationText.setText(operationStr)


        // end this screen
        val doneButton = view.findViewById<Button>(R.id.bDone)
        //val toast1: Toast = Toast.makeText(context, "Correct. Good Work!",Toast.LENGTH_SHORT)
        //val toast2: Toast = Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT)
        val mediaPlayer1 = MediaPlayer.create(context, R.raw.right)
        val mediaPlayer2 = MediaPlayer.create( context, R.raw.wrong)
        doneButton.setOnClickListener {
            var numCorrect = mathapp.numCorrect
            var lastCorrect = 0

            if (mathapp.ProcessAnswer(answerText.text.toString()))
            {
                //ToEndScreen()
                //MathScreenDirections.actionMathScreenToStartScreen()
                lastCorrect = mathapp.numCorrect
                ToStartScreenWithGrade()
            }
            else
            {
                lastCorrect = mathapp.numCorrect
                //val toast1: Toast = Toast.makeText(context, "Correct. Good Work!",Toast.LENGTH_SHORT)
                //val toast2: Toast = Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT)
                //val mediaPlayer1 = MediaPlayer.create(context, R.raw.right)
                //val mediaPlayer2 = MediaPlayer.create( context, R.raw.wrong)
                val nextOperands = mathapp.operandList.last()
                operand1Text.setText(nextOperands.first.toInt().toString())
                operand2Text.setText(nextOperands.second.toInt().toString())
            }

            if (lastCorrect > numCorrect ){
                println("pooop")
                val toast1 = Toast.makeText(context, "Correct. Good Work!",Toast.LENGTH_SHORT)
                toast1.show()
                mediaPlayer1.start()
            }
            else {
                println("shoe")
                val toast2 = Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT)
                toast2.show()

                mediaPlayer2.start()
            }

            answerText.setText("")

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