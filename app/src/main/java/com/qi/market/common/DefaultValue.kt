package com.qi.market.common

/**
 * Created by Qi on 2017/11/26.
 */
class DefaultValue {
    companion object {
        fun getValue(value: String?): String {
            return if (value.isNullOrEmpty() || value!!.isBlank()) ""
            else value
        }

        fun getValue(value: Int?) = value ?: 0
        fun getValue(value: Long?) = value ?: 0
        fun getValue(value: Double?) = value ?: 0
    }

}