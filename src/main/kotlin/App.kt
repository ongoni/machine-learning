import classifier.ReviewClassifier
import manipulator.DataManipulator
import statistics.ClassifierStatistics

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
            val statistics = ClassifierStatistics()

            classifySet.forEach { review ->
                val result = classifier.classify(review.first)
                statistics.add(result.first, review.second)
            }

            println(statistics.getSimpleStatistics())
        }
    }

}