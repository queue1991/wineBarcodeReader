package com.example.winebarcodereader.di

import com.example.winebarcodereader.model.DataModel
import com.example.winebarcodereader.model.DataModelImpl
import com.example.winebarcodereader.model.common.APICommonValue.REAL_BASE_URL
import com.example.winebarcodereader.model.service.RetrofitService
import com.example.winebarcodereader.viewmodel.advertisement.AdvertisementRepository
import com.example.winebarcodereader.viewmodel.advertisement.AdvertisementViewModel
import com.example.winebarcodereader.viewmodel.intro.IntroRepository
import com.example.winebarcodereader.viewmodel.intro.IntroViewModel
import com.example.winebarcodereader.viewmodel.scan.ScanningRepository
import com.example.winebarcodereader.viewmodel.scan.ScanningViewModel
import com.example.winebarcodereader.viewmodel.wineinfo.WineInfoResultRepository
import com.example.winebarcodereader.viewmodel.wineinfo.WineInfoResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * single : 앱이 실행되는 동안 계속 유지되는 싱글톤 객체를 생성합니다.
 * factory : 요청할 때마다 매번 새로운 객체를 생성합니다.
 * get() : 컴포넌트 내에서 알맞은 의존성을 주입 받습니다.
 */


var retrofitPart = module {
    single<RetrofitService> {
        Retrofit.Builder().baseUrl(REAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}

var repositoryPart = module{
    single{
        IntroRepository(get(),androidContext())
    }

    single{
        ScanningRepository(get(),androidContext())
    }
    single{
        WineInfoResultRepository(get(),androidContext())
    }

    single{
        AdvertisementRepository(get(),androidContext())
    }
}

var modelPart = module {
    factory<DataModel> {
        DataModelImpl(get())
    }
}

var viewModelPart = module {
    viewModel {
        IntroViewModel(get())
    }

    viewModel {
        ScanningViewModel(get())
    }

    viewModel {
        WineInfoResultViewModel(get())
    }

    viewModel {
        AdvertisementViewModel(get())
    }
}


var myDiModule = listOf(retrofitPart, modelPart, viewModelPart,repositoryPart)