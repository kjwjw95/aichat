package com.example

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {

    private val client = OkHttpClient()
    @Value("\${openai.api_key}")
    private lateinit var apiKey: String

    @GetMapping("/sendMessage")
    fun sendMessageToChatGpt(@RequestParam msg: String): String {
        val requestBody = """{"model": "text-davinci-003", "prompt": "$msg", "max_tokens": 150}""".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/text-davinci-003/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: "Error: No response from ChatGPT API"
        }
    }
}
