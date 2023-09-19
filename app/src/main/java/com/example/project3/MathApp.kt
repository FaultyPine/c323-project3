package com.example.project3

import android.util.Log
import java.lang.Math.abs
import kotlin.random.Random

enum class DifficultyLevel
{
    EASY, MEDIUM, HARD
}
enum class OperationMode
{
    ADDITION, MULTIPLICATION, DIVISION, SUBTRACTION
}

class OperandPair(f: Float, s: Float) {
    var first = f
    var second = s
}

class MathApp {

    var difficultyLevel = DifficultyLevel.EASY
    var operationMode = OperationMode.ADDITION
    var numQuestions = 1
    var operandList: ArrayList<OperandPair> = ArrayList()
    var numCorrect = 0

    fun Reset()
    {
        operandList.clear()
        numCorrect = 0
    }

    fun Float.sameValueAs(other: Float) : Boolean {
        return (abs(this-other) < 0.01)
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
        val instance:MathApp by lazy {
            MathApp()
        }
    }
}