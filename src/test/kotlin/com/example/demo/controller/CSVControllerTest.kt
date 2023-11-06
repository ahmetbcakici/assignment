package com.example.demo.controller

import com.example.demo.dto.CSVEvaluateResponseDTO
import com.example.demo.service.CSVService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class CSVControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMocks
    private lateinit var csvController: CSVController

    @Mock
    private lateinit var csvService: CSVService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(csvController).build()
    }

    @Test
    fun testEvaluate() {
        val mockResponse = CSVEvaluateResponseDTO("mostSpeeches", "mostSecurity", "leastWordy")
        Mockito.`when`(csvService.evaluate(Mockito.anyList())).thenReturn(mockResponse)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/evaluation")
                .param("urls", "url1", "url2", "url3")
        )

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    """
                        {
                            "mostSpeeches": "mostSpeeches",
                            "mostSecurity": "mostSecurity",
                            "leastWordy": "leastWordy"
                        }
                        """.trimIndent()
                )
            )
    }
}