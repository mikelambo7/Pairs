package com.mlambo.pairs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlambo.pairs.ui.theme.PairsTheme
import java.io.InputStream

// Cite https://developer.android.com/develop/ui/compose/tutorial

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PairsTheme {
                Column(
                    modifier = Modifier
                        .padding(top = 36.dp, bottom = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Pairs()
                }
            }
        }
    }
}

// Cite https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DropdownMenuItem(kotlin.Function0,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Boolean,androidx.compose.material3.MenuItemColors,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource)
@Composable
fun SelectFileNames(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = "Select an input file",
        modifier = Modifier
            .fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)
        .clickable { expanded = true }
        .padding(top = 10.dp, bottom = 20.dp)
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
    var x by remember { mutableStateOf(TextFieldValue("")) }
    var y by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    var generateData by remember { mutableStateOf(false) }
    var generatePairs by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp)) {
        SelectFileNames(selectedOption = selectedFile,
            onOptionSelected = {
                setSelectedFile(it)
                generateData = false
                generatePairs = false
                x = TextFieldValue("")
                y = TextFieldValue("")
            }
        )

        if (selectedFile !== "Please select a file..") {
            val people = generatedPeople(context, selectedFile)

            Button(onClick = {
                generateData = true
            }) {
                Text("Load")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (generateData) {
                Column {
                    Text(
                        "People",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontWeight = FontWeight.Bold,
                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Text("Email",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "First Name",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Last Name",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Skill Level",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    people.forEach { person ->
                        HorizontalDivider()
                        Row {
                            Text(person.email, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            Text(person.firstName, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            Text(person.lastName, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            Text(
                                person.skillLevel.toString(),
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Time to make pairs!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "X represents the number of people in a team",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Y represents the total number of people to choose from",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = x,
                        onValueChange = { x = it },
                        label = { Text("Enter a value for X:") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = y,
                        onValueChange = { y = it },
                        label = { Text("Enter a value for Y:") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        generatePairs = true
                    }) {
                        Text("Submit")
                    }

                    if (generatePairs) {
                        GeneratedPairs(people, x, y)
                    }
                }
            }
        }
    }
}

// Cite https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-int-or-null.html
@Composable
fun GeneratedPairs(people: List<Person>, x: TextFieldValue, y: TextFieldValue) {
    val numOfSimilar = x.text.toIntOrNull() ?: 0
    val numOfTotal = y.text.toIntOrNull() ?: people.size

    // Provided lambda to determine if two people are similar
    val similarPairs = similarPairs(people, numOfSimilar, numOfTotal) { p1, p2 ->
        p1.skillLevel == p2.skillLevel
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        "Generated Pairs",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Spacer(modifier = Modifier.height(8.dp))

    similarPairs.forEachIndexed { index, pair ->
        Text("Team ${index + 1}", fontWeight = FontWeight.Bold)
        pair.forEach { person ->
            Text("${person.firstName} ${person.lastName} (${person.skillLevel})")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Cite https://kotlinlang.org/docs/collection-parts.html#take-and-drop
fun similarPairs(
    people: List<Person>,
    x: Int,
    y: Int,
    areSimilar: (Person, Person) -> Boolean
): List<List<Person>> {
    val totalPeopleSelected = people.take(y)
    val pairs = mutableListOf<List<Person>>()
    val selected = mutableSetOf<Person>()

    for (person in totalPeopleSelected) {
        if (person !in selected) {
            val pair =
                totalPeopleSelected.filter { p -> areSimilar(person, p) && p !in selected }.take(x)
            if (pair.size >= x) {
                selected.addAll(pair)
                pairs.add(pair)
            }
        }
    }

    return pairs
}

// Cite https://hyperskill.org/learn/step/6351#readlines
// Cite https://www.geeksforgeeks.org/read-from-files-using-inputreader-in-kotlin/
fun generatedPeople(context: Context, fileName: String): List<Person> {
    val people = mutableListOf<Person>()

    try {
        val inputStream: InputStream = context.assets.open(fileName)
        inputStream.reader().useLines { lines ->
            lines.forEach {
                val person = it.split(",")

                // Line is invalid if person has less than four attributes
                if (person.size >= 4) {
                    val email = person[0]
                    val firstName = person[1].lowercase().replaceFirstChar { it.uppercaseChar() }
                    val lastName = person[2].lowercase().replaceFirstChar { it.uppercaseChar() }
                    val skillLevel = person[3]

                    if (validLine(email, skillLevel)) {
                        people.add(
                            Person(
                                email,
                                firstName,
                                lastName,
                                SkillLevel.valueOf(skillLevel.uppercase())
                            )
                        )
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("PairsApp", "Error reading file: ${e.message}")
    }

    return people
}

fun validLine(email: String, skillLevel: String): Boolean {
//    Check if Email and SkillLevel are valid
    return !(!Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
            !SkillLevel.isValidSkillLevel(skillLevel))
}


@Preview(showBackground = true)
@Composable
fun PairsPreview() {
    PairsTheme {
        Pairs()
    }
}