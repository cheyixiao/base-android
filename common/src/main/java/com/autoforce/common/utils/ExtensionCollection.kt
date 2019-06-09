package com.autoforce.common.utils

import android.widget.EditText

/**
 *  Created by xialihao on 2018/12/6.
 */

fun EditText.isEmpty(): Boolean {
    return text.toString().isEmpty()
}

fun EditText.toInt(): Int {
    return text.toString().toInt()
}

fun EditText.toLong(): Long {
    return text.toString().toLong()
}

fun EditText.moveCursorLast() {
    setSelection(text.length)
}

