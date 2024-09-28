package com.mlambo.pairs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlambo.pairs.ui.theme.PairsTheme

// Cite https://developer.android.com/develop/ui/compose/tutorial

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PairsTheme {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Pairs()
                }
            }
        }
    }
}

// Cite https://stackoverflow.com/questions/16120697/kotlin-how-to-pass-a-function-as-parameter-to-another
// Cite https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DropdownMenuItem(kotlin.Function0,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Boolean,androidx.compose.material3.MenuItemColors,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource)
@Composable
fun SelectFileNames(selectedOption: String,
                    onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = "Select an input file",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)
        .clickable { expanded = true }
        .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 20.dp)
    ) {
        Text(
            text = selectedOption,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = { Text("People.csv") },
                onClick = {
                    onOptionSelected("People.csv")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("People2.csv") },
                onClick = {
                    onOptionSelected("People2.csv")
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun Pairs() {
    val (selectedFile, setSelectedFile) = remember { mutableStateOf("Please select a file..") }

    Column {
        SelectFileNames(selectedOption = selectedFile,
            onOptionSelected = { setSelectedFile(it) })
    }
}

@Preview(showBackground = true)
@Composable
fun PairsPreview() {
    PairsTheme {
        Pairs()
    }
}