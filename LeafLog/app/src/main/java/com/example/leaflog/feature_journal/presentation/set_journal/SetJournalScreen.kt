package com.example.leaflog.feature_journal.presentation.set_journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_journal.presentation.component.BookEntry
import com.example.leaflog.feature_journal.presentation.component.BookImageSelector
import com.example.leaflog.ui.theme.LeafLogTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SetJournalScreen(
    viewModel: SetJournalViewModel = viewModel()
) {
    val background = Color(0xFFF77171)
    Scaffold(
        topBar = {
            Box(modifier = Modifier.height(80.dp)) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .background(background)
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BookImageSelector(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .width(250.dp),
                ) {

                }
                BookEntry(
                    modifier = Modifier.padding(bottom = 20.dp),
                    placeholder = "Title",
                    value = "",
                ) {

                }
                BookEntry(placeholder = "Description", value = "", multiline = true) {

                }
            }
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
                label = "Confirm",
                leadingIcon = Icons.Outlined.Create,
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun PreviewSetJournalScreen() {
    LeafLogTheme {
        SetJournalScreen()
    }
}