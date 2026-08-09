package com.artem.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.artem.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                StudentAssistantApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentAssistantApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Студентський Асистент") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Розклад") },
                    label = { Text("Розклад") },
                    selected = currentRoute == "schedule",
                    onClick = {
                        navController.navigate("schedule") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Нотатки") },
                    label = { Text("Нотатки") },
                    selected = currentRoute == "notes",
                    onClick = {
                        navController.navigate("notes") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Звіти") },
                    label = { Text("Звіти") },
                    selected = currentRoute == "reports",
                    onClick = {
                        navController.navigate("reports") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "reports",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("schedule") { ScheduleScreen() }
            composable("notes") { NotesScreen() }
            composable("reports") { ReportsScreen() }
        }
    }
}


@Composable
fun ScheduleScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Тут буде розклад пар")
    }
}

@Composable
fun NotesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Тут будуть нотатки (Room Database)")
    }
}

@Composable
fun ReportsScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var studentName by remember { mutableStateOf("") }
        var group by remember { mutableStateOf("") }
        var practiceBase by remember { mutableStateOf("") }

        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text("ПІБ студента") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = group,
            onValueChange = { group = it },
            label = { Text("Шифр групи (напр. ІПЗ-21)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = practiceBase,
            onValueChange = { practiceBase = it },
            label = { Text("База практики (Підприємство/Кафедра)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { generateWordReport(context, studentName, group, practiceBase) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Word (.docx)")
            }

            Button(
                onClick = { generatePdfReport(context, studentName, group, practiceBase) },
                modifier = Modifier.weight(1f)
            ) {
                Text("PDF")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MyApplicationTheme {
        StudentAssistantApp()
    }
}