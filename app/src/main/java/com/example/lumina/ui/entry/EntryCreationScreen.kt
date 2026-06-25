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
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.lumina.R
import com.example.lumina.domain.model.Mood
import com.example.lumina.ui.theme.LuminaTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EntryCreationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    EntryCreationContent(
        entryUiState = viewModel.entryUiState,
        onValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveEntry()
                navigateBack()
            }
        },
        navigateBack = navigateBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryCreationContent(
    entryUiState: EntryUiState,
    onValueChange: (EntryDetails) -> Unit,
    onSaveClick: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_journal_entry)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_description)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSaveClick,
                        enabled = entryUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.save_entry_content_description)
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
            onValueChange = onValueChange,
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
            label = { Text(stringResource(R.string.entry_title_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Date field (Read-only for now, representing the current date/selected date)
        val locale = LocalLocale.current.platformLocale
        OutlinedTextField(
            value = SimpleDateFormat("MMMM dd, yyyy", locale).format(Date(entryDetails.date)),
            onValueChange = { },
            label = { Text(stringResource(R.string.entry_date_label)) },
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
            label = { Text(stringResource(R.string.entry_content_label)) },
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
            label = { Text(stringResource(R.string.entry_mood_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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

@Preview(showBackground = true)
@Composable
fun EntryCreationPreview() {
    LuminaTheme {
        EntryCreationContent(
            entryUiState = EntryUiState(
                entryDetails = EntryDetails(
                    title = "A Great Day",
                    content = "Today was an amazing day! I learned a lot about Jetpack Compose.",
                    mood = Mood.HAPPY
                ),
                isEntryValid = true
            ),
            onValueChange = {},
            onSaveClick = {},
            navigateBack = {}
        )
    }
}
