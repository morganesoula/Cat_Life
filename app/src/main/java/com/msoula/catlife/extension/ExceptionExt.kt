package com.msoula.catlife.extension

import com.msoula.catlife.feature_calendar.custom_places.data.state.State
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.resolveError(): State.ErrorState {
    var error = this

    when (this) {
        is SocketTimeoutException -> error =
            NetworkErrorException(errorMessage = "Connection error")
        is ConnectException -> error =
            NetworkErrorException(errorMessage = "No internet access")
        is UnknownHostException -> error =
            NetworkErrorException(errorMessage = "Unknown host")
    }

    if (this is HttpException) {
        when (this.code()) {
            502 -> error = NetworkErrorException(
                errorMessage = "Internal error with code: ${this.code()}"
            )
            401 -> throw AuthenticationException("Authentication error")
            404 -> error = NetworkErrorException.parseException(
                this
            )
        }
    }

    return State.ErrorState(error)
}

private open class NetworkErrorException(
    val errorMessage: String
) : Exception() {

    override val message: String?
        get() = localizedMessage

    override fun getLocalizedMessage(): String? {
        return errorMessage
    }

    companion object {
        fun parseException(exception: HttpException): NetworkErrorException {
            val errorBody = exception.response()?.errorBody()?.string()

            return try {
                NetworkErrorException(
                    errorMessage = JSONObject(errorBody!!).getString("message")
                )
            } catch (_: Exception) {
                NetworkErrorException(
                    errorMessage = "Into catch " + (exception.localizedMessage
                        ?: exception.message()
                            .toString())
                )
            }
        }
    }
}

private class AuthenticationException(authMessage: String) :
    NetworkErrorException(errorMessage = authMessage)