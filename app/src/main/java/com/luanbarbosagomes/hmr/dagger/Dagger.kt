package com.luanbarbosagomes.hmr.dagger

import android.content.Context
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.data.repository.*
import com.luanbarbosagomes.hmr.feature.add.NewExpressionViewModel
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.init.InitViewModel
import com.luanbarbosagomes.hmr.feature.list.ExpressionsViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import com.luanbarbosagomes.hmr.feature.preference.StorageOption
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface MainComponent {

    fun inject(vm: ExpressionViewModel)
    fun inject(vm: NewExpressionViewModel)
    fun inject(vm: ExpressionsViewModel)
    fun inject(vm: AuthViewModel)
    fun inject(vm: PreferenceViewModel)
    fun inject(vm: InitViewModel)
}

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase = App.database

    @Provides
    fun provideLocalExpressionRepository() = LocalExpressionRepository(App.database)

    @Provides
    @Singleton
    fun provideAuthRepository() = AuthRepository(App.firebaseAuth)

    @Provides
    @Singleton
    fun providePreferenceRepository() =
        PreferenceRepository(App.appContext.getSharedPreferences("pref", Context.MODE_PRIVATE))

    @Provides
    fun provideExpressionRepository(): BaseExpressionRepository {
        return when (providePreferenceRepository().storageOption) {
            StorageOption.LOCAL -> provideLocalExpressionRepository()
            StorageOption.REMOTE -> RemoteExpressionRepository()
            null -> throw IllegalStateException("Storage option not chosen!")
        }
    }

}
