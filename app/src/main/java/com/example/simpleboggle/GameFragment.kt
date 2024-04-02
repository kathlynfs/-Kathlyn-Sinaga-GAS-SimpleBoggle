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

    private val dictionaryFileName = "dictionary.txt"
    private lateinit var dictionary: List<String>
    private lateinit var letterButtons : List<ToggleButton>
    private lateinit var clearSelectionButton : Button
    private lateinit var submitWordButton : Button
    private lateinit var displayWordInput: TextView
    private val selectedLetters = mutableListOf<String>()


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


        }

    }

    private fun submitWord(selectedLetters: MutableList<String>) {
        val word = selectedLetters.joinToString(separator = "")
        if (isValidWord(word)) {
            Toast.makeText(requireContext(), "That's correct", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "That's incorrect", Toast.LENGTH_SHORT).show()
        }
        clearSelection()
    }

    private fun isValidWord(word : String) : Boolean {
        if (word.length < 4) {
            return false
        }

        val vowels = setOf('a', 'e', 'i', 'o', 'u')
        val vowelCount = word.count { vowels.contains(it) }
        if (vowelCount < 2) {
            return false
        }

        return dictionary.contains(word)
    }

    fun readDictionaryFile(fileName: String): List<String> {
        val assetManager = context?.assets
        val dictionary = mutableListOf<String>()

        try {
            val inputStream = assetManager?.open(fileName)
            val reader = inputStream?.bufferedReader()
            var line: String?
            while (reader?.readLine().also { line = it } != null) {
                dictionary.add(line!!)
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
            val randomLetter = ('A'..'Z').random().toString()
            toggleButton.textOn = randomLetter
            toggleButton.textOff = randomLetter
            toggleButton.text = randomLetter
            toggleButton.tag = index


            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val buttonIndex = toggleButton.tag as Int
                if (isChecked) {
                    toggleButton.isChecked = false
                    if (!isValidMove(buttonIndex)) {
                        Toast.makeText(requireContext(), "You cannot select this letter", Toast.LENGTH_SHORT).show()
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
                if (newR in 0..3 && newC in 0 .. 3) {
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
