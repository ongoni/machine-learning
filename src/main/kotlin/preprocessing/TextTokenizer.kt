package preprocessing

import logger.ParsingLogger

object TextTokenizer {

    private val delimiters = arrayOf(" ", ",", ".", "!", "?", ":", ";", "/", "-", "«", "»", "—", "\r", "\t", "\n", "(", ")")

    fun getTextTokens(text: String): List<String> {
        ParsingLogger.log("Tokenizing...")

        val result = text.split(*delimiters)
                .asSequence()
                .map { it.toLowerCase().dropWhile { x -> x.isDigit() } }
                .toMutableList()

        result.removeIf { x -> x.contains(Regex("good|bad|neutral|\\.\\.\\.|\\.\\.")) || x.isEmpty() }

        return result
                .asSequence()
                .map { TextLemmatizer.lemmatize(it) }
                .toList()
                .filterNotNull()
    }

}