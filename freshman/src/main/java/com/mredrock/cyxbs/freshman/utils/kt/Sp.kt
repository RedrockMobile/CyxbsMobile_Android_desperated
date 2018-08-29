package com.mredrock.cyxbs.freshman.utils.kt

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mredrock.cyxbs.freshman.ui.activity.App
import com.mredrock.cyxbs.freshman.utils.net.APIService
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val gson = Gson()

val defaultSp get() = App.getContext().sp("FreshManDefault")

fun Context.sp(name: String): SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)

fun sp(name: String) = App.getContext().sp(name)

operator fun SharedPreferences.invoke(modify: SharedPreferences.Editor.() -> Unit) = edit().apply(modify).apply()

@JvmOverloads
fun <T> getBeanFromSP(keyName: String, clazz: Class<T>, spName: String = "FreshManDefault"): T? =
        label@ try {
            gson.fromJson(sp(spName).getString(keyName, null) ?: return@label null, clazz)
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
    Observable.create<String?> { it.onNext(sp(spName).getString(keyName, "")) }
            .doOnNext { json ->
                observable(HttpLoader.service)
                        .filter { gson.toJson(it) != json }
                        .doOnNext { putBeanToSP(keyName, it, spName) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onGetBean, fail)
            }.map {
                if (it.isNotBlank()) {
                    gson.fromJson(it, clazz)
                } else {
                    null
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { null }
            .subscribe({ it?.let(onGetBean) }, { (it as? JsonSyntaxException) ?: fail(it) })
}
