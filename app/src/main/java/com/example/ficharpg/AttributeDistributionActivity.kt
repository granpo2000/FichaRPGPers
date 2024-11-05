package com.example.ficharpg

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class AttributeDistributionActivity : AppCompatActivity() {
    private lateinit var strengthSeekBar: SeekBar
    private lateinit var dexteritySeekBar: SeekBar
    private lateinit var constitutionSeekBar: SeekBar
    private lateinit var intelligenceSeekBar: SeekBar
    private lateinit var wisdomSeekBar: SeekBar
    private lateinit var charismaSeekBar: SeekBar
    private lateinit var okButton: Button

    private lateinit var strengthTextView: TextView
    private lateinit var dexterityTextView: TextView
    private lateinit var constitutionTextView: TextView
    private lateinit var intelligenceTextView: TextView
    private lateinit var wisdomTextView: TextView
    private lateinit var charismaTextView: TextView
    private lateinit var pointsCounterTextView: TextView

    private var totalPoints = 27
    private val baseAttributes = mutableMapOf(
        "Força" to 8,
        "Destreza" to 8,
        "Constituição" to 8,
        "Inteligência" to 8,
        "Sabedoria" to 8,
        "Carisma" to 8
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attribute_distribution)

        strengthSeekBar = findViewById(R.id.strengthSeekBar)
        dexteritySeekBar = findViewById(R.id.dexteritySeekBar)
        constitutionSeekBar = findViewById(R.id.constitutionSeekBar)
        intelligenceSeekBar = findViewById(R.id.intelligenceSeekBar)
        wisdomSeekBar = findViewById(R.id.wisdomSeekBar)
        charismaSeekBar = findViewById(R.id.charismaSeekBar)
        okButton = findViewById(R.id.okButton)

        strengthTextView = findViewById(R.id.strengthTextView)
        dexterityTextView = findViewById(R.id.dexterityTextView)
        constitutionTextView = findViewById(R.id.constitutionTextView)
        intelligenceTextView = findViewById(R.id.intelligenceTextView)
        wisdomTextView = findViewById(R.id.wisdomTextView)
        charismaTextView = findViewById(R.id.charismaTextView)
        pointsCounterTextView = findViewById(R.id.pointsCounterTextView)

        val raceBonuses = intent.getSerializableExtra("raceBonuses") as HashMap<String, Int>
        val subRaceBonuses = intent.getSerializableExtra("subRaceBonuses") as? HashMap<String, Int>
        val classBonuses = intent.getSerializableExtra("classBonuses") as HashMap<String, Int>

        applyBonuses(raceBonuses)
        subRaceBonuses?.let { applyBonuses(it) }
        applyBonuses(classBonuses)

        setupSeekBar(strengthSeekBar, "Força", strengthTextView)
        setupSeekBar(dexteritySeekBar, "Destreza", dexterityTextView)
        setupSeekBar(constitutionSeekBar, "Constituição", constitutionTextView)
        setupSeekBar(intelligenceSeekBar, "Inteligência", intelligenceTextView)
        setupSeekBar(wisdomSeekBar, "Sabedoria", wisdomTextView)
        setupSeekBar(charismaSeekBar, "Carisma", charismaTextView)

        updatePointsCounter()

        okButton.setOnClickListener {
            val finalAttributes = baseAttributes.toMutableMap()

            finalAttributes["Força"] = baseAttributes["Força"]!! + strengthSeekBar.progress
            finalAttributes["Destreza"] = baseAttributes["Destreza"]!! + dexteritySeekBar.progress
            finalAttributes["Constituição"] = baseAttributes["Constituição"]!! + constitutionSeekBar.progress
            finalAttributes["Inteligência"] = baseAttributes["Inteligência"]!! + intelligenceSeekBar.progress
            finalAttributes["Sabedoria"] = baseAttributes["Sabedoria"]!! + wisdomSeekBar.progress
            finalAttributes["Carisma"] = baseAttributes["Carisma"]!! + charismaSeekBar.progress

            val intent = Intent(this, CharacterSheetActivity::class.java).apply {
                putExtra("finalAttributes", HashMap(finalAttributes))
                putExtra("name", intent.getStringExtra("name"))
                putExtra("race", intent.getStringExtra("race"))
                putExtra("subRace", intent.getStringExtra("subRace"))
                putExtra("class", intent.getStringExtra("class"))
            }
            startActivity(intent)
        }
    }

    private fun setupSeekBar(seekBar: SeekBar, attribute: String, textView: TextView) {
        seekBar.max = 7
        seekBar.progress = baseAttributes[attribute]!! - 8
        textView.text = "$attribute: ${seekBar.progress + 8}"

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val oldValue = textView.text.toString().split(": ")[1].toInt()
                val newValue = progress + 8

                if (newValue > oldValue && totalPoints > 0) {
                    textView.text = "$attribute: $newValue"

                    totalPoints -= (newValue - oldValue)
                } else if (newValue < oldValue) {
                    if (newValue >= 8) {
                        textView.text = "$attribute: $newValue"

                        totalPoints += (oldValue - newValue)
                    } else {
                        seekBar?.progress = oldValue - 8
                    }
                }

                updatePointsCounter()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updatePointsCounter() {
        pointsCounterTextView.text = "Pontos restantes: $totalPoints"
    }

    private fun applyBonuses(bonuses: Map<String, Int>) {
        for ((attribute, bonus) in bonuses) {
            baseAttributes[attribute] = baseAttributes[attribute]!! + bonus
        }
    }
}
