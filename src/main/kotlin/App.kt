import classifier.ReviewClassifier
import manipulator.DataManipulator
import utils.format

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            DataManipulator.parseAndSaveReviews(false)
            val filmCount = 240

            val classifier = ReviewClassifier()
            val trainingSet = DataManipulator.getTrainingSet(filmCount)

            classifier.train(trainingSet)

            val classifySet = DataManipulator.getClassifySet(filmCount)

            var correct = 0
            var incorrect = 0

            classifySet.forEach { review ->
                val result = classifier.classify(review.first)

                if (result.first == review.second) {
                    correct++
                } else {
                    incorrect++
                }
            }

            println("correct - ${(100 * correct.toDouble() / (correct + incorrect)).format(2)}%\n" +
                    "total: $correct")
            println("incorrect - ${(100 * incorrect.toDouble() / (correct + incorrect)).format(3)}%\n" +
                    "total: $incorrect")
        }
    }

}