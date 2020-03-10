package com.luanbarbosagomes.hmr.dagger

import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.data.repository.AuthRepository
import com.luanbarbosagomes.hmr.data.repository.LocalExpressionRepository
import com.luanbarbosagomes.hmr.data.repository.RemoteExpressionRepository
import com.luanbarbosagomes.hmr.feature.add.NewExpressionViewModel
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.list.ExpressionsViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.main.MainViewModel
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
    fun inject(vm: MainViewModel)
}

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase = App.database

    @Provides
    @Singleton
    fun provideLocalExpressionRepository(): LocalExpressionRepository = LocalExpressionRepository(App.database)

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepository(App.firebaseAuth)
}
