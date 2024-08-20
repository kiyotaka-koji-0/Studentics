package com.studentics.kiyo.viewModels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.studentics.kiyo.utils.getAIReview
import datetimeGson
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime





data class Subject(
    val subjectName: String = "",
    val marks: Int = 0,
    val outOf: Int = 0
)


data class Exam(
    val name: String = "",
    val className: String = "",
    val datetime: String = LocalDateTime.now().toString(),
    var aiReview: String = "",
    val subjects: List<Subject> = mutableListOf()
)




class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val examsCollection = db.collection("exams")

    suspend fun addExam(exam: Exam) {

        examsCollection.document(exam.name).set(exam).await()
    }

    suspend fun getExams(): List<Exam> {
        return try {
            val result = examsCollection.get().await().documents.mapNotNull {
                it.toObject(Exam::class.java)
            }
            Log.d("Result", "Fetched Data Successfully $result")
            result
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            emptyList()
        }
    }

    suspend fun getPreviousExamByDate(selectedExamDate: LocalDateTime): Exam? {
        return try {
            val exams = getExams()
            exams.filter{ LocalDateTime.parse(it.datetime) < selectedExamDate }
                .maxByOrNull { LocalDateTime.parse(it.datetime) }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            null
        }
    }

    suspend fun updateExam(exam: Exam) {
        examsCollection.document(exam.name).set(exam).await()
    }
}

class ExamViewModel(private val repository: FirestoreRepository) : ViewModel() {
    private val _exams = MutableLiveData<List<Exam>>()
    val exams: LiveData<List<Exam>> get() = _exams

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    init {
        fetchExams()
    }

    fun fetchExams() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val examList = repository.getExams()
            _exams.value = examList
            _isRefreshing.value = false
        }
    }

    fun addExam(exam: Exam) {
        viewModelScope.launch {
            exam.aiReview = getAIReview(exam)
            repository.addExam(exam)
            fetchExams()
        }
    }

    fun getPreviousExamByDate(selectedExamDate: LocalDateTime): LiveData<Exam?> {
        val previousExam = MutableLiveData<Exam?>()
        viewModelScope.launch {
            previousExam.value = repository.getPreviousExamByDate(selectedExamDate)
        }
        return previousExam
    }

    fun updateExam(exam: Exam) {
        viewModelScope.launch {
            repository.updateExam(exam)
            fetchExams() // Refresh the list of exams after updating
        }
    }
}
