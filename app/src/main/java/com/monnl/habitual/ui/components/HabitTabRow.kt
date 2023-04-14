package com.monnl.habitual.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.monnl.habitual.ui.habits.HabitCategoryTab

@Composable
fun HabitsTabRow(
    categories: List<HabitCategoryTab>,
    selectedCategoryIndex: Int,
    onCategorySelected: (index: Int) -> Unit = { }
) {
    TabRow(
        selectedTabIndex = selectedCategoryIndex
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedCategoryIndex,
                onClick = { onCategorySelected(index) }
            ) {
                Text(text = category.text)
            }
        }
    }
}