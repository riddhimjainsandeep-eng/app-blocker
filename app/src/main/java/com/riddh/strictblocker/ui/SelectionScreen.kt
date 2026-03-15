package com.riddh.strictblocker.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riddh.strictblocker.data.BlockedApp
import com.riddh.strictblocker.data.BlockedKeyword
import com.riddh.strictblocker.data.BlockedUrl
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionScreen(viewModel: BlockerViewModel, onBack: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Apps", "Websites", "Keywords")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forbidden Vault", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                divider = {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                text = title, 
                                fontSize = 15.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            ) 
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> AppSelectionList(viewModel)
                1 -> UrlSelectionList(viewModel)
                2 -> KeywordSelectionList(viewModel)
            }
        }
    }
}

@Composable
fun AppSelectionList(viewModel: BlockerViewModel) {
    val context = LocalContext.current
    val pm = context.packageManager

    val installedApps = remember {
        val intent = android.content.Intent(android.content.Intent.ACTION_MAIN, null).apply {
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
        }
        pm.queryIntentActivities(intent, 0)
            .map { it.activityInfo.applicationInfo }
            .distinctBy { it.packageName }
            .sortedBy { it.loadLabel(pm).toString() }
    }
    val blockedApps by viewModel.blockedApps.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(installedApps) { app ->
            val packageName = app.packageName
            val label = app.loadLabel(pm).toString()
            val isBlocked = blockedApps.any { it.packageName == packageName }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                ListItem(
                    headlineContent = { Text(label, fontWeight = FontWeight.Medium) },
                    supportingContent = { Text(packageName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                    trailingContent = {
                        Switch(
                            checked = isBlocked,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    viewModel.addApp(packageName, label)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.background,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                uncheckedTrackColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlSelectionList(viewModel: BlockerViewModel) {
    val blockedUrls by viewModel.blockedUrls.collectAsState()
    var newUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = newUrl,
            onValueChange = { newUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter domain (e.g. facebook.com)", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { 
                        if (newUrl.isNotBlank()) {
                            viewModel.addUrl(newUrl.trim().lowercase())
                            newUrl = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(blockedUrls) { url ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    ListItem(
                        leadingContent = {
                            coil.compose.AsyncImage(
                                model = "https://www.google.com/s2/favicons?domain=${url.url}&sz=128",
                                contentDescription = "Favicon",
                                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                            )
                        },
                        headlineContent = { Text(url.url, fontWeight = FontWeight.Medium, fontSize = 16.sp) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordSelectionList(viewModel: BlockerViewModel) {
    val blockedKeywords by viewModel.blockedKeywords.collectAsState()
    var newKeyword by remember { mutableStateOf("") }
    var keywordToConfirm by remember { mutableStateOf<String?>(null) }

    if (keywordToConfirm != null) {
        AlertDialog(
            onDismissRequest = { keywordToConfirm = null },
            title = { Text("Are you absolutely sure?", fontWeight = FontWeight.Bold) },
            text = { Text("Once you add '${keywordToConfirm}', you cannot remove it. It will be permanently blocked to enforce discipline.") },
            confirmButton = {
                Button(
                    onClick = {
                        keywordToConfirm?.let {
                            viewModel.addKeyword(it)
                            newKeyword = ""
                        }
                        keywordToConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679))
                ) {
                    Text("YES, BLOCK IT", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { keywordToConfirm = null }) {
                    Text("CANCEL", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = newKeyword,
            onValueChange = { newKeyword = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter exact keyword to block", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { 
                        if (newKeyword.isNotBlank()) {
                            keywordToConfirm = newKeyword.trim().lowercase()
                        }
                    }
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(blockedKeywords) { kw ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(kw.keyword, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 0.5.sp) },
                        leadingContent = { Icon(Icons.Default.Block, contentDescription = "Blocked", tint = Color(0xFFCF6679).copy(alpha = 0.7f), modifier = Modifier.size(20.dp)) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}