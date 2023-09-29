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



    companion object {
        val instance:MathApp by lazy {
            MathApp()
        }
    }
}