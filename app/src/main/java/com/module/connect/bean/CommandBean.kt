package com.module.connect.bean

const val STYLE_ONE = 1
const val STYLE_TWO = 2
const val STYLE_THIRD = 3
const val STYLE_FOUR = 4
const val STYLE_FIVE = 5
const val STYLE_SIX = 6
const val STYLE_SEVEN = 7

data class CommandBean(
    val name: String,
    val command:String,
    var res: String,
    val style: Int = STYLE_ONE,
    val tip1: String?,
    val tip2: String?,
    val tip3: String?,
    val tip4: String?,
    val tip5: String?,
    val tip6: String?,
    val tip7: String?,
)