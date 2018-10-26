import classifier.ReviewClassifier
import manipulator.DataManipulator

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DataManipulator.parseAndSaveReviews(false)

            val classifier = ReviewClassifier()
            val trainingSet = DataManipulator.getTrainingSet()

            classifier.train(trainingSet)

            val classifySet = DataManipulator.getClassifySet()

            var correct = 0
            var incorrect = 0

            classifySet.forEach {
                val result = classifier.classify(it.first)

                if (result.first == it.second) {
                    correct++
                } else {
                    incorrect++
                }
            }

            println("correct - ${correct / (correct + incorrect)}\nincorrect - ${incorrect / (correct + incorrect)}")
        }
    }

}