package logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ParsingLogger {

    fun log(message: String) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")

        println("[${now.format(formatter)}]: $message")
    }

}