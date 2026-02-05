feature_auth
├── data
│   ├── repository
│   │   └── AuthRepositoryImpl.kt
│   │
│   └── datasource
│       └── AuthFakeDataSource.kt   // (optional)
│
├── domain
│   └── (❌ thường KHÔNG cần, dùng core/domain)
│
├── presentation
│   └── login
│       ├── LoginViewModel.kt
│       ├── LoginUiState.kt
│       ├── LoginScreen.kt
│       └── LoginViewModelFactory.kt (nếu chưa dùng Hilt)
│
│   └── register
│       ├── RegisterViewModel.kt
│       ├── RegisterUiState.kt
│       └── RegisterScreen.kt
│
└── AuthFeature.kt (optional)