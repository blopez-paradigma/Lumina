package com.example.lumina.ui.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumina.data.Mood
import com.example.lumina.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryCreationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val entryUiState = viewModel.entryUiState

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("New Journal Entry") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.saveEntry()
                                navigateBack()
                            }
                        },
                        enabled = entryUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save Entry"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        EntryForm(
            entryDetails = entryUiState.entryDetails,
            onValueChange = viewModel::updateUiState,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    entryDetails: EntryDetails,
    onValueChange: (EntryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = entryDetails.title,
            onValueChange = { onValueChange(entryDetails.copy(title = it)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Date field (Read-only for now, representing the current date/selected date)
        OutlinedTextField(
            value = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(entryDetails.date)),
            onValueChange = { },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        MoodDropdown(
            selectedMood = entryDetails.mood,
            onMoodSelected = { onValueChange(entryDetails.copy(mood = it)) }
        )

        OutlinedTextField(
            value = entryDetails.content,
            onValueChange = { onValueChange(entryDetails.copy(content = it)) },
            label = { Text("How was your day?") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDropdown(
    selectedMood: Mood,
    onMoodSelected: (Mood) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedMood.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
            label = { Text("Mood") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Mood.entries.forEach { mood ->
                DropdownMenuItem(
                    text = {
                        Text(text = mood.name.lowercase().replaceFirstChar { it.uppercase() })
                    },
                    onClick = {
                        onMoodSelected(mood)
                        expanded = false
                    }
                )
            }
        }
    }
}
