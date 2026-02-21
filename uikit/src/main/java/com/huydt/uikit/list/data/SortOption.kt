package com.huydt.uikit.list.data

/**
 * Mô tả cách sắp xếp một list.
 *
 * @param field   Tên field cần sort (ví dụ "name", "createdAt").
 * @param ascending true = A→Z / nhỏ→lớn, false = Z→A / lớn→nhỏ.
 */
data class SortOption(
    val field: String,
    val ascending: Boolean = true,
)