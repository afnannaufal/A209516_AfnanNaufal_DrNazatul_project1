package com.example.a209516_afnannaufal_drnazatul_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.compose.AppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(dynamicColor = false) {
                val navController = rememberNavController()
                val vm: UserViewModel = viewModel()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    floatingActionButton = {
                        if (currentRoute == "home") {
                            FloatingActionButton(
                                onClick = {
                                    vm.clearDrafts()
                                    navController.navigate("add_form")
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Course")
                            }
                        }
                    },
                    bottomBar = {
                        if (currentRoute != "login" && currentRoute != null) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)) {
                                NavigationBarItem(selected = currentRoute == "home", onClick = { navController.navigate("home") }, icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("Hub") })
                                NavigationBarItem(selected = currentRoute == "focus", onClick = { navController.navigate("focus") }, icon = { Icon(Icons.Default.Timer, null) }, label = { Text("Focus") })
                                NavigationBarItem(selected = currentRoute == "profile", onClick = { navController.navigate("profile") }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") })
                            }
                        }
                    }
                ) { padding ->
                    NavHost(navController, "login", Modifier.padding(padding)) {
                        composable("login") { LoginScreen(navController, vm) }
                        composable("home") { HomeScreen(navController, vm) }
                        composable("focus") { FocusScreen(vm) }
                        composable("profile") { ProfileScreen(navController, vm) }
                        composable("add_form") { FormScreen(navController, vm) }
                    }
                }
            }
        }
    }
}

// ── SCREEN 1: LOGIN ─────────────────────────────────────────────────────────
@Composable
fun LoginScreen(navController: NavController, vm: UserViewModel) {
    Box(Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.bghome), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Color.Black.copy(0.5f)))
        Column(
            Modifier.fillMaxSize().padding(32.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(Modifier.height(60.dp))
            Text("Simplify Your\nAcademic Life.", fontSize = 42.sp, fontWeight = FontWeight.Black, color = Color.White, lineHeight = 46.sp)
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = vm.profile.name,
                        onValueChange = { vm.updateName(it) },
                        label = { Text("Full Name", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { if(vm.profile.name.isNotBlank()) navController.navigate("home") }, Modifier.fillMaxWidth().height(52.dp)) {
                        Text("ENTER HUB", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

// ── SCREEN 2: HUB (HOME) ────────────────────────────────────────────────────
@Composable
fun HomeScreen(navController: NavController, vm: UserViewModel) {
    Box(Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.bghome), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(0.4f), Color.Black.copy(0.9f)))))

        Column(Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Row(Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 20.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("EduFlow.",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black)

                IconButton(onClick = { navController.navigate("profile") },
                    Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)) {
                    val initial = if (vm.profile.name.isNotEmpty()) vm.profile.name.take(1) else "A"
                    Text(initial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
            }

            OutlinedTextField(
                value = vm.searchQuery,
                onValueChange = { vm.searchQuery = it },
                placeholder = { Text("Search your courses...",
                color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.White.copy(0.25f), unfocusedContainerColor = Color.White.copy(0.2f)
                )
            )

            Spacer(Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                item {
                    Text("My Courses", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                }
                items(vm.filteredSubjects) { subject ->
                    var expanded by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth().animateContentSize().clickable { expanded = !expanded },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(subject.color.copy(alpha = 0.2f)), Alignment.Center) {
                                    Icon(Icons.Default.Book, null, tint = subject.color)
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(subject.name, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("Credits: ${subject.credits}", fontSize = 12.sp, color = Color.DarkGray)
                                }
                                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Black)
                            }
                            if (expanded) {
                                Spacer(Modifier.height(16.dp))
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                    Column {
                                        Text("Grade: ${subject.grade}", fontWeight = FontWeight.SemiBold, color = Color.Black)
                                        Text("Lecturer: ${subject.lecturer}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    IconButton(
                                        onClick = {
                                            vm.startEditing(subject)
                                            navController.navigate("add_form")
                                        },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(0.1f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

// ── SCREEN 3: FOCUS ─────────────────────────────────────────────────────────
@Composable
fun FocusScreen(vm: UserViewModel) {
    var studyMode by remember { mutableStateOf(false) }
    LaunchedEffect(vm.isTimerRunning) {
        while (vm.isTimerRunning && vm.timeLeft > 0) { delay(1000L); vm.timeLeft-- }
    }
    Column(
        Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Focus Mode", fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Lofi Mode", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Switch(checked = studyMode, onCheckedChange = { studyMode = it })
        }
        Spacer(Modifier.height(32.dp))
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = { vm.timeLeft / 1500f }, modifier = Modifier.size(200.dp), strokeWidth = 8.dp)
            Text("%02d:%02d".format(vm.timeLeft / 60, vm.timeLeft % 60), fontSize = 42.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(40.dp))
        Button(onClick = { vm.isTimerRunning = !vm.isTimerRunning }, modifier = Modifier.fillMaxWidth(0.6f).height(54.dp)) {
            Text(if (vm.isTimerRunning) "PAUSE" else "START FOCUS", fontWeight = FontWeight.ExtraBold)
        }
        TextButton(onClick = { vm.timeLeft = 1500; vm.isTimerRunning = false }) { Text("Reset", fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(20.dp))
    }
}

// ── SCREEN 4: PROFILE ───────────────────────────────────────────────────────
@Composable
fun ProfileScreen(navController: NavController, vm: UserViewModel) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bghome),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Color.Black.copy(0.85f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Box(Modifier.size(100.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), Alignment.Center) {
                //tempat nama keluar
                Text(if (vm.profile.name.isNotEmpty()) vm.profile.name.take(1) else "A",
                    fontSize = 40.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            Text(vm.profile.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold, color = Color.White)
            Text("${vm.profile.faculty} | ${vm.profile.studentId}",
                color = Color.White)

            Spacer(Modifier.height(32.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Current GPA", fontWeight = FontWeight.ExtraBold, color = Color.Black)
                            Text("%.2f".format(vm.totalGPA),
                                color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Year", fontWeight = FontWeight.ExtraBold, color = Color.Black)
                            Text("3", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("Progress to 4.0", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { (vm.totalGPA / 4.0).toFloat() },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                }
            }
            Spacer(Modifier.height(40.dp))
            Button(onClick = { vm.logout(); navController.navigate("login") { popUpTo("home") { inclusive = true } } }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("LOGOUT", fontWeight = FontWeight.Black, color = Color.White)
            }
        }
    }
}

// ── SCREEN 5: ADD/EDIT FORM ────────────────────────────────────────────────
@Composable
fun FormScreen(navController: NavController, vm: UserViewModel) {
    val colorOptions = listOf(Color(0xFF2196F3), Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFE91E63))
    var selectedColor by remember { mutableStateOf(vm.draftColor ?: colorOptions[0]) }

    Box(Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.bghome), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Color.Black.copy(0.75f)))

        Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text(if(vm.isEditing) "Edit Course" else "Add New Course", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color.White)
            Spacer(Modifier.height(32.dp))

            val fieldColors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)

            OutlinedTextField(
                value = vm.draftName,
                onValueChange = { vm.draftName = it },
                label = { Text("Course Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = vm.draftCredits,
                onValueChange = { vm.draftCredits = it },
                label = { Text("Credits") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = vm.draftGrade,
                onValueChange = { vm.draftGrade = it },
                label = { Text("Grade (e.g., A, B+)") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors)
            Spacer(Modifier.height(24.dp))

            Text("Choose Tag Color",
                color = Color.White,
                fontWeight = FontWeight.Bold)
            Row(Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                colorOptions.forEach { color ->
                    Box(
                        Modifier.size(36.dp).clip(CircleShape).background(color)
                            .clickable { selectedColor = color }
                            .border(if (selectedColor == color) 3.dp else 0.dp,
                                Color.White, CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
            Button(
                onClick = {
                    if (vm.isEditing) {
                        val index = vm.subjects.indexOfFirst { it.name == vm.draftName }
                        if (index != -1) {
                            vm.subjects[index] = SubjectRecord(vm.draftName, vm.draftCredits.toIntOrNull() ?: 0, vm.draftGrade, color = selectedColor)
                        } else {
                            vm.addSubject(selectedColor)
                        }
                    } else {
                        vm.addSubject(selectedColor)
                    }
                    navController.popBackStack()
                    vm.clearDrafts()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)
            ) { Text("SAVE CHANGES", fontWeight = FontWeight.Black) }
        }
    }
}

// ── PREVIEWS (FIXED) ───────────────────────────────────────────────────────


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PreviewLogin() {
    val vm = remember { UserViewModel() }
    AppTheme { LoginScreen(rememberNavController(), vm) }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PreviewHome() {
    val vm = remember { UserViewModel() }
    AppTheme { HomeScreen(rememberNavController(), vm) }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PreviewFocus() {
    val vm = remember { UserViewModel() }
    AppTheme { FocusScreen(vm) }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PreviewProfile() {
    val vm = remember { UserViewModel() }
    AppTheme { ProfileScreen(rememberNavController(), vm) }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PreviewForm() {
    val vm = remember { UserViewModel() }
    AppTheme { FormScreen(rememberNavController(), vm) }
}