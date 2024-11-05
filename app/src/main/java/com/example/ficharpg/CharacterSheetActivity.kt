package com.example.ficharpg

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CharacterSheetActivity : AppCompatActivity() {
    private lateinit var characterSheetTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_sheet)

        characterSheetTextView = findViewById(R.id.characterSheetTextView)

        val name = intent.getStringExtra("name")
        val race = intent.getStringExtra("race")
        val subRace = intent.getStringExtra("subRace")
        val characterClass = intent.getStringExtra("class")
        val finalAttributes = intent.getSerializableExtra("finalAttributes") as HashMap<String, Int>

        val characterSheet = buildCharacterSheet(name, race, subRace, characterClass, finalAttributes)
        characterSheetTextView.text = characterSheet
    }

    private fun buildCharacterSheet(name: String?, race: String?, subRace: String?, characterClass: String?, attributes: Map<String, Int>): String {
        val subRaceText = subRace?.let { "Sub-raça: $it\n" } ?: ""
        return """
            Ficha do Personagem:
            
            Nome: $name
            
            Raça: $race
            
            $subRaceText
            Classe: $characterClass
            
            Atributos:
            Força: ${attributes["Força"]}
            Destreza: ${attributes["Destreza"]}
            Constituição: ${attributes["Constituição"]}
            Inteligência: ${attributes["Inteligência"]}
            Sabedoria: ${attributes["Sabedoria"]}
            Carisma: ${attributes["Carisma"]}
        """.trimIndent()
    }
}
