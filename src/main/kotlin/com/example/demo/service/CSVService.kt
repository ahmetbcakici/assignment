package com.example.demo.service

import com.example.demo.dto.CSVEvaluateResponseDTO
import com.example.demo.model.CSVRow
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI

@Service
class CSVService {

    fun evaluate(urls: List<String>): CSVEvaluateResponseDTO {

        // url validation
        if (urls.any { !isValidURL(it) }) {
            throw IllegalArgumentException("All URLs must be valid.")
        }

        val csvData = combineCSVDataFromURLs(urls)

        val leastWordy = findSpeakerWithLowestTotalWords(csvData)
        val mostSpeeches = findSpeakerWithMostOccurrencesInDate(csvData, DATE_TO_FIND)
        val mostSecurity = findSpeakerWithMostOccurrencesInTopic(csvData, TOPIC_TO_FIND)

        return CSVEvaluateResponseDTO(mostSpeeches, mostSecurity, leastWordy)
    }

    fun isValidURL(url: String) = runCatching {
        URI.create(url).toURL()
    }.isSuccess

    fun combineCSVDataFromURLs(urls: List<String>): List<CSVRow> {
        // I read all the CSV files I have and return the rows in a single list to process
        return urls.flatMap { url -> readCSVFromURL(url) }
    }

    fun findSpeakerWithLowestTotalWords(list: List<CSVRow>): String? {
        return list.groupBy { it.speaker }
            .mapValues { (_, rows) ->
                rows.sumOf {
                    it.words.toIntOrNull()
                        ?: 0  // I need to cast "words" to Int from Strıng because of the CSV. Normally I would use it as an Int
                }
            }
            .minByOrNull { it.value }
            ?.key
    }

    fun findSpeakerWithMostOccurrencesInDate(list: List<CSVRow>, dateToFind: String): String? {
        val nameCounts = list
            .filter { it.date.startsWith(dateToFind) }
            .groupingBy { it.speaker }
            .eachCount()

        val mostFrequentName = nameCounts.maxByOrNull { it.value }
        return mostFrequentName?.key
    }

    fun findSpeakerWithMostOccurrencesInTopic(list: List<CSVRow>, topicToFind: String): String? {
        val filteredData = list.filter { it.topic == topicToFind }
        if (filteredData.isEmpty()) {
            return null
        }

        val nameCounts = filteredData.groupingBy { it.speaker }
            .eachCount()

        val mostFrequentName = nameCounts.maxByOrNull { it.value }
        return mostFrequentName?.key
    }

    fun readCSVFromURL(url: String): List<CSVRow> {
        return try {
            // This process might be slow, but we can speed it up by using caching and multi-threading
            val csvParser = CSVParser(
                BufferedReader(InputStreamReader(URI.create(url).toURL().openStream())),
                CSVFormat.DEFAULT.withHeader()
            )

            csvParser.map { csvRecord ->
                CSVRow(
                    speaker = csvRecord.get("Speaker"),
                    topic = csvRecord.get("Topic"),
                    date = csvRecord.get("Date"),
                    words = csvRecord.get("Words")
                )
            }
        } catch (e: Exception) {

            /* There could be different exceptions here. If CSV format is not expected we'll got IllegalArgumentException
               or if there is no CSV file we'll got FileNotFoundException.
               I kept it simple here, and even if an error occurs, it does not prevent progress for other valid CSVs.
               But we can change the behaviour as we want. We can also throw an exception stop the process when there is any problme */

            emptyList()
        }
    }

    companion object {
        private const val TOPIC_TO_FIND = "homeland security"
        private const val DATE_TO_FIND = "2013"
    }
}