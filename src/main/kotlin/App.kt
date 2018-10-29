import classifier.ReviewClassifier
import manipulator.DataManipulator
import org.apache.lucene.morphology.russian.RussianLuceneMorphology
import statistics.ClassifierStatistics

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val reviews = DataManipulator.parseReviews(false)
            DataManipulator.saveReviews(reviews)

            val filmCount = 200

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
            println()
            statistics.getPrecisionRecall().forEach {
                println(it)
            }
        }
    }

}