package statistics

import utils.format

class ClassifierStatistics {

    private var correct = 0
    private var incorrect = 0

    fun add(classifierResult: String, expertResult: String) {
        if (classifierResult == expertResult) {
            correct++
        } else {
            incorrect++
        }
    }

    fun getSimpleStatistics(): String {
        val correctPercent = 100 * correct.toDouble() / (correct + incorrect)
        val incorrectPercent = 100 * incorrect.toDouble() / (correct + incorrect)

        return "correct - ${correctPercent.format(2)}%\n" +
                "total: $correct" +
                "incorrect - ${incorrectPercent.format(3)}%\n" +
                "total: $incorrect"
    }

}