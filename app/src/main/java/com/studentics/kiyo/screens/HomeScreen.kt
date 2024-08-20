package com.studentics.kiyo.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.studentics.kiyo.R
import com.studentics.kiyo.classes.AddExamScreen
import com.studentics.kiyo.components.BottomNavBar
import com.studentics.kiyo.components.SubjectMarksPieChart
import com.studentics.kiyo.components.TopBar
import com.studentics.kiyo.components.TypewriterText
import com.studentics.kiyo.ui.theme.accentColor
import com.studentics.kiyo.ui.theme.backgroundColor
import com.studentics.kiyo.ui.theme.componentColor
import com.studentics.kiyo.utils.ExamDropdown
import com.studentics.kiyo.utils.getFont
import com.studentics.kiyo.viewModels.Exam
import com.studentics.kiyo.viewModels.ExamViewModel
import com.studentics.kiyo.viewModels.FirestoreRepository
import com.studentics.kiyo.viewModels.Subject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.compose.runtime.remember as remember


@Composable
fun HomeScreenUI(navController: NavController, examViewModel: ExamViewModel = viewModel()) {
    val examList by examViewModel.exams.observeAsState(initial = emptyList())
    val isRefreshing by examViewModel.isRefreshing.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    var selectedExam by remember { mutableStateOf<Exam?>(null) }


    LaunchedEffect(Unit) {
        examViewModel.fetchExams()
        delay(2000)
        examViewModel.fetchExams()
    }



    Scaffold(
        topBar = ({ TopBar() }),
        bottomBar = ({ BottomNavBar(navController = navController) })
    ) { paddingValues ->
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = {
            coroutineScope.launch {
                examViewModel.fetchExams()
            }
        }) {


            LazyColumn(

                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(color = componentColor)
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.padding(top = 5.dp))
                        Text(
                            text = "Welcome",
                            fontFamily = getFont("Ubuntu"),
                            color = Color.White,
                            fontSize = 25.sp
                        )
                        Text(
                            text = "Kiyotaka",
                            fontFamily = getFont("Poppins"),
                            color = colorResource(
                                id = R.color.google_green
                            ),
                            fontSize = 23.sp
                        )
                    }
                }

                if (examList.isEmpty()) {
                    item {

                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .background(colorResource(id = R.color.metalBG))
                                .border(
                                    color = colorResource(id = R.color.google_green),
                                    width = 2.dp,
                                    shape = RoundedCornerShape(15.dp)
                                )

                                .fillMaxWidth(0.95f)
                                .padding(5.dp)
                                .clickable {
                                    navController.navigate(AddExamScreen)
                                }) {

                            Text(
                                text = "Oh! No Exam Found!!",
                                fontFamily = getFont("Ubuntu"),
                                fontSize = 25.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "Add A Exam To See Analytics And Choose From The Dropdown",
                                fontFamily = getFont("Poppins"),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                        }
                    }
                } else {

                    item {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)

                        ) {


                            ExamDropdown(list = examList, onExamSelected = { exam ->
                                selectedExam = exam
                            })


                        }
                    }
                }
                item {

                    selectedExam?.let { exam ->
                        StudentPortfolio(selectedExam = exam)
                        ExamDetails(exam = exam)

                    }
                }
                item {
                    selectedExam?.let { exam ->
                        AIReviewBlob(exam = exam)
                    }
                }
            }
        }
    }
}


@Composable
fun ExamDetails(modifier: Modifier = Modifier, exam: Exam) {
    val lazyListState = rememberLazyListState()
    val lazyListState2 = rememberLazyListState()
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        state = lazyListState,
        userScrollEnabled = true,
        modifier = modifier
            .padding(top = 10.dp)
            .background(color = componentColor, shape = RoundedCornerShape(15.dp))
            .fillMaxWidth()

    ) {
        items(exam.subjects.size) { index ->
            SubjectCapsule(subject = exam.subjects[index])
        }
    }
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        state = lazyListState2,
        modifier = modifier
            .padding(top = 10.dp)
            .background(color = componentColor, shape = RoundedCornerShape(15.dp))

            .fillMaxWidth()

    ) {
        item {
            SubjectMarksPieChart(subjects = exam.subjects)
        }
    }

}


@Composable
fun SubjectCapsule(modifier: Modifier = Modifier, subject: Subject) {
    val percentage = (subject.marks.toFloat() / subject.outOf.toFloat()) * 100
    Card(
        modifier = modifier
            .padding(10.dp)
            .width(160.dp)
            .height(100.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.purple_200)
        ),
        border = BorderStroke(width = 2.dp, color = colorResource(id = R.color.google_green)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 8.dp,
            pressedElevation = 12.dp,
            focusedElevation = 12.dp
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = subject.subjectName,
                fontFamily = getFont("Ubuntu"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${subject.marks}/${subject.outOf}",
                    fontFamily = getFont("Poppins"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(Color(0xFF4CAF50), shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(
                    text = "$percentage%",
                    fontFamily = getFont("Poppins"),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            if (percentage >= 70) Color(0xFF4CAF50) else Color(0xFFF44336),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
fun StudentPortfolio(modifier: Modifier = Modifier, selectedExam: Exam) {
    val viewModel: ExamViewModel = ExamViewModel(FirestoreRepository())
    val examList by viewModel.exams.observeAsState(initial = emptyList())
    val prevExam =
        viewModel.getPreviousExamByDate(LocalDateTime.parse(selectedExam.datetime)).observeAsState()
    val currentPercentage = (selectedExam.subjects.sumOf { it.marks }
        .toFloat() / selectedExam.subjects.sumOf { it.outOf }.toFloat() * 100).toInt()
    val previousPercentage = (prevExam.value?.subjects?.sumOf { it.marks }?.toFloat()
        ?: 0f) / (prevExam.value?.subjects?.sumOf { it.outOf }?.toFloat() ?: 1f)
    val diffrencepercent = currentPercentage - (previousPercentage * 100).toInt()

    Card(
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp,
            hoveredElevation = 20.dp,
            pressedElevation = 15.dp,
            focusedElevation = 15.dp,
            disabledElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(containerColor = componentColor),
        border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.google_green)),
        modifier = Modifier
            .padding(top = 10.dp)
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(5.dp))


    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 15.dp, shape = RoundedCornerShape(5.dp))

        ) {
            Text(
                text = "Kiyotaka Portfolio",
                color = Color.White,
                fontFamily = getFont("Poppins"),
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
            Row {

                Text(
                    text = "Current Percentage: $currentPercentage%",
                    color = Color.White,
                    fontFamily = getFont("Poppins")
                )
                Icon(
                    imageVector = if (diffrencepercent > 0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    tint = if (diffrencepercent > 0) Color.Green else Color.Red
                )
            }

            Text(
                text = "Projectile Marks Scored: ${selectedExam.subjects.sumOf { it.marks }}/${selectedExam.subjects.sumOf { it.outOf }}",
                color = Color.White,
                fontFamily = getFont("Poppins")
            )
            val bestSubject = selectedExam.subjects.maxByOrNull { it.marks }

            Text(text = "Best Subject This Exam: ${bestSubject?.subjectName}", color = Color.White)

        }

    }
}

@Composable
fun AIReviewBlob(modifier: Modifier = Modifier, exam: Exam) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 10.dp)
            .background(shape = RoundedCornerShape(5.dp), color = componentColor)
            .fillMaxWidth()
            .clickable { expanded = !expanded }


    )

    {
        Box(contentAlignment = Alignment.Center) {
            val review = exam.aiReview.replace("*", "").replace("", "")
            if (expanded) {
                TypewriterText(text = review)
            } else {
                Text(
                    text = review,
                    fontFamily = getFont(name = "Poppins"),
                    color = Color.White,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



