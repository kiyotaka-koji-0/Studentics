package com.studentics.kiyo.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studentics.kiyo.R
import com.studentics.kiyo.classes.HomeScreen
import com.studentics.kiyo.components.BottomNavBar
import com.studentics.kiyo.components.CustomSnackbar
import com.studentics.kiyo.components.DatePickerModal
import com.studentics.kiyo.components.TopBar
import com.studentics.kiyo.utils.getAIReview
import com.studentics.kiyo.utils.getFont
import com.studentics.kiyo.viewModels.Exam
import com.studentics.kiyo.viewModels.ExamViewModel
import com.studentics.kiyo.viewModels.FirestoreRepository
import com.studentics.kiyo.viewModels.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.coroutineContext


@Composable
fun AddExamScreenUI(
    viewmodel: ExamViewModel = ExamViewModel(FirestoreRepository()), navController: NavController
) {
    Scaffold(topBar = { TopBar() },
        bottomBar = { BottomNavBar(navController = navController) }) { padding ->


        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = padding,
            modifier = Modifier
                .background(color = Color.Black)
                .fillMaxSize()
        ) {

            item {
                AddExamModal(viewmodel = viewmodel, navController = navController)
            }

        }
    }
}

@Composable
fun AddExamModal(
    modifier: Modifier = Modifier,
    viewmodel: ExamViewModel,
    navController: NavController
) {
    var examName by remember { mutableStateOf("") }
    val subjectsForClass9 = listOf("English", "Maths", "SST", "Hindi", "AI", "Science")
    var showSnackbar by remember { mutableStateOf(false) }
    var classname by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    val aiReview = remember { mutableStateOf("") }
    val context = LocalContext.current
    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = Color.White,
        focusedTextColor = Color.White,
        focusedBorderColor = colorResource(id = R.color.google_green),
        focusedLabelColor = Color.White
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 20.dp)
            .background(colorResource(id = R.color.metalBG), shape = RoundedCornerShape(6.dp))
            .fillMaxSize(0.90f)

    ) {
        Text(
            text = "Add Exam", fontFamily = getFont("Oswald"), color = colorResource(
                id = R.color.google_green
            ), fontSize = 25.sp
        )
        OutlinedTextField(
            label = { Text(text = "Exam Name") },
            value = examName,
            onValueChange = { examName = it },
            colors = fieldColors,
            singleLine = true
        )
        if (showSnackbar) {
            CustomSnackbar(message = "Please Enter Exam Name First!!",
                actionLabel = "Dismiss",
                onActionClick = { showSnackbar = false })
        }
        ClassDropdownWithSubjects(classList = listOf(
            "Class 9th",
            "Class 10th",
            "Class 11th",
            "Class 12th"
        ),
            onClassSelected = { classname = it },
            onSubmit = { subjects ->
                if (examName.isBlank()) {
                    coroutine.launch {
                        showSnackbar = true
                        delay(2000)
                        showSnackbar = false
                    }


                } else {

                    viewmodel.addExam(
                        Exam(name = examName, subjects = subjects, className = classname)
                    )
                    Toast.makeText(context, "Exam Added Successfully!!", Toast.LENGTH_SHORT).show()
                    navController.navigate(HomeScreen)

                }
            })


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDropdownWithSubjects(
    classList: List<String>, onClassSelected: (String) -> Unit, onSubmit: (List<Subject>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var subjects by remember { mutableStateOf(mutableListOf<Subject>()) }
    val subjectsForClass9 = listOf("English", "Maths", "SST", "Hindi", "AI", "Science")
    val fieldColors = TextFieldDefaults.colors(
        focusedLabelColor = Color.White,
        unfocusedContainerColor = Color.Black,
        focusedTextColor = colorResource(id = R.color.google_green),
        focusedContainerColor = Color.Black,
        unfocusedLabelColor = colorResource(id = R.color.google_green),
        unfocusedTextColor = colorResource(id = R.color.google_green),

        )

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            value = selectedClass,
            onValueChange = { },
            label = { Text("Select Class") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            colors = fieldColors,
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(0.80f)
                .menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            classList.forEach { className ->
                DropdownMenuItem(onClick = {
                    selectedClass = className
                    onClassSelected(className)
                    expanded = false

                    // Show subjects only if Class 9th is selected
                    if (className == "Class 9th") {
                        subjects = subjectsForClass9.map { Subject(it, 0, 0) }.toMutableList()

                    } else {
                        subjects.clear()
                    }
                }, text = { Text(text = className) })
            }
        }

    }
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                dateMillis?.let {
                    selectedDate =
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    if (selectedClass == "Class 9th") {
        Row( horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { showDatePicker = true},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.google_green),
                    contentColor = Color.Black,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(pressedElevation = 18.dp),
                modifier = Modifier.padding(end = 5.dp, start = 5.dp)
            ) {
                Text(text = "Select Exam Date", fontFamily = getFont("Poppins"))
            }

            OutlinedTextField(label = { Text(text = "Exam Date")}, value = if (selectedDate == null)"" else selectedDate.toString(), onValueChange = {}, readOnly = true, colors = fieldColors, modifier = Modifier
                .height(70.dp)
                .padding(top = 5.dp))
        }
        DynamicSubjectFields(subjects = subjects,
            onSubjectsChange = { newSubjects -> subjects = newSubjects.toMutableList() },
            onSubmit = { onSubmit(subjects) })



    }
}


@Composable
fun DynamicSubjectFields(
    subjects: List<Subject>, onSubjectsChange: (List<Subject>) -> Unit, onSubmit: () -> Unit
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = Color.White,
        focusedTextColor = Color.White,
        focusedBorderColor = colorResource(id = R.color.google_green),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedContainerColor = Color.Black,
        unfocusedContainerColor = Color.Black
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        subjects.forEachIndexed { index, subject ->

            Spacer(
                modifier = Modifier
                    .border(
                        color = colorResource(id = R.color.google_green), width = 1.dp
                    )
                    .background(color = colorResource(id = R.color.google_green))
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedTextField(
                    value = subject.subjectName,
                    onValueChange = { name ->
                        onSubjectsChange(subjects.toMutableList().apply {
                            this[index] = subject.copy(subjectName = name)
                        })
                    },
                    label = { Text("Subject Name") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    colors = fieldColors,
                    readOnly = true
                )

                OutlinedTextField(
                    value = subject.marks.toString(),
                    onValueChange = { marks ->
                        onSubjectsChange(subjects.toMutableList().apply {
                            this[index] = subject.copy(marks = marks.toIntOrNull() ?: 0)
                        })
                    },
                    label = { Text("Marks") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    colors = fieldColors
                )

                OutlinedTextField(
                    value = subject.outOf.toString(),
                    onValueChange = { outOf ->
                        onSubjectsChange(subjects.toMutableList().apply {
                            this[index] = subject.copy(outOf = outOf.toIntOrNull() ?: 0)
                        })
                    },
                    label = { Text("Out Of") },
                    modifier = Modifier.weight(1f),
                    colors = fieldColors
                )
            }
        }

        Button(
            onClick = onSubmit, colors = ButtonColors(
                containerColor = colorResource(id = R.color.google_green),
                contentColor = Color.Black,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.Transparent
            ), shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(Icons.Filled.Done, contentDescription = "")
            Text("Submit", fontFamily = getFont("Ubuntu"), fontWeight = FontWeight.Bold)
        }
    }
}

