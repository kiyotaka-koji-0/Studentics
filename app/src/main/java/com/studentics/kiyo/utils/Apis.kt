package com.studentics.kiyo.utils

import android.util.Log

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException

import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

import com.studentics.kiyo.viewModels.Exam


suspend fun getAIReview(exam: Exam): String {
    val apikey = "AIzaSyCe4h7RMQqhdu-Gr-vTK14nxIJ1I8TufPI"
    val systemInstruction = "You are an helpful assistant/teacher, your task is to evaluate and provide a review of the exam performed by student, you need to include in which he is good and what he need to focus, also add some suggestion by yourself"


    val generativeModel = GenerativeModel(
        modelName = "gemini-1.0-pro",
        generationConfig = generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
        apiKey = apikey,

    )

    val prompt =
        "$systemInstruction, Exam: ${exam.name}, Class: ${exam.className}, All Subjects Details With Marks: ${exam.subjects}, write a review for this exam about student performance and what he need to improve, in 150 words"

    val response = generativeModel.generateContent(prompt)

    return try {
        Log.d("Generated AI Review", "Review: ${response.text.toString()}")
        response.text.toString()
    } catch (it: GoogleGenerativeAIException) {
        Log.d("Error", it.message.toString())
        ""
    }
}