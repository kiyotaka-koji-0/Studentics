package com.studentics.kiyo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.studentics.kiyo.ui.theme.componentColor

import com.studentics.kiyo.viewModels.Subject

import kotlin.random.Random


@Composable
fun SubjectMarksPieChart(subjects: List<Subject>, height:Int = 300, width:Int = 300) {
    val totalMarks = subjects.sumOf { it.marks }.toFloat()
    val pieChartData = subjects.map { subject ->
        PieChartData.Slice(
            label = subject.subjectName,
            value = ((subject.marks / totalMarks) * 100),
            color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        )
    }

    val pieChartConfig = PieChartConfig(
        isSumVisible = true,
        labelType = PieChartConfig.LabelType.PERCENTAGE,
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
        sliceLabelTextSize = 15.sp,
        backgroundColor = componentColor,
        sumUnit = "%"

    )

    PieChart(
        modifier = Modifier
            .width(width = width.dp)
            .height(height=height.dp)

            .background(Color.Transparent, shape = RoundedCornerShape(10.dp))
            .shadow(8.dp),
        pieChartData = PieChartData(pieChartData, plotType = PlotType.Pie),
        pieChartConfig = pieChartConfig
    )
}

