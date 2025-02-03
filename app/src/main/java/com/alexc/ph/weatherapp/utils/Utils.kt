package com.alexc.ph.weatherapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.alexc.ph.domain.model.Result


const val TAG = "AppTag"
const val EMPTY_STRING = ""

fun printError(e: Exception) = Log.e(TAG, "${e.message}")

fun showToastError(
    context: Context,
    e: Exception
) = makeText(context, "${e.message}", LENGTH_LONG).show()

fun showToastMessage(
    context: Context,
    resourceId: Int
) = makeText(context, context.resources.getString(resourceId), LENGTH_LONG).show()

suspend fun <T> launchCatching(block: suspend () -> T) = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Error(e)
}