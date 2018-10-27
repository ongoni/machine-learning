package statistics

import utils.format

class ClassifierStatistics {

    private val contingencyTable = mapOf(
            "good" to Array(2) { arrayOf(0, 0) },
            "bad" to Array(2) { arrayOf(0, 0) },
            "neutral" to Array(2) { arrayOf(0, 0) }
    )
    private var correct = 0
    private var incorrect = 0

    fun add(classifierResult: String, expertResult: String) {
        if (expertResult == "good") {
            if (classifierResult == "good") {
                contingencyTable["good"]!![0][0]++
            } else {
                contingencyTable["good"]!![0][1]++
            }
        } else {
            if (classifierResult == "good") {
                contingencyTable["good"]!![1][0]++
            } else {
                contingencyTable["good"]!![1][1]++
            }
        }

        if (expertResult == "bad") {
            if (classifierResult == "bad") {
                contingencyTable["bad"]!![0][0]++
            } else {
                contingencyTable["bad"]!![0][1]++
            }
        } else {
            if (classifierResult == "bad") {
                contingencyTable["bad"]!![1][0]++
            } else {
                contingencyTable["bad"]!![1][1]++
            }
        }

        if (expertResult == "neutral") {
            if (classifierResult == "neutral") {
                contingencyTable["neutral"]!![0][0]++
            } else {
                contingencyTable["neutral"]!![0][1]++
            }
        } else {
            if (classifierResult == "neutral") {
                contingencyTable["neutral"]!![1][0]++
            } else {
                contingencyTable["neutral"]!![1][1]++
            }
        }

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
                "total: $correct\n" +
                "incorrect - ${incorrectPercent.format(3)}%\n" +
                "total: $incorrect"
    }

    fun getPrecisionRecall(): List<String> {
        val result = mutableListOf<String>()

        contingencyTable.keys.forEach {
            val precision = contingencyTable[it]!![0][0].toDouble() /
                    (contingencyTable[it]!![0][0] + contingencyTable[it]!![1][0])
            val recall = contingencyTable[it]!![0][0].toDouble() /
                    (contingencyTable[it]!![0][0] + contingencyTable[it]!![0][1])
            result.add(
                    "$it:\nprecision - $precision\nrecall - $recall"
            )
        }

        return result
    }

}