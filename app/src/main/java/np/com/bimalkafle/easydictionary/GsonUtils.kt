package np.com.bimalkafle.easydictionary

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {
    val gson = Gson()

    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }

    inline fun <reified T> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }
}
