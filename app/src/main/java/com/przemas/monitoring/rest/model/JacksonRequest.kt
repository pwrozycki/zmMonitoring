package com.przemas.monitoring.rest.model

import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class JacksonRequest<T>(
    method: Int,
    url: String,
    private val clazz: Class<T>,
    listener: Response.Listener<T>,
    errorListener: Response.ErrorListener,
    requestBody: Any? = null
) : JsonRequest<T>(
    method, url, requestBody?.let { writeAsString(requestBody) }, listener, errorListener
) {

    companion object {
        private var objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule())
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
        }

        fun writeAsString(obj: Any): String?  {
            return objectMapper.writeValueAsString(obj)
        }

        fun <T> readFromString(str: String, clazz: Class<T>) : T =
            if (clazz.isAssignableFrom(String::class.java)) str as T else objectMapper.readValue(str, clazz)
    }

    override fun getHeaders(): MutableMap<String, String> = hashMapOf()

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val readFromString =
                readFromString(response!!.data.toString(Charset.defaultCharset()), clazz)
            Log.i(javaClass.simpleName, readFromString.toString())
            Response.success(
                readFromString,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        }
    }
}