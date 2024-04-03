package com.example.simpleboggle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score
    private val _newGameTrigger = MutableLiveData<Boolean>()
    val newGameTrigger: LiveData<Boolean> = _newGameTrigger

    private var currentScore = 0

    fun updateScore(newScore: Int) {
        currentScore += newScore
        _score.value = currentScore
    }

    fun getScore(): Int {
        return currentScore
    }

    fun startNewGame() {
        _newGameTrigger.value = true
    }

    fun resetNewGameTrigger() {
        _newGameTrigger.value = false
    }


}