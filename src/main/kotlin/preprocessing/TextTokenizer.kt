package preprocessing

import logger.ParsingLogger
import java.io.File

object TextTokenizer {

    private val delimiters = arrayOf(" ", ",", ".", "!", "?", ":", ";", "/", "-", "«", "»", "—", "\r", "\t", "\n", "(", ")")
    private val stopWords = File("stopwords.txt").readText().split(*delimiters)

    fun getTextTokens(text: String): Pair<List<String>, String> {
        ParsingLogger.log("Tokenizing...")

        val words = text
                .split(*delimiters)
                .asSequence()
                .map { it.toLowerCase()
                        .dropWhile { x -> x.isDigit() }
                }
                .filter { x -> !stopWords.contains(x) }
                .toMutableList()

        val type = words.first { x -> x.contains(Regex("good|bad|neutral")) }

        return words
                .asSequence()
                .filterNot { it.isEmpty() }
                .map { TextLemmatizer.lemmatize(it) }
                .filterNotNull()
                .toList() to type
    }

}