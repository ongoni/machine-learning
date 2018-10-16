package parsing

import org.jsoup.Connection
import org.jsoup.Jsoup
object FilmTopParser {

    private const val KINOPOISK_URL = "https://www.kinopoisk.ru/"
    private const val FILM_TOP_URL = "https://www.kinopoisk.ru/top/"
    private const val DEFAULT_FILM_COUNT = 250
    private const val DEFAULT_REVIEW_COUNT = 50
    private var cookies = Jsoup.connect(KINOPOISK_URL).method(Connection.Method.GET).execute().cookies()

    private fun getFilmReviewsURL(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT)
            = "https://www.kinopoisk.ru/film/$filmIdentifier/ord/rating/perpage/$reviewCount/#list"

    fun getTopFilmsIdentifiers(count: Int = DEFAULT_FILM_COUNT): List<String> {
        val doc = Jsoup.connect(FILM_TOP_URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .cookies(cookies)
                .get()
        val regex = Regex("/film/(.+)/(.+)/")

        return (1 until count)
                .map { doc.select("tr#top250_place_$it a.all[href]").toString() }
                .map { regex.find(it)!!.groups[1]!!.value }
                .toList()
    }

    fun getFilmReviews(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT): List<String> {
        val doc = Jsoup.connect(getFilmReviewsURL(filmIdentifier, reviewCount))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .cookies(cookies)
                .get()

        return doc.select(".clear_all .reviewItem .response .brand_words ._reachbanner_")
                .map {
                    it.ownText()
                            .replace("<br>", "")
                            .replace("<b>", "")
                            .replace("<i>", "")
                }
    }

}