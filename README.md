# Screenshots
<div style="display:flex; gap:10px;">
  <img src="screenshots/QrSearch.gif" width="20%" />
  <img src="screenshots/regularSearch.gif" width="20%" />
  <img src="screenshots/swipeTorefresh.gif" width="20%" />
  <img src="screenshots/taskList.gif" width="20%" />
</div>

# Architecture & Data Flow
The project implements Clean Architecture with MVVM pattern, adhering to the Single Source of Truth (SSOT) principle.
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

Hello dear Android dev prospect!

This repository is supposed to act as a playground for your submission.
Before getting started, please make sure to clone this repository on which you will commit and push your code regularly. Once you are ready, please mail us back the link to your repository. 

Below, you will find the **Task** definition. Happy Hacking :computer: and Good Luck :shamrock:

# Task

Write an Android application that connects to a remote API, downloads a certain set of resources, shows them in a list and provides some basic searching/filtering feature-set.
In particular, the app should:

- Request the resources located at `https://api.baubuddy.de/dev/index.php/v1/tasks/select` 
- Store them in an appropriate data structure that allows using the application offline
- Display all items in a list showing `task`, `title`, `description` and `colorCode` (which should be a view colored according to `colorCode`)
- The app should offer a search menu item (search bar not a separate screen) that allows searching for any of the class properties (even those, that are not visible to the user directly)
- The app should offer a menu item that allows scanning for QR-Codes
  - Upon successful scan, the search query should be set to the scanned text
- In order to refresh the data, the app should offer:
  - a swipe-2-refresh functionality
  - and a worker that requests the resources from above every 60 minutes

### Authorization

It's mandatory for your requests towers the API to be authorized. You can find the required request below:

This is how it looks in `curl`:

```bash
curl --request POST \
  --url https://api.baubuddy.de/index.php/login \
  --header 'Authorization: Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz' \
  --header 'Content-Type: application/json' \
  --data '{
        "username":"365",
        "password":"1"
}'
```

The response will contain a json object, having the access token in `json["oauth"]["access_token"]`. For all subsequent calls this has to be added to the request headers as `Authorization: Bearer {access_token}`.

A possible implementation in `Kotlin` could be the following. You don't have to copy over this one, feel free to indivualize it or use a different network library.

```kotlin
val client = OkHttpClient()
val mediaType = MediaType.parse("application/json")
val body = RequestBody.create(mediaType, "{\n        \"username\":\"365\",\n        \"password\":\"1\"\n}")
val request = Request.Builder()
  .url("https://api.baubuddy.de/index.php/login")
  .post(body)
  .addHeader("Authorization", "Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz")
  .addHeader("Content-Type", "application/json")
  .build()
val response = client.newCall(request).execute()
```
