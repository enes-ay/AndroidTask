# Screenshots
<div style="display:flex; gap:10px;">
  <img src="screenshots/QrSearch.gif" width="20%" />
  <img src="screenshots/regularSearch.gif" width="20%" />
  <img src="screenshots/swipeTorefresh.gif" width="20%" />
  <img src="screenshots/taskList.gif" width="20%" />
</div>

# Architecture & Data Flow
The project implements Clean Architecture with MVVM pattern, adhering to the Single Source of Truth (SSOT) principle and it also follows Atomic Commit principle as much as possible.
- Data Layer (The Foundation)
  - Single Source of Truth: The UI always consumes data from the local Room Database. The network only serves to update this local cache.
  - Secure Networking: Retrofit manages API calls. An AuthInterceptor injects the JWT token (secured in EncryptedSharedPreferences) into every request header automatically.
  - Repository: Orchestrates data synchronization between the API and Database.
  - Critical Solution (Missing IDs): Since the backend response lacks unique IDs, a custom Hash Logic is implemented during mapping. It generates stable IDs based on immutable (assumption) fields 
    (title + task + businessUnit), ensuring duplicate-free caching and efficient DiffUtil operations. 
    (prevent redundant recomposition and list flickering)

- Domain Layer (Pure Logic)
  - UseCases: Specific business actions (GetTasksUseCase, SearchTasksUseCase, RefreshTasksUseCase) are encapsulated here, keeping the ViewModel slim and focused.
  - Decoupled Models: Uses distinct Domain Models (TaskModel), separating UI logic from API DTOs and Database Entities.

- Presentation Layer (UI)
  - Jetpack Compose: Built with Material3, providing a fully reactive UI.
  - Reactive State Management:
  - The ViewModel exposes a single TaskListUiState.
  - Uses advanced Flow operators (combine, flatMapLatest) to merge Search Queries, Loading States, and Database Streams seamlessly.
  - Unidirectional Data Flow: The View simply observes state and triggers events (e.g., OnRefresh, OnSearch), completing the cycle without manual data fetching.
