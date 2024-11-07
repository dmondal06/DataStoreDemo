package edu.farmingdale.datastoredemo.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.farmingdale.datastoredemo.R
import edu.farmingdale.datastoredemo.data.local.LocalEmojiData
import edu.farmingdale.datastoredemo.ui.theme.DataStoreDemoTheme

/*
 * Main App Composable that initializes the EmojiScreen with ViewModel
 */
@Composable
fun EmojiReleaseApp(
    emojiViewModel: EmojiScreenViewModel = viewModel(
        factory = EmojiScreenViewModel.Factory
    )
) {
    EmojiScreen(
        uiState = emojiViewModel.uiState.collectAsState().value,
        selectLayout = emojiViewModel::selectLayout,
    )
}

/*
 * This is emoji Screen Composable with a Top Bar, Layout Toggle, and Dark Mode Toggle
 * it's resposible for dark and light mode for the app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmojiScreen(
    uiState: EmojiReleaseUiState,
    selectLayout: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var isDarkTheme by rememberSaveable { mutableStateOf(false) } // Toggle for light-dark theme
    val isLinearLayout = uiState.isLinearLayout

    // Apply the theme with the correct `isDarkTheme` toggle
    DataStoreDemoTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.top_bar_name)) },
                    actions = {
                        // Toggle layout button (grid or list)
                        IconButton(
                            onClick = {
                                selectLayout(!isLinearLayout)
                            }
                        ) {
                            Icon(
                                painter = painterResource(uiState.toggleIcon),
                                contentDescription = stringResource(uiState.toggleContentDescription),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        // Toggle dark mode switch
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it } // Update theme toggle
                        )
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary
                    )
                )
            }
        ) { innerPadding ->
            val modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_medium),
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium),
                )
            if (isLinearLayout) {
                EmojiReleaseLinearLayout(
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = innerPadding
                )
            } else {
                EmojiReleaseGridLayout(
                    modifier = modifier,
                    contentPadding = innerPadding,
                )
            }
        }
    }
}

/*
 * thi is linear layout composable to display emojis in a list format
 * this is responsible for the linear layout
 * toast to make it clickable
 */
@Composable
fun EmojiReleaseLinearLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val cntxt = LocalContext.current
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    ) {
        items(
            items = LocalEmojiData.EmojiList,
            key = { e -> e }
        ) { e ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.clickable {
                    Toast.makeText(cntxt, "Emoji: $e", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    text = e, fontSize = 50.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/*
 * Grid layout composable to display emojis in a grid format
 * this is responsible for the grid layout
 * using toast to make it clickable
 */
@Composable
fun EmojiReleaseGridLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val cntxt = LocalContext.current
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(
            items = LocalEmojiData.EmojiList,
            key = { e -> e }
        ) { e ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .height(110.dp)
                    .clickable {
                        Toast.makeText(cntxt, "Emoji: $e", Toast.LENGTH_SHORT).show()
                    },
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = e, fontSize = 50.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .padding(dimensionResource(R.dimen.padding_small))
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
