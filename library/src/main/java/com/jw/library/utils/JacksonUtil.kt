package com.jw.library.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object JacksonUtil {

    var objectMapper: ObjectMapper? = null

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
    </Student> */
    fun <T> readValue(jsonStr: String, valueType: Class<T>): T? {
        if (objectMapper == null) {
            objectMapper = ObjectMapper()
        }

        try {
            return objectMapper!!.readValue(jsonStr, valueType)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * json数组转List
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    fun <T> readValue(jsonStr: String, valueTypeRef: TypeReference<T>): T? {
        if (objectMapper == null) {
            objectMapper = ObjectMapper()
        }

        try {
            return objectMapper!!.readValue(jsonStr, valueTypeRef)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object
     * @return
     */
    fun toJSon(`object`: Any): String? {
        if (objectMapper == null) {
            objectMapper = ObjectMapper()
        }

        try {
            return objectMapper!!.writeValueAsString(`object`)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}