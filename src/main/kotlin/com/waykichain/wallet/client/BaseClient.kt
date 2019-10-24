package com.waykichain.wallet.client

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface BaseClient{
    val mDisposablePool: CompositeDisposable

    fun addDisposable(disposable: Disposable) {
        mDisposablePool.add(disposable)
    }
    fun onDetach() {
        if (!mDisposablePool.isDisposed) {
            mDisposablePool.clear()
        }
    }
}