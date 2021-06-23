package fr.juniorreox.disquette.modele

import fr.juniorreox.disquette.repository.disqueRepository

class Id {

    companion object {
        var num = disqueRepository.singleton.disqueList.size
        fun getId():Int {
            num = num + 1
            return num }

    }
}