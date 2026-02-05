package com.xxx.app.feature_auth.ui.register

import com.xxx.app.core.domain.exception.auth.*

fun Throwable.toRegisterUiMessage(): String =
    when (this) {

        is UsernameEmptyException ->
            "Tên đăng nhập không được để trống"

        is UsernameTooShortException ->
            "Tên đăng nhập phải ít nhất 3 ký tự"

        is UsernameTooLongException ->
            "Tên đăng nhập không được vượt quá 30 ký tự"

        is UsernameAlreadyExistsException ->
            "Tên đăng nhập đã tồn tại"

        is InvalidEmailException ->
            "Email không hợp lệ"

        is PasswordEmptyException ->
            "Bạn chưa nhập mật khẩu"

        is PasswordTooShortException ->
            "Mật khẩu phải ít nhất 6 ký tự"

        is WeakPasswordException ->
            "Mật khẩu quá yếu (cần ký tự đặc biệt)"

        is java.net.UnknownHostException ->
            "Không có kết nối internet"

        else ->
            message ?: "Đã có lỗi xảy ra, vui lòng thử lại"
    }
