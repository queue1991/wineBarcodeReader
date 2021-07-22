package com.example.winebarcodereader.base.view

import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.winebarcodereader.service.CheckNetworkConnectionService
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.winebarcodereader.base.viewmodel.BaseKotlinViewModel
import com.example.winebarcodereader.util.NetworkCheckUtil

/**
 * ViewDataBinding, BaseKotlinViewModel 를 유형매개변수로 받는 abstract class
 * 1. inflate View
 * 2. databinding 준비
 * 3. observe livedata
 * 4. network state 상태 변화 감지
 */
abstract class BaseKotlinActivity<T : ViewDataBinding, R : BaseKotlinViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T

    /**
     * setContentView로 호출할 Layout의 리소스 Id.
     */
    abstract val layoutResourceId: Int

    /**
     * common Loading View 관련
     */
    abstract val parentView : ViewGroup
    lateinit var loadingView: View
    lateinit var animation: AnimationDrawable
    lateinit var animationView: ImageView

    /**
     * viewModel 로 쓰일 변수.
     */
    abstract val viewModel: R

    /**
     * network 체크 클래스 변수
     */
    private val checkNetworkConnectionUtil: CheckNetworkConnectionService
        get() = CheckNetworkConnectionService(
            this,
            networkCallback
    )

    /**
     * network 상태 변수
     * onAvailable -> true
     * onLost -> false
     */
    private var networkState:Boolean = true


    /**
     * network 상태 변화 감지하는 Service 변수 초기화
     */
    private val networkCallback = object:ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            // 기존에 Connected 였던 상황에서는 do not handle
            if(!networkState){
                networkState = true
                handleNetworkStateForService()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            // 기존에 disconnected 였던 상황에서는 do not handle
            if(networkState){
                networkState = false
                handleNetworkStateForService()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)

        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)

        // LoadingView를 inflate 시키기
        setLoadingView()

        // ViewModel에서 postValue(LiveData) 를 해주는 순간 UI가 업데이트 되기 때문에 따로 observe를 할 필요가 없게 함
        viewDataBinding.lifecycleOwner = this

        initStartView()
        initDataBinding()
        initAfterBinding()
        setScreenAlwaysOn()
    }


    /**
     * 레이아웃을 띄운 직후 호출.
     * 뷰나 액티비티의 속성 등을 초기화.
     * ex) 리사이클러뷰, 툴바, 드로어뷰..
     */
    abstract fun initStartView()

    /**
     * 두번째로 호출.
     * 데이터 바인딩 및 rxjava 설정.
     * ex) rxjava observe, databinding observe..
     */
    abstract fun initDataBinding()

    /**
     * 바인딩 이후에 할 일을 여기에 구현.
     * 그 외에 설정할 것이 있으면 이곳에서 설정.
     * 클릭 리스너도 이곳에서 설정.
     */
    abstract fun initAfterBinding()

    /**
     * for service
     * network 상태 변화시 Activity에 팝업 띄우기
     * isAlsoConnectedCheck -> 연결된 상태일때 Toast 받을건지 ( ConnectivityManager.NetworkCallback 으로 받는거에서만 )
     */
    private fun handleNetworkStateForService(){
        if(!networkState){
            // network 끊긴 경우 로직
            Toast.makeText(this, this.resources.getString(com.example.winebarcodereader.R.string.network_disconnected),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, this.resources.getString(com.example.winebarcodereader.R.string.network_connected),Toast.LENGTH_LONG).show()
        }
    }

    /**
     * current Network State not for service
     * 1. Scan 화면에서 WinePrice API(1010) 호출시에 통신 실패시 네트워크 체크 function
     */
    fun handleNetworkState(){
        networkState = NetworkCheckUtil.isNetworkConnected(this)

        if(!networkState){
            // network 끊긴 경우 로직
            Toast.makeText(this, this.resources.getString(com.example.winebarcodereader.R.string.network_disconnected),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, this.resources.getString(com.example.winebarcodereader.R.string.network_connected),Toast.LENGTH_LONG).show()
        }
    }

    protected fun showLoading(){
        loadingView.visibility = View.VISIBLE
        animation.start()
    }

    protected fun hideLoading(){
        loadingView.visibility = View.GONE
        animation.stop()
    }

    /**
     * 화면이 절전모드가 되지 않도록 셋팅
     */
    private fun setScreenAlwaysOn(){
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BaseKotlinActivity", "BaseKotlinActivity onDestroy")


    }

    override fun onResume() {
        super.onResume()

        // Network State 체크 등록
        checkNetworkConnectionUtil.networkStateRegister()

        viewModel.registerSharedPreferenceChangedListner()
    }

    override fun onPause() {
        super.onPause()

        viewModel.unregisterSharedPreferenceChangedListner()

        // Network State 체크 해제
        checkNetworkConnectionUtil.networkStateUnregister()
    }

    override fun onBackPressed() {

    }

    /**
     * Common LoadingView 준비로직
     */
    private fun setLoadingView(){
        loadingView = layoutInflater.inflate(com.example.winebarcodereader.R.layout.common_loading_layout,parentView,false)
        animationView = loadingView.findViewById(com.example.winebarcodereader.R.id.iv_loading)
        animation = animationView.background as AnimationDrawable
        parentView.addView(loadingView)
    }
}