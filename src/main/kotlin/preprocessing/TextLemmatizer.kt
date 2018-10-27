package preprocessing

import org.apache.lucene.morphology.russian.RussianLuceneMorphology
import java.lang.Exception

object TextLemmatizer {

    private val morphology = RussianLuceneMorphology()

    fun lemmatize(word: String): String? {
        val wordInfo: MutableList<String>

        try {
            wordInfo = morphology.getMorphInfo(word).toMutableList()
        } catch (ex: Exception) {
            return null
        }

        wordInfo.removeIf { x -> x.contains(Regex("ПРЕД|СОЮЗ|МЕЖД|МС|ЧАСТ")) }

        return if (wordInfo.size >= 1) {
            escapeWord(wordInfo.first())
        } else {
            null
        }
    }

    private fun escapeWord(word: String): String? {
        val match = Regex("([а-яё]+)\\|").find(word)

        if (match != null) {
            return match.groups[1]?.value
        }

        return null
    }

}