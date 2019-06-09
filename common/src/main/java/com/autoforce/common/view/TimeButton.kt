package com.autoforce.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.autoforce.common.R
import com.autoforce.common.utils.RxDisposeManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 *  Created by xialihao on 2018/12/4.
 */
open class TimeButton(context: Context, attributeSet: AttributeSet? = null) : AppCompatTextView(context, attributeSet) {

    private var isCountDowning = false

    @SuppressLint("CheckResult")
    fun startCountDown() {
        isCountDowning = true
        Observable.intervalRange(1L, 61L, 0L, 1L, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<Long> {
                override fun onComplete() {
                    isEnabled = true
                    setText(R.string.get_verify_code)
                    isCountDowning = false
                    listener?.onComplete()
                }

                override fun onSubscribe(d: Disposable) {
                    RxDisposeManager.get().add(COUNT_DOWN_TAG, d)
                }

                override fun onNext(t: Long) {

                    text = "${60 - t}s"
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    fun clearCountDown() {
        RxDisposeManager.get().cancel(COUNT_DOWN_TAG)
    }

    fun isCountDowning() = isCountDowning

    var listener: OnFinishListener? = null

    interface OnFinishListener {

        fun onComplete()
    }

    companion object {
        private const val COUNT_DOWN_TAG = "count_down_register"
    }

}