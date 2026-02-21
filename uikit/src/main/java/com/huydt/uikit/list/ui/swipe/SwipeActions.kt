// package com.huydt.uikit.list.ui.swipe

// data class SwipeActions<T>(
//     val start: List<SwipeAction<T>> = emptyList(),
//     val end: List<SwipeAction<T>> = emptyList()
// ) {
//     val enabled: Boolean
//         get() = start.isNotEmpty() || end.isNotEmpty()
// }

package com.huydt.uikit.list.ui.swipe

data class SwipeAction(
    val id: String,
    val label: String,
    val icon: Int? = null,
    val backgroundColor: Int? = null,
    val textColor: Int? = null,
)

data class SwipeActions(
    val startActions: List<SwipeAction> = emptyList(),  // left→right
    val endActions: List<SwipeAction> = emptyList(),    // right→left
)