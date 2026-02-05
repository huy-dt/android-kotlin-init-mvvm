package com.xxx.app.feature_auth.ui.login

import com.xxx.app.core.domain.exception.auth.*

fun Throwable.toUiMessage(): String =
    when (this) {
        is PasswordEmptyException ->
            "Bạn chưa nhập mật khẩu"

        is PasswordTooShortException ->
            "Mật khẩu phải ít nhất 8 ký tự"

        is WeakPasswordException ->
            "Mật khẩu quá yếu (cần chữ hoa và số)"

        is InvalidUsernameException ->
            "Tên đăng nhập không hợp lệ"

        is InvalidCredentialsException ->
            "Sai tài khoản hoặc mật khẩu"

        is java.net.UnknownHostException ->
            "Không có kết nối internet"

        else ->
            message ?: "Đã có lỗi xảy ra, vui lòng thử lại"
    }
