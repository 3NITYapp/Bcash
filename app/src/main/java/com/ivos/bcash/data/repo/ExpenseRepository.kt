package com.ivos.bcash.data.repo


import com.ivos.bcash.data.local.AppDatabase
import com.ivos.bcash.data.model.Expense
import javax.inject.Inject

/**
 ### `Singleton repository for storing data in database`
 */

class ExpenseRepository @Inject constructor(private val database: AppDatabase) {

    suspend fun insert(expense: Expense) = database.getExpenseDao().insert(expense)

    suspend fun update(expense: Expense) = database.getExpenseDao().update(expense)

    suspend fun delete(expense: Expense) = database.getExpenseDao().delete(expense)

    fun getAllItem() = database.getExpenseDao().getAllExpenses()

    fun getById(id: Int) = database.getExpenseDao().getById(id)

    suspend fun deleteById(id: Int) = database.getExpenseDao().deleteById(id)



}
