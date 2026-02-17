package com.huydt.uikit.topbar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Một option sort.
 */
data class SortOption(
    val id: String,
    val label: String
)

/**
 * Hướng sort.
 */
enum class SortDirection { ASC, DESC }

/**
 * State sort hiện tại.
 */
data class SortState(
    val selectedId: String? = null,
    val direction: SortDirection = SortDirection.ASC
)

/**
 * SortBar inline thay thế topbar khi người dùng click Sort.
 */
@Composable
fun InlineSortBar(
    options: List<SortOption>,
    sortState: SortState,
    onSelect: (optionId: String) -> Unit,
    onToggleDir: () -> Unit,
    onClear: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // ── Header ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (sortState.selectedId != null) {
                    IconButton(onClick = onToggleDir, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (sortState.direction == SortDirection.ASC)
                                Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = "Toggle direction",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(
                        onClick = onClear,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("Clear", style = MaterialTheme.typography.labelMedium)
                    }
                }
                IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                }
            }

            // ── Sort chips ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    val selected = option.id == sortState.selectedId
                    FilterChip(
                        selected = selected,
                        onClick = { onSelect(option.id) },
                        label = {
                            Text(
                                text = option.label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        trailingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = if (sortState.direction == SortDirection.ASC)
                                        Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}