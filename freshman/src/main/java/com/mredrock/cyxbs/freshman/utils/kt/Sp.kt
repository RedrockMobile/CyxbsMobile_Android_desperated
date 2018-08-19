package com.mredrock.cyxbs.freshman.utils.kt

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mredrock.cyxbs.freshman.ui.activity.App
import com.mredrock.cyxbs.freshman.utils.net.APIService
import io.reactivex.Observable

val gson = Gson()

val Context.defaultSp get() = sp("FreshManDefault")

val defaultSp get() = App.getContext().sp("FreshManDefault")

fun Context.sp(name: String): SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)

fun sp(name: String) = App.getContext().sp(name)

operator fun SharedPreferences.invoke(modify: SharedPreferences.Editor.() -> Unit) = edit().apply(modify).apply()

@JvmOverloads
fun <T> getBeanFromSP(keyName: String, clazz: Class<T>, spName: String = "FreshManDefault"): T? =
        try {
            gson.fromJson(sp(spName).getString(keyName, null), clazz)
        } catch (e: Exception) {
            null
        }

@JvmOverloads
fun <T> putBeanToSP(keyName: String, bean: T, spName: String = "FreshManDefault") =
        sp(spName)() { putString(keyName, gson.toJson(bean)) }

@JvmOverloads
fun <T> withSPCache(keyName: String, clazz: Class<T>, observable: APIService.() -> Observable<T>,
                    onGetBean: (T) -> Unit, fail: (Throwable) -> Unit = { it.printStackTrace() },
                    spName: String = "FreshManDefault") {
    val jsonFromSP = sp(spName).getString(keyName, null)
    try {
        gson.fromJson(jsonFromSP, clazz)
    } catch (e: Exception) {
        null
    }?.let { onGetBean(it) }
    getBeanFromNet({
        if (jsonFromSP != gson.toJson(it)) {
            putBeanToSP(keyName, it, spName)
            onGetBean(it)
        }
    }, fail, observable)
}

