package com.example.winebarcodereader.base.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.winebarcodereader.base.repository.BaseRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


abstract class BaseKotlinViewModel(private val repository: BaseRepository) : ViewModel(){
    abstract fun handleSharedPreferenceChangedListenerKey(key : String)

    protected var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    /**
     * RxJava 의 observing을 위한 부분.
     * addDisposable을 이용하여 추가하기만 하면 된다
     */
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * progress bar on/off
     */
    protected fun setProgressBar(state:Boolean){
        _isLoading.postValue(state)
    }

    private val sharedPreferenceListener = SharedPreferences.OnSharedPreferenceChangeListener{ sharedPreferences, key ->
        Log.d(this.javaClass.simpleName,
            "sharedPreferenceListener key :: $key"
        )
        handleSharedPreferenceChangedListenerKey(key)
    }

    fun registerSharedPreferenceChangedListner(){
        repository.registerSharedPreferenceChangedListner(sharedPreferenceListener)
    }

    fun unregisterSharedPreferenceChangedListner(){
        repository.unregisterSharedPreferenceChangedListner(sharedPreferenceListener)
    }
}