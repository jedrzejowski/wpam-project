package pl.gauganian.mytrash.helper

import java.lang.Exception

interface ErrorSink {
    fun handleWarnSink(tag: String, msg: String)
    fun handleErrorSink(tag: String, msg: String, e: Exception)
    fun handleErrorSink(tag: String, msg: Int, e: Exception)
}