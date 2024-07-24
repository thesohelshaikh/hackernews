package com.emergetools.hackernews.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emergetools.hackernews.data.UserStorage
import com.emergetools.hackernews.features.login.LoginDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class SettingsState(
  val loggedIn: Boolean
)

sealed interface SettingsAction {
  data object LoginPressed : SettingsAction
}

sealed interface SettingsNavigation {
  data object GoToLogin : SettingsNavigation {
    val login = LoginDestinations.Login
  }
}

class SettingsViewModel(userStorage: UserStorage) : ViewModel() {
  private val internalState = MutableStateFlow(SettingsState(false))

  val state = combine(
    userStorage.getCookie(),
    internalState.asStateFlow()
  ) { cookie, state ->
    if (!cookie.isNullOrEmpty()) {
      state.copy(loggedIn = true)
    } else {
      state
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(),
    initialValue = SettingsState(false)
  )

  fun actions(action: SettingsAction) {
    when (action) {
      SettingsAction.LoginPressed -> {
        // TODO
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(private val userStorage: UserStorage): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return SettingsViewModel(userStorage) as T
    }
  }
}