package com.huydt.uikit.list.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Footer dùng chung cho 2 mode:
 *  - Infinite scroll: hiện loading spinner hoặc "Đã tải hết"
 *  - Pagination:      hiện Prev / Page info / Next
 */
@Composable
internal fun ListFooter(
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    isPaginationMode: Boolean,
    currentPage: Int,
    totalPages: Int,
    hasPreviousPage: Boolean,
    hasNextPage: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onGoToPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            /* ── Pagination controls ── */
            isPaginationMode -> {
                PaginationControls(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    hasPreviousPage = hasPreviousPage,
                    hasNextPage = hasNextPage,
                    onPrevious = onPrevious,
                    onNext = onNext,
                    onGoToPage = onGoToPage,
                )
            }

            /* ── Infinite scroll: đang load more ── */
            isLoadingMore -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.5.dp,
                )
            }

            /* ── Infinite scroll: hết data ── */
            !canLoadMore -> {
                Text(
                    text = "Đã tải hết",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    hasPreviousPage: Boolean,
    hasNextPage: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onGoToPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPageInput by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        /* Nút Prev */
        IconButton(
            onClick = onPrevious,
            enabled = hasPreviousPage,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Trang trước",
            )
        }

        /* Trang hiện tại / tổng — click để nhập tay */
        if (showPageInput) {
            PageInputField(
                currentPage = currentPage,
                totalPages = totalPages,
                onConfirm = { page ->
                    showPageInput = false
                    onGoToPage(page)
                },
                onDismiss = { showPageInput = false },
            )
        } else {
            TextButton(onClick = { if (totalPages > 1) showPageInput = true }) {
                Text(
                    text = "$currentPage / $totalPages",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }

        /* Nút Next */
        IconButton(
            onClick = onNext,
            enabled = hasNextPage,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Trang sau",
            )
        }
    }
}

@Composable
private fun PageInputField(
    currentPage: Int,
    totalPages: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(currentPage.toString()) }

    fun confirm() {
        val page = text.toIntOrNull()
        if (page != null && page in 1..totalPages) onConfirm(page)
        else onDismiss()
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it.filter { c -> c.isDigit() } },
        modifier = modifier.width(80.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { confirm() }),
        colors = OutlinedTextFieldDefaults.colors(),
        isError = text.toIntOrNull()?.let { it !in 1..totalPages } ?: true,
    )
}