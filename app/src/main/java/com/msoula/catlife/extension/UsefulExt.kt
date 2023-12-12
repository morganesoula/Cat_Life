import android.util.Log

fun <T> Collection<T>?.notEmpty(): Boolean {
    return this?.isNotEmpty() == true
}

fun Any?.printToLog(tag: String = "XXX - ") {
    Log.d(tag, toString())
}