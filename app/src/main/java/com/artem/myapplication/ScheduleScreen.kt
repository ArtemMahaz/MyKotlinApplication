package com.artem.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel = viewModel()) {
    // Спостерігаємо за станом із ViewModel
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ScheduleState.Loading -> {
            // Показуємо крутилку завантаження на ті 1.5 секунди затримки
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ScheduleState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Помилка: ${currentState.message}", color = MaterialTheme.colorScheme.error)
            }
        }
        is ScheduleState.Success -> {
            val days = currentState.data.days
            // Стан для свайпера сторінок
            val pagerState = rememberPagerState(pageCount = { days.size })
            val coroutineScope = rememberCoroutineScope()

            Column(modifier = Modifier.fillMaxSize()) {
                // Вкладки з назвами днів
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    days.forEachIndexed { index, daySchedule ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                // Анімовано гортаємо до вибраного дня при кліку на вкладку
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(daySchedule.dayOfWeek) }
                        )
                    }
                }

                // Сам контент, який можна свайпати вліво-вправо
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    val dayData = days[page]

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(dayData.classes) { classItem ->
                            // Картка для кожної окремої пари
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${classItem.lessonNumber} пара",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = classItem.time,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = classItem.subject,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Викладач: ${classItem.teacher}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Аудиторія: ${classItem.room}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
