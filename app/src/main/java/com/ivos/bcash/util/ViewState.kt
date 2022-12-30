package com.ivos.bcash.util

import com.ivos.bcash.data.model.Expense

sealed class ViewState {
    object Loading : ViewState()
    object Empty : ViewState()
    data class Success(val expense: List<Expense>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}