package com.luanbarbosagomes.hmr.dagger

import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.add.NewExpressionViewModel
import com.luanbarbosagomes.hmr.feature.details.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.list.ExpressionsViewModel
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
}

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase = App.database

    @Provides
    @Singleton
    fun provideExpressionRepository(): ExpressionRepository = ExpressionRepository(App.database)
}
