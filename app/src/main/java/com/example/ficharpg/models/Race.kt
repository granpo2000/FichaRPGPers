package models

enum class Race(val bonuses: Map<String, Int>, val subRaces: List<SubRace>?) {
    HUMANO(mapOf("Força" to 1, "Constituição" to 1, "Destreza" to 1, "Sabedoria" to 1, "Inteligência" to 1, "Carisma" to 1), null),
    ANAO(mapOf("Força" to 3, "Constituição" to 3), listOf(SubRace.COLINA, SubRace.MONTANHA)),
    ELFO(mapOf("Destreza" to 3, "Inteligência" to 3), listOf(SubRace.ALTO, SubRace.FLORESTA, SubRace.NEGRO)),
    HALFLING(mapOf("Sabedoria" to 3, "Carisma" to 3), listOf(SubRace.PE_PEQUENO, SubRace.ROBUSTO)),
    GNOMO(mapOf("Sabedoria" to 3, "Inteligência" to 3), listOf(SubRace.FLORESTA, SubRace.ROCHOSO)),
    MEIO_ELFO(mapOf("Constituição" to 3, "Destreza" to 3), null),
    MEIO_ORC(mapOf("Força" to 4, "Constituição" to 2), null),
    TIEFLING(mapOf("Destreza" to 3, "Constituição" to 3), null);

    companion object {
        // Método para obter uma Race pelo nome
        fun fromName(name: String): Race? = values().find { it.name.equals(name, ignoreCase = true) }
    }
}

