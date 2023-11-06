package com.example.demo.controller

import com.example.demo.dto.CSVEvaluateResponseDTO
import com.example.demo.service.CSVService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/evaluation")
class CSVController(private val csvService: CSVService) {

    @GetMapping
    // I prefer using a Set to avoid duplicating URLs.
    fun evaluate(@RequestParam("urls") urls: Set<String>): CSVEvaluateResponseDTO {
        return csvService.evaluate(urls.toList())
    }
}