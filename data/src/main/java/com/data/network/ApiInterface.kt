package com.data.network


import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

public interface ApiInterface {

    //Creating GET/POST request
    @GET
    suspend fun get(@Url endpoint: String): Response<ResponseBody>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @QueryMap query: @JvmSuppressWildcards Map<String, String>
    ): Response<ResponseBody>

    @POST
    suspend fun post(@Url endpoint: String): Response<ResponseBody>

    //multipart POST request for uploading files
    @Multipart
    @POST
    suspend fun post(
        @Url endpoint: String,
        @PartMap bodyMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<ResponseBody>

    //multipart POST request for uploading files
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Body bodyMap: RequestBody
    ): Response<ResponseBody>

    //post with query map
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Body bodyMap: RequestBody,
        @QueryMap query: @JvmSuppressWildcards Map<String, String>
    ): Response<ResponseBody>

    @POST
    suspend fun post(
        @Url endpoint: String,
        @Body bodyMap: JsonObject,

    ): Response<ResponseBody>


    //single File
    //multipart POST request for uploading files
    @Multipart
    @POST
    suspend fun post(
        @Url endpoint: String,
        @PartMap bodyMap: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part file: @JvmSuppressWildcards MultipartBody.Part
    ): Response<ResponseBody>

    //multiple Files -- Can be used for 1 file also
    //Recommended to use this as this one method can cover all scenario
    //multipart POST request for uploading files
    @Multipart
    @POST
    suspend fun post(
        @Url endpoint: String,
        @PartMap bodyMap: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part files: @JvmSuppressWildcards List<MultipartBody.Part>
    ): Response<ResponseBody>

    //delete request is not working with bodyMap.. retrofit is not accepting
    //only simple delete is working
    @DELETE
    suspend fun delete(@Url endpoint: String): Response<ResponseBody>

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun delete(
        @Url endpoint: String,
        @PartMap bodyMap: Map<String, RequestBody>
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun delete(
        @Url endpoint: String,
        @Body bodyMap: JsonObject?,
    ): Response<ResponseBody>

    @Multipart
    @PUT
    suspend fun put(
        @Url endpoint: String,
        @PartMap bodyMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<ResponseBody>

    @PUT
    suspend fun put(@Url endpoint: String): Response<ResponseBody>

    @PATCH
    suspend fun patch(
        @Url endpoint: String,
        @Body bodyMap: JsonObject
    ): Response<ResponseBody>

//    @Multipart
//    @PATCH
//    suspend fun patch(
//        @Url endpoint: String,
//        @PartMap bodyMap: Map<String, @JvmSuppressWildcards RequestBody>
//    ): Response<ResponseBody>

    @PATCH
    suspend fun patch(@Url endpoint: String): Response<ResponseBody>


    @PATCH
    suspend fun patch(
        @Url endpoint: String,
        @QueryMap query: @JvmSuppressWildcards Map<String, String>
    ): Response<ResponseBody>




}
