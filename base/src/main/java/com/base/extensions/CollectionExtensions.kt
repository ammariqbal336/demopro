package com.base.extensions

fun <T>List<T>.getIfExist(index: Int) :T? {
    if(index >= 0 && index < this.size){
        return this.get(index) as T
    } else {
        return null
    }
}