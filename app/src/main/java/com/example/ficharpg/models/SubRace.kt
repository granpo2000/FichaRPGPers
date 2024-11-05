package models

enum class SubRace(val bonus: Map<String, Int>) {
    COLINA(mapOf("Constituição" to 1)),
    MONTANHA(mapOf("Força" to 1)),
    ALTO(mapOf("Inteligência" to 1)),
    FLORESTA(mapOf("Sabedoria" to 1)),
    NEGRO(mapOf("Destreza" to 1)),
    PE_PEQUENO(mapOf("Destreza" to 1)),
    ROBUSTO(mapOf("Constituição" to 1)),
    ROCHOSO(mapOf("Inteligência" to 1));

    companion object {
        // Método para encontrar uma SubRace pelo nome
        fun fromName(name: String): SubRace? = values().find { it.name.equals(name, ignoreCase = true) }
    }
}
