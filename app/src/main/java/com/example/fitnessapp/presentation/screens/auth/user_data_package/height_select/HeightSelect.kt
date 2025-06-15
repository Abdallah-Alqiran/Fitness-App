
package com.example.fitnessapp.presentation.screens.auth.user_data_package.height_select

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.presentation.components.BottomButtonsSection
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataState
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun rememberPickerState() = remember { PickerState() }
class PickerState {
    var selectedItem by mutableStateOf("")
}
@Composable
fun Picker(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)
    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                val item = getItem(index)
                val isSelected = item == state.selectedItem
                Text(
                    text = item,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = if (isSelected) {
                        textStyle.copy(
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        textStyle
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier)
                )
            }
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NumberPickerDemo(
    onHeight: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val values = remember { (120..210).map { it.toString() } }
    val valuesPickerState = rememberPickerState()

    val saveUserDataViewModel = hiltViewModel<SaveUserDataViewModel>()
    val userDataState = saveUserDataViewModel.userDataState.collectAsStateWithLifecycle()

    var loadTrigger by remember { mutableStateOf(false) }

    if (loadTrigger) {
        LaunchedEffect(Unit) {
            saveUserDataViewModel.saveDataToFirestore(
                mapOf("height" to valuesPickerState.selectedItem)
            )
            loadTrigger = false
        }
    }

    when (userDataState.value) {
        is SaveUserDataState.Error -> {
            FailedLoadingScreen()
            return
        }

        SaveUserDataState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            return
        }

        is SaveUserDataState.Success -> {
            LaunchedEffect(Unit) {
                onHeight()
                saveUserDataViewModel.resetUserDataState()
            }
        }

        else -> Unit
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = maxWidth * 0.05f)
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Text(
                text = "Select your height",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = screenHeight * 0.02f)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = screenWidth * 0.07f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = valuesPickerState.selectedItem,
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = screenWidth * 0.02f)
                        )
                        Text(
                            text = "Cm",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Picker(
                        state = valuesPickerState,
                        items = values,
                        visibleItemsCount = 7,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.5f)
                            .align(Alignment.CenterHorizontally),
                        textModifier = Modifier.padding(10.dp),
                        textStyle = TextStyle(
                            fontSize = screenHeight.value.sp * 0.04f,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    )
                }
            }

            BottomButtonsSection(
                onContinueClick = {
                    loadTrigger = true
                },
                onBackClick = onBack,
            )
        }
    }
}
