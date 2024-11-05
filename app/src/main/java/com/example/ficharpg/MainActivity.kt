package com.example.ficharpg

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import models.CharacterClass
import models.Race
import models.SubRace
import com.example.ficharpg.R
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var raceSpinner: Spinner
    private lateinit var subRaceSpinner: Spinner
    private lateinit var classSpinner: Spinner
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        raceSpinner = findViewById(R.id.raceSpinner)
        subRaceSpinner = findViewById(R.id.subRaceSpinner)
        classSpinner = findViewById(R.id.classSpinner)
        confirmButton = findViewById(R.id.confirmButton)

        val races = Race.values().map { it.name }
        val raceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, races)
        raceSpinner.adapter = raceAdapter

        raceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRace = Race.values()[position]
                if (selectedRace.subRaces != null) {
                    val subRaces = selectedRace.subRaces.map { it.name }
                    val subRaceAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, subRaces)
                    subRaceSpinner.adapter = subRaceAdapter
                    subRaceSpinner.visibility = View.VISIBLE
                } else {
                    subRaceSpinner.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                subRaceSpinner.visibility = View.GONE
            }
        }

        val classes = CharacterClass.values().map { it.name }
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
        classSpinner.adapter = classAdapter

        confirmButton.setOnClickListener {
            val selectedRace = Race.values()[raceSpinner.selectedItemPosition]
            val selectedSubRace = if (selectedRace.subRaces != null && subRaceSpinner.selectedItemPosition >= 0) {
                selectedRace.subRaces[subRaceSpinner.selectedItemPosition]
            } else null
            val selectedClass = CharacterClass.values()[classSpinner.selectedItemPosition]
            val name = nameEditText.text.toString()

            if (name.isBlank()) {
                Toast.makeText(this, "Por favor, insira o nome do personagem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, AttributeDistributionActivity::class.java).apply {
                putExtra("name", name)
                putExtra("race", selectedRace.name)
                putExtra("subRace", selectedSubRace?.name)
                putExtra("class", selectedClass.name)
                putExtra("raceBonuses", HashMap(selectedRace.bonuses))
                putExtra("subRaceBonuses", selectedSubRace?.bonus?.let { HashMap(it) })
                putExtra("classBonuses", HashMap(selectedClass.bonus))
            }
            startActivity(intent)
        }
    }
}