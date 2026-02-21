package com.huydt.uikit.list.data

import com.huydt.uikit.list.data.result.PagedResult

/**
 * Interface chuẩn cho mọi list data source.
 *
 * Tất cả param mới ([sortOption], [filters]) đều có **default value**
 * → các implementation cũ KHÔNG cần sửa, vẫn compile bình thường.
 *
 * Nếu impl muốn hỗ trợ sort/filter thì override và dùng các param đó.
 */
interface ListRepository<T> {

    /**
     * Lấy list đơn thuần (không có totalCount).
     * Trả về empty list nếu hết data.
     */
    suspend fun getItems(
        page: Int,
        pageSize: Int,
        query: String? = null,
        sortOption: SortOption? = null,
        filters: Map<String, Any> = emptyMap(),
    ): List<T> = emptyList()

    /**
     * Lấy list kèm totalCount / totalPages (dùng cho pagination).
     * Trả về null → ViewModel sẽ fallback sang [getItems].
     */
    suspend fun getPagedItems(
        page: Int,
        pageSize: Int,
        query: String? = null,
        sortOption: SortOption? = null,
        filters: Map<String, Any> = emptyMap(),
    ): PagedResult<T>? = null

    /** Thêm 1 item. */
    suspend fun add(item: T)

    /** Xóa 1 item. */
    suspend fun remove(item: T)
}