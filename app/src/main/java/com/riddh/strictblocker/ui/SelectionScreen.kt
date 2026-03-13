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
                title = { Text("Forbidden Vault", style = MaterialTheme.typography.titleLarge) },
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
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 14.sp) }
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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(installedApps) { app ->
            val packageName = app.packageName
            val label = app.loadLabel(pm).toString()
            val isBlocked = blockedApps.any { it.packageName == packageName }

            ListItem(
                headlineContent = { Text(label) },
                supportingContent = { Text(packageName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                trailingContent = {
                    Checkbox(
                        checked = isBlocked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addApp(packageName, label)
                            }
                            // Do nothing if unchecked to prevent easy undo
                        }
                    )
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlSelectionList(viewModel: BlockerViewModel) {
    val blockedUrls by viewModel.blockedUrls.collectAsState()
    var newUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newUrl,
                onValueChange = { newUrl = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("domain (e.g. facebook.com)") },
                singleLine = true,
                colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
            IconButton(onClick = { 
                if (newUrl.isNotBlank()) {
                    viewModel.addUrl(newUrl.trim().lowercase())
                    newUrl = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(blockedUrls) { url ->
                ListItem(
                    leadingContent = {
                        coil.compose.AsyncImage(
                            model = "https://www.google.com/s2/favicons?domain=${url.url}&sz=64",
                            contentDescription = "Favicon",
                            modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp))
                        )
                    },
                    headlineContent = { Text(url.url) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).padding(vertical = 4.dp)
                )
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
            title = { Text("Are you absolutely sure?") },
            text = { Text("Once you add '${keywordToConfirm}', you cannot remove it. It will be permanently blocked.") },
            confirmButton = {
                TextButton(onClick = {
                    keywordToConfirm?.let {
                        viewModel.addKeyword(it)
                        newKeyword = ""
                    }
                    keywordToConfirm = null
                }) {
                    Text("YES, BLOCK IT", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { keywordToConfirm = null }) {
                    Text("CANCEL", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newKeyword,
                onValueChange = { newKeyword = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter keyword to block") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { 
                    if (newKeyword.isNotBlank()) {
                        keywordToConfirm = newKeyword.trim().lowercase()
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(blockedKeywords) { kw ->
                ListItem(
                    headlineContent = { Text(kw.keyword, fontWeight = FontWeight.SemiBold) },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.padding(vertical = 4.dp).clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}