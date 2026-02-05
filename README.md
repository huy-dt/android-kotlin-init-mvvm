
```md
# 📦 Android-Init-Kotlin-MVVM (Android)

Ứng dụng Android demo **Order Server** được xây dựng bằng **Kotlin** theo kiến trúc **MVVM cơ bản**, mục tiêu để học và thực hành:
- Clean code
- Phân tách layer rõ ràng
- Quản lý state với ViewModel + StateFlow

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM (Model – View – ViewModel)
- **State management:** StateFlow
- **Asynchronous:** Kotlin Coroutines
- **DI:** Manual Dependency Injection (demo)
- **Build:** Gradle (Kotlin DSL / Groovy)

---

## 📐 Architecture Overview

Ứng dụng tuân theo mô hình **MVVM**:

```

UI (Compose)
↓ observe
ViewModel
↓ call
UseCase
↓ depend on
Repository (interface)
↓ implement
RepositoryImpl (Fake / Real)

```

### Giải thích:
- **View (Compose UI)**  
  Hiển thị dữ liệu và gửi action từ người dùng.
- **ViewModel**  
  Quản lý UI state, xử lý logic hiển thị.
- **UseCase**  
  Chứa business logic (ví dụ: login, order, validate).
- **Repository (interface)**  
  Định nghĩa contract lấy dữ liệu.
- **RepositoryImpl**  
  Cung cấp dữ liệu từ API / Fake data.

---

## 📂 Project Structure

```

com.huydt.orderserver
├── core
│   └── domain
│       ├── model
│       ├── repository
│       └── usecase
│
├── feature_order
│   ├── data
│   │   └── repository
│   ├── presentation
│   │   ├── OrderViewModel
│   │   └── OrderScreen
│
└── App.kt

````

---

## 🔄 UI State Management

UI state được quản lý bằng **StateFlow** trong ViewModel.

Ví dụ:

```kotlin
sealed interface OrderUiState {
    object Idle : OrderUiState
    object Loading : OrderUiState
    data class Success(val orders: List<Order>) : OrderUiState
    data class Error(val message: String) : OrderUiState
}
````

👉 Mỗi thời điểm UI chỉ có **1 trạng thái duy nhất**, giúp:

* Tránh state mập mờ
* Dễ debug
* Dễ mở rộng

---

## ▶️ Data Flow

```
User Action
   ↓
ViewModel
   ↓
UseCase
   ↓
Repository
   ↓
Result
   ↓
Update UiState
   ↓
UI recomposition
```

---

## 🧪 Fake Data

Ứng dụng sử dụng **Fake Repository** để demo, giúp:

* Không phụ thuộc backend
* Dễ test logic
* Dễ thay thế bằng API thật sau này

---

## 🎯 Mục tiêu của project

* Hiểu và áp dụng **MVVM cơ bản**
* Thực hành **StateFlow + Compose**
* Chuẩn bị nền tảng để học:

  * MVI
  * Clean Architecture nâng cao
  * Dependency Injection (Hilt)

---

## 🚀 Hướng phát triển tiếp theo

* Thêm API thật (Retrofit)
* Áp dụng Hilt
* Tách module rõ ràng hơn
* Thêm unit test cho UseCase & ViewModel

---

## 📌 Note

Đây là project **demo / học tập**, ưu tiên:

* Dễ hiểu
* Code rõ ràng
* Không over-engineering

---

## 👤 Author

* **Name:** Huy DT
* **Platform:** Android / Kotlin

```