package com.example.sportsnote.ui.target.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sportsnote.R
import com.example.sportsnote.model.Target
import com.example.sportsnote.ui.components.items.ItemLabel

/**
 * 年間目標と月間目標を表示するコンポーネント
 *
 * @param yearlyTarget 年間目標
 * @param monthlyTarget 月間目標
 */
@Composable
fun TargetDisplaySection(
    yearlyTarget: Target?,
    monthlyTarget: Target?,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(8.dp),
    ) {
        Column {
            // 年間目標
            yearlyTarget?.let {
                ItemLabel(stringResource(R.string.targetYear, it.title))
            } ?: ItemLabel(stringResource(R.string.targetYear, stringResource(R.string.targetNotFound)))

            // 月間目標
            monthlyTarget?.let {
                ItemLabel(stringResource(R.string.targetMonth, it.title))
            } ?: ItemLabel(stringResource(R.string.targetMonth, stringResource(R.string.targetNotFound)))
        }
    }
}
