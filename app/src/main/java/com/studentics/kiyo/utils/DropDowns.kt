package com.studentics.kiyo.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.studentics.kiyo.viewModels.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDropdown(list: List<Exam>, onExamSelected: (Exam) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedExam by remember { mutableStateOf(if (list.isNotEmpty()) list[0].name else "") }

    // Call the onExamSelected callback with the initially selected exam
    if (list.isNotEmpty()) {
        onExamSelected(list[0])
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxSize(0.5f)
    ) {
        TextField(
            value = selectedExam,
            onValueChange = { },
            label = { Text("Select Exam") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
            },
            readOnly = true,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (list.isEmpty()) {
                AlertDialog(onDismissRequest = { }, confirmButton = { },
                    title = { Text(text = "No Exam Found!!", fontFamily = getFont("Ubuntu")) })
            } else {
                list.forEach { exam ->
                    DropdownMenuItem(
                        onClick = {
                            selectedExam = exam.name
                            expanded = false
                            onExamSelected(exam)
                        },
                        text = {
                            Text(text = exam.name)
                        }
                    )
                }
            }
        }
    }
}
