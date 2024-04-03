package com.example.simpleboggle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.example.simpleboggle.databinding.FragmentGameBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val dictionaryFileName = "dictionary.txt"
    private lateinit var dictionary: List<String>
    private lateinit var letterButtons : List<ToggleButton>
    private lateinit var clearSelectionButton : Button
    private lateinit var submitWordButton : Button
    private lateinit var displayWordInput: TextView
    private val selectedLetters = mutableListOf<String>()
    private val playerWords = mutableListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bindLetterButtons()
            displayWordInput = root.findViewById(R.id.wordInput)
            clearSelectionButton = root.findViewById(R.id.clearButton)
            clearSelectionButton.setOnClickListener {
                clearSelection()
            }
            submitWordButton = root.findViewById(R.id.submitButton)
            submitButton.setOnClickListener {
                submitWord(selectedLetters)
            }
            dictionary = readDictionaryFile(dictionaryFileName)
            sharedViewModel.newGameTrigger.observe(viewLifecycleOwner) { startNewGameTriggered ->
                if (startNewGameTriggered) {
                    resetGame()
                    sharedViewModel.resetNewGameTrigger()
                }
            }


        }

    }

    private fun resetGame() {
        setLettersOnButtons()
        playerWords.clear()
        val currentScore = sharedViewModel.getScore()
        sharedViewModel.updateScore(-currentScore)
    }

    private fun submitWord(selectedLetters: MutableList<String>) {
        val word = selectedLetters.joinToString(separator = "")
        val points = getPoints(word)
        if (points > 0) {
            Toast.makeText(requireContext(), "That's correct, $points", Toast.LENGTH_SHORT).show()
            sharedViewModel.updateScore(points)
            playerWords.add(word)
        } else if (points == 0) {
            Toast.makeText(requireContext(), "You have previously entered that word, $points", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "That's incorrect, $points", Toast.LENGTH_SHORT).show()
            sharedViewModel.updateScore(points)
        }
        clearSelection()
    }
    
    private fun getPoints(word : String) : Int {
        if (playerWords.contains(word)) {
            return 0
        }

        if (!isValidWord(word)) {
            return -10
        }
        var consonants = 0
        var vowels = 0
        for (char in word) {
            if (char in setOf('A', 'E', 'I', 'O', 'U')) {
                vowels++
            } else {
                consonants++
            }
        }
        return ((vowels*5)+consonants)
    }

    private fun isValidWord(word : String) : Boolean {
        if (word.length < 4) {
            Toast.makeText(requireContext(), "Please submit at least 4 letters", Toast.LENGTH_SHORT).show()
            return false
        }

        val vowels = setOf('A', 'E', 'I', 'O', 'U')
        val vowelCount = word.count { vowels.contains(it) }
        if (vowelCount < 2) {
            Toast.makeText(requireContext(), "Please submit at least 2 vowels", Toast.LENGTH_SHORT).show()
            return false
        }

        return dictionary.contains(word.lowercase())

    }

    fun readDictionaryFile(fileName: String): List<String> {
        val assetManager = context?.assets
        val dictionary = mutableListOf<String>()

        try {
            val inputStream = assetManager?.open(fileName)
            val reader = inputStream?.bufferedReader()
            var line: String?
            while (reader?.readLine().also { line = it } != null) {
                dictionary.add(line!!.lowercase())
            }

            reader?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dictionary
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearSelection() {
        for (button in letterButtons) {
            button.isChecked = false
            button.isEnabled = true
        }
        selectedLetters.clear()
        updateDisplayWord()
    }

    private fun setLettersOnButtons() {
        val commonLetters = listOf(
            'E', 'A', 'R', 'I', 'O', 'T', 'N', 'S', 'L', 'C'
        )
        val uncommonLetters = listOf(
            'M', 'H', 'G', 'B', 'F', 'Y', 'W', 'P', 'U', 'D'
        )
        val rareLetters = listOf(
            'Y', 'V', 'X', 'Z', 'J', 'Q', 'K'
        )
        var randomLetter = 'E'

        for (toggleButton in letterButtons) {
            val randomNumber = (1..100).random()

            if (randomNumber <= 65) {
                randomLetter = commonLetters.random()
            } else if (randomNumber <= 95 ) {
                randomLetter = uncommonLetters.random()
            } else {
                randomLetter = rareLetters.random()
            }

            toggleButton.textOn = randomLetter.toString()
            toggleButton.textOff = randomLetter.toString()
            toggleButton.text = randomLetter.toString()
            toggleButton.isChecked = false
            toggleButton.isEnabled = true
        }
    }

    private fun bindLetterButtons() {
        val toggleButtonIds = arrayOf(
            R.id.boggleLetter1, R.id.boggleLetter2, R.id.boggleLetter3, R.id.boggleLetter4,
            R.id.boggleLetter5, R.id.boggleLetter6, R.id.boggleLetter7, R.id.boggleLetter8,
            R.id.boggleLetter9, R.id.boggleLetter10, R.id.boggleLetter11, R.id.boggleLetter12,
            R.id.boggleLetter13, R.id.boggleLetter14, R.id.boggleLetter15, R.id.boggleLetter16
        )

        val buttonList = mutableListOf<ToggleButton>()

        for ((index, buttonId) in toggleButtonIds.withIndex()) {
            val toggleButton: ToggleButton = binding.root.findViewById(buttonId)
            toggleButton.tag = index


            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val buttonIndex = toggleButton.tag as Int
                if (isChecked) {
                    toggleButton.isChecked = false
                    if (!isValidMove(buttonIndex)) {
                        Toast.makeText(requireContext(), "You may only select connected letters", Toast.LENGTH_SHORT).show()
                    } else {
                        toggleButton.isChecked = true
                        toggleButton.isEnabled = false
                        selectedLetters.add(toggleButton.text.toString())
                        updateDisplayWord()
                    }
                }
            }


            buttonList.add(toggleButton)
        }

        letterButtons = buttonList
        setLettersOnButtons()

    }

    private fun updateDisplayWord() {
        val word = selectedLetters.joinToString(separator = "")
        displayWordInput.text = word
    }

    private fun isNewInput() : Boolean {
        for (button in letterButtons) {
            if (button.isChecked) {
                return false
            }
        }

        return true
    }

    private fun isValidMove(index : Int): Boolean {

        if (isNewInput()) {
            return true
        } else {
            val row = index / 4
            val col = index % 4

            val moves = listOf(
                // diagonals
                Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1),
                // up, down, left right
                Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1)
            )

            for ((moveR, moveC) in moves) {
                val newR = row + moveR
                val newC = col + moveC

                // Check if the adjacent position is within bounds
                if (newR in 0..3 && newC in 0..3) {
                    val adjacentButtonIndex = newR * 4 + newC
                    Log.d("AdjacentButton", "Index: $adjacentButtonIndex}")
                    if (letterButtons[adjacentButtonIndex].isChecked) {
                        return true
                    }
                }
            }
        }
        return false
    }


}
