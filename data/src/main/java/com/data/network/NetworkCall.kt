package com.data.network

import TypeReference
import com.data.fromJson
import com.data.util.Applog
import com.domain.exceptions.*
import com.google.gson.JsonObject
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Dell 5521 on 9/28/2019.
 */
@Singleton
class NetworkCall @Inject constructor(
    var apiInterface: ApiInterface,
    var moshi: Moshi
) {
    //creating general methods for calling GET/POST request
    @Throws(Exception::class)
    suspend inline fun <reified T> generalRequest(
        crossinline request: suspend () -> Response<ResponseBody>
    ): Flow<T?> =
        flow {
            val response = request()
            Applog.d("APP:", "${response.raw()}")
            if (response.isSuccessful) {
                var responseString = response.body()?.string()
                Applog.d(responseString.toString())
                val type = object : TypeReference<T>() {}.type
                val model = moshi.fromJson<T>(responseString ?: "", type) as T?
                emit(model)
            } else if (response.code() == 422) {
                var errorResponse = response.errorBody()?.string() ?: "{}"
                throw _422Exception(handleErrors(errorResponse))
            } else if (response.code() == 500) {
                throw ServerException()
            } else if (response.code() == 404) {
                var errorResponse = response.errorBody()?.string() ?: "{}"
                val msg = handleErrors(errorResponse)
                throw _404Exception(if (msg.isEmpty().not()) msg else "URL Not found")
            } else if (response.code() == 401) {
                var errorResponse = response.errorBody()?.string() ?: "{}"
                throw _401Exception(handleErrors(errorResponse))
            } else {
                var errorResponse = response.errorBody()?.string() ?: "{}"
                throw Exception(handleErrors(errorResponse))
            }
        }


    //simple POST with data map
    @Throws(Exception::class)
    suspend inline fun <reified T> post(
        endpoint: String,
        bodyMap: Map<String, String>?
    )//data params
            : Flow<T?> {
        Applog.d("Payload: $bodyMap")
        val requestBodyMap = HashMap<String, @kotlin.jvm.JvmSuppressWildcards RequestBody>()
        if (bodyMap != null) {
            for ((key, value) in bodyMap) {
                requestBodyMap[key] = RequestBody.create(MediaType.parse("text/plain"), value)
            }
        }
        Applog.d("bodyMap", bodyMap.toString())
        return if (bodyMap == null) {
            generalRequest<T>({ apiInterface.post(endpoint) })
        } else {
            generalRequest<T>({ apiInterface.post(endpoint, requestBodyMap) })
        }
    }

    @Throws(Exception::class)
    suspend inline fun <reified T> post(
        endpoint: String,
        bodyMap: JsonObject?
    )//data params
            : Flow<T?> {
//        Applog.d("Payload: $bodyMap")
        // Applog.d(bodyMap)
        //val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)
        // val requestBodyMap = RequestBody.create(MediaType.parse("tex/json"), bodyMap)
        // Applog.d(requestBodyMap.toString())
        return if (bodyMap == null) {
            generalRequest<T>({ apiInterface.post(endpoint) })
        } else {
            generalRequest<T>({ apiInterface.post(endpoint, bodyMap) })
        }
    }


    //simple POST with data map
    @Throws(Exception::class)
    inline suspend fun <reified T> post(
        endpoint: String,
        bodyMap: String
    )//data params
            : Flow<T?> {
//        Applog.d("Payload: $bodyMap")
        val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)

        return generalRequest<T>({ apiInterface.post(endpoint, requestBody) })
    }

    @Throws(Exception::class)
    inline suspend fun <reified T> deleteWithBody(
        endpoint: String,
        bodyMap: JsonObject?
    )//data params
            : Flow<T?> {
        // val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)

        return generalRequest<T> { apiInterface.delete(endpoint, bodyMap = bodyMap) }
    }

    //post method with query map
    @Throws(Exception::class)
    inline suspend fun <reified T> post(
        endpoint: String,
        bodyMap: String,
        queryMap: Map<String, String>
    )//data params
            : Any? {

        val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)


        return generalRequest<T>({ apiInterface.post(endpoint, requestBody, queryMap) })
    }

    //put method with query map
    @Throws(Exception::class)
    inline suspend fun <reified T> put(
        endpoint: String,
        bodyMap: Map<String, String?>?
    )//data params
            : Flow<T?> {

        val requestBodyMap = HashMap<String, @kotlin.jvm.JvmSuppressWildcards RequestBody>()
        if (bodyMap != null) {
            for ((key, value) in bodyMap) {
                requestBodyMap[key] = RequestBody.create(MediaType.parse("text/plain"), value)
            }
        }
        Applog.d("bodyMap", bodyMap.toString())
        return if (bodyMap == null) {
            generalRequest<T>({ apiInterface.put(endpoint) })
        } else {
            generalRequest<T>({ apiInterface.put(endpoint, requestBodyMap) })
        }
    }

    @Throws(Exception::class)
    inline suspend fun <reified T> get(
        endpoint: String,
        queryMap: Map<String, String>? //query params
    ): Flow<T?> {

        Applog.d("queryMap", queryMap?.toString() ?: "")
        return if (queryMap == null)
            generalRequest<T> { apiInterface.get(endpoint) }
        else
            generalRequest<T> { apiInterface.get(endpoint, queryMap) }
    }


    @Throws(Exception::class)
    inline suspend fun <reified T> delete(
        endpoint: String
    ): Any? {
        return generalRequest<T> { apiInterface.delete(endpoint) }
    }


//    @Throws(Exception::class)
//    suspend inline fun <reified T> delete(
//        endpoint: String,
//        bodyMap: String
//    )//data params
//            : Flow<T?> {
//        val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)
//
//
//        return generalRequest<T>(suspend { apiInterface.delete(endpoint, requestBody) })
//    }

//    @Throws(Exception::class)
//    suspend inline fun <reified T> patch(
//        endpoint: String,
//        bodyMap: Map<String, String?>?,
//    )//data params
//            : Flow<T?> {
//
//        val requestBodyMap = HashMap<String, @kotlin.jvm.JvmSuppressWildcards RequestBody>()
//
//        if (bodyMap != null) {
//            for ((key, value) in bodyMap) {
//                requestBodyMap[key] = RequestBody.create(MediaType.parse("text/plain"), value)
//            }
//        }
//        Applog.d("bodyMap",bodyMap.toString())
//        return if (bodyMap == null) {
//            generalRequest<T>({ apiInterface.patch(endpoint) })
//        } else {
//            generalRequest<T>({ apiInterface.patch(endpoint, requestBodyMap) })
//        }
//    }


    @Throws(Exception::class)
    inline suspend fun <reified T> patch(
        endpoint: String,
        queryMap: Map<String, String>? //query params
    ): Flow<T?> {

        Applog.d("queryMap", queryMap?.toString() ?: "")
        return if (queryMap == null)
            generalRequest<T> { apiInterface.patch(endpoint) }
        else
            generalRequest<T> { apiInterface.patch(endpoint, queryMap) }
    }


    @Throws(Exception::class)
    suspend inline fun <reified T> _patch(
        endpoint: String,
        bodyMap: JsonObject?
    )//data params
            : Flow<T?> {
        // Applog.d(bodyMap)
        //val requestBody = RequestBody.create(MediaType.parse("application/json"), bodyMap)
        // val requestBodyMap = RequestBody.create(MediaType.parse("tex/json"), bodyMap)
        // Applog.d(requestBodyMap.toString())
        return if (bodyMap == null) {
            generalRequest<T>({ apiInterface.patch(endpoint) })
        } else {
            generalRequest<T>({ apiInterface.patch(endpoint, bodyMap) })
        }
    }


    fun handle422Error(
        errorResponse: String
    ): HashMap<String, String> {
        val errorMap = HashMap<String, String>()

        var err: JSONObject = JSONObject(errorResponse)
        if (err.has("errors")) {
            err = err.getJSONObject("errors")
        }
        if (err.has("error")) {
            err = err.getJSONObject("error")
        }

        val keys = err.keys()
        while (keys.hasNext()) {
            var dynamicKey = keys.next().toString()
            try {
                val jsonArray = err.getJSONArray(dynamicKey)
                if (jsonArray.length() > 0) {
                    errorMap.put(dynamicKey, jsonArray.getString(0))
                }
            } catch (e: Exception) {

            }
        }
        return errorMap
    }

    fun handleErrors(
        errorResponse: String
    ): String {
        var err: JSONObject = JSONObject(errorResponse)
        if (err.has("message"))
            return err.get("message").toString()

        if (err.has("error"))
            return err.get("error").toString()

        if (err.has("msg"))
            return err.get("msg").toString()

        return "Something went wrong"
    }
}
