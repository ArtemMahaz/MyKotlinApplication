package com.artem.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem.myapplication.network.RetrofitClient
import com.artem.myapplication.network.ScheduleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScheduleState {
    object Loading : ScheduleState()
    data class Success(val data: ScheduleResponse) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}

class ScheduleViewModel : ViewModel() {
    private val _state = MutableStateFlow<ScheduleState>(ScheduleState.Loading)
    val state: StateFlow<ScheduleState> = _state.asStateFlow()

    init {
        fetchSchedule()
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            _state.value = ScheduleState.Loading
            try {
                val response = RetrofitClient.api.getSchedule()
                _state.value = ScheduleState.Success(response)
            } catch (e: Exception) {
                _state.value = ScheduleState.Error(e.message ?: "Невідома помилка")
            }
        }
    }
}