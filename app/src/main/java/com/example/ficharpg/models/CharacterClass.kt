package models

enum class CharacterClass(val bonus: Map<String, Int>) {
    BARBARO(mapOf("Força" to 2)),
    BARDO(mapOf("Carisma" to 2)),
    CLERIGO(mapOf("Sabedoria" to 2)),
    DRUIDA(mapOf("Sabedoria" to 2)),
    GUERREIRO(mapOf("Força" to 1, "Destreza" to 1)),
    MONGE(mapOf("Destreza" to 2)),
    PALADINO(mapOf("Força" to 1, "Sabedoria" to 1)),
    PATRULHEIRO(mapOf("Destreza" to 2)),
    LADINO(mapOf("Destreza" to 2)),
    FEITICEIRO(mapOf("Inteligência" to 1, "Sabedoria" to 1)),
    BRUXO(mapOf("Inteligência" to 2)),
    MAGO(mapOf("Sabedoria" to 2))
}
