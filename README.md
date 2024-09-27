# Sample App to Demonstrate Background Worker and Room Caching

This app was initially built to practice data caching before entering SPECTIV. Basically this is a sample app to record inbound and outbound truck weights.

Some of the technologies used in this project are listed below:

* Language : Kotlin
* Dependency Injection : Hilt
* UI : XML & Compose
* Design Pattern : MVVM & Clean Architecture
* Sync / Async Task : Kotlin Coroutine
* Reactive Programming : LiveData & Kotlin Flow
* Storage : Room Database & Firebase Realtime Database
* Unit Testing : MockK, Turbine

## Background Worker
This worker is responsible for synchronizing data with Firebase. The first step is to fetch data from Room and check if there are any pending actions. If pending actions exist, the worker pushes the data to Firebase and removes the corresponding data from the local database.

```kotlin
override suspend fun doWork(): Result {
        val pendingChanges = pendingAction.getPendingActionList()
        for (pending in pendingChanges) {
            when (pending.action) {
                PendingActionOption.ADD, PendingActionOption.EDIT  -> {
                    when (networkSource.editTicket(pending.data)) {
                        is RemoteResult.OnSuccess -> {
                            pendingAction.deletePendingActionById(pending.id)
                        }

                        else                      -> {
                            return Result.retry()
                        }
                    }
                }

                else                     -> {
                    when (networkSource.deleteTicketById(pending.data.id)) {
                        is RemoteResult.OnSuccess -> {
                            pendingAction.deletePendingActionById(pending.id)
                        }

                        else                      -> return Result.retry()
                    }
                }
            }
        }
        return Result.success()
    }
```

```kotlin
@HiltAndroidApp
class MainApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}
```

You can configure how the worker will operate, whether it runs periodically or only once.

## Room Caching
Room caching in this app is to handle poor network scenario. So, when user lost their internet connection they still can use the app.

In this example, if there is no internet user still can add a new ticket but it will be stored in Room and marked as a `Pending Action`. Then when the internet connection is recovered, the background worker will take an action to push the pending tickets to Firebase.

```kotlin
override suspend fun addNewTicket(data: TicketItem): RemoteResult<String> {
        return withContext(ioDispatcher) {
            if (networkCheck.isConnected()) {
                when (val result = networkSource.editTicket(data)) {
                    is RemoteResult.OnSuccess -> {
                        localSource.editTicket(data)
                        result
                    }

                    else                      -> {
                        localSource.cacheTicket(data)
                        pendingAction.addNewPendingAction(PendingActionOption.ADD, data = data)
                        RemoteResult.OnSuccess(data = "success")
                    }
                }
            } else {
                localSource.cacheTicket(data)
                pendingAction.addNewPendingAction(PendingActionOption.ADD, data = data)
                RemoteResult.OnSuccess(data = "success")
            }
        }
    }
```

## Compose Interoperability

For layouting, this app also use a bit of jetpack compose to demonstrate the interoperability of jetpack compose with existing project (xml).

Basically you can add a compose view as a View in xml layout. Then you can set the content of compose in fragment. You can find the code at here or here is some of the snippet code :

* First Step (Define compose view in xml layout)
```xml
<androidx.constraintlayout.widget.ConstraintLayout
..
..
    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/compose_view"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbar"/>
..
..
<androidx.constraintlayout.widget.ConstraintLayout/>
```

* Second Step (define compose content in fragment)
```kotlin
override fun initView() {
        vm.getTicketList()
        bind.composeView.setContent {
            val isRefresh = vm.ticketList.observeAsState().value?.loading ?: false
            val list = vm.ticketList.observeAsState().value?.data ?: emptyList()
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefresh,
                onRefresh = { vm.getTicketList() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .padding(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(list) {
                        TicketItem(data = it) {
                            ticketDetailSheet.arguments = Bundle().apply { putParcelable(DATA, it) }
                            ticketDetailSheet.show(
                                childFragmentManager,
                                getString(R.string.ticketdetailsheet)
                            )
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefresh,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
```