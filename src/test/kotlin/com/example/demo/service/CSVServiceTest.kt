package com.example.demo.service

import com.example.demo.model.CSVRow
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CSVServiceTest{

    @InjectMocks
    private lateinit var csvService: CSVService

    @Test
    fun `it should not be valid url`(){
        val isValid = csvService.isValidURL("sample-url")

        Assertions.assertThat(isValid).isFalse()
    }

    @Test
    fun `it should be valid url`(){
        val isValid = csvService.isValidURL("https://www.sample.com")

        Assertions.assertThat(isValid).isTrue()
    }

    @Test
    fun it_should_get_speaker_most_occurrences_in_topic() {
        val csvData = listOf(
            CSVRow("John", "TopicA", "2023-01-01", "100"),
            CSVRow("John", "TopicA", "2023-01-01", "100"),
            CSVRow("Jane", "TopicB", "2023-01-02", "200"),
            CSVRow("Bob", "TopicA", "2023-01-03", "150"),
            CSVRow("Bob", "TopicA", "2023-01-03", "150"),
            CSVRow("Bob", "TopicA", "2023-01-03", "150"),
        )

        val topicToFind = "TopicA"
        val result = csvService.findSpeakerWithMostOccurrencesInTopic(csvData, topicToFind)

        val expectedSpeaker = "Bob"
        assertEquals(expectedSpeaker, result)
    }

    @Test
    fun it_should_get_null_when_speaker_most_occurrences_in_topic_more_than_one() {
        val csvData = listOf(
            CSVRow("John", "TopicA", "2023-01-01", "100"),
            CSVRow("John", "TopicA", "2023-01-01", "100"),
            CSVRow("Jane", "TopicB", "2023-01-02", "200"),
            CSVRow("Bob", "TopicA", "2023-01-03", "150"),
            CSVRow("Bob", "TopicA", "2023-01-03", "150"),
        )

        val topicToFind = "TopicA"
        val result = csvService.findSpeakerWithMostOccurrencesInTopic(csvData, topicToFind)

        assertNull(result)
    }

    @Test
    fun it_should_get_speaker_most_occurrences_in_date() {
        val dateToFind = "2013"
        val csvData = listOf(
            CSVRow("John", "TopicA", "2013-01-01", "100"),
            CSVRow("Jane", "TopicA", "2013-01-01", "200"),
            CSVRow("Jane", "TopicA", "2013-02-02", "200"),
            CSVRow("Bob", "TopicA", "2013-01-01", "150"),
            CSVRow("Alice", "TopicB", "2013-01-01", "300")
        )

        val result = csvService.findSpeakerWithMostOccurrencesInDate(csvData, dateToFind)

        assertEquals("Jane", result)
    }

    @Test
    fun it_should_get_null_when_speaker_most_occurrences_in_date_more_than_one() {
        val dateToFind = "2013"
        val csvData = listOf(
            CSVRow("John", "TopicA", "2013-01-01", "100"),
            CSVRow("Jane", "TopicA", "2013-02-02", "200"),
            CSVRow("Bob", "TopicA", "2013-01-01", "150"),
            CSVRow("Alice", "TopicB", "2013-01-01", "300")
        )

        val result = csvService.findSpeakerWithMostOccurrencesInDate(csvData, dateToFind)

        assertNull(result)
    }

    @Test
    fun it_should_get_speaker_with_lowest_total_words() {
        val csvData = listOf(
            CSVRow("John", "TopicA", "2013-01-01", "100"),
            CSVRow("Jane", "TopicA", "2013-01-01", "200"),
            CSVRow("Bob", "TopicB", "2013-01-01", "150"),
            CSVRow("Alice", "TopicB", "2013-01-01", "50"),
            CSVRow("Jesse", "TopicB", "2013-01-01", "30")
        )

        val result = csvService.findSpeakerWithLowestTotalWords(csvData)

        assertEquals("Jesse", result)
    }

    @Test
    fun it_should_get_null_when_speaker_with_lowest_total_words_more_than_one() {
        val csvData = listOf(
            CSVRow("John", "TopicA", "2013-01-01", "100"),
            CSVRow("Jane", "TopicA", "2013-01-01", "200"),
            CSVRow("Bob", "TopicB", "2013-01-01", "150"),
            CSVRow("Alice", "TopicB", "2013-01-01", "50"),
            CSVRow("Jesse", "TopicB", "2013-01-01", "50")
        )

        val result = csvService.findSpeakerWithLowestTotalWords(csvData)

        assertNull(result)
    }
}