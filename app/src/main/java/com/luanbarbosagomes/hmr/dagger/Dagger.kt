package com.luanbarbosagomes.hmr.dagger

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.data.repository.*
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.ViewModelFactory
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.expression.add.NewExpressionViewModel
import com.luanbarbosagomes.hmr.feature.expression.list.ExpressionsViewModel
import com.luanbarbosagomes.hmr.feature.init.FragSplash
import com.luanbarbosagomes.hmr.feature.init.InitViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.preference.BaseBottomSheetDialogFrag
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import com.luanbarbosagomes.hmr.feature.preference.StorageOption
import com.luanbarbosagomes.hmr.utils.ViewModelKey
import com.luanbarbosagomes.hmr.work.NotificationWorker
import com.luanbarbosagomes.hmr.work.QuizWorker
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface MainComponent {

    fun inject(baseMainFragment: BaseMainFragment)
    fun inject(notificationWorker: NotificationWorker)
    fun inject(quizWorker: QuizWorker)
    fun inject(bottomSheetDialogFrag: BaseBottomSheetDialogFrag)

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
    fun providesPhoneAuthProvider(): PhoneAuthProvider = PhoneAuthProvider.getInstance()


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

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(InitViewModel::class)
    abstract fun bindInitViewModel(initVM: InitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpressionViewModel::class)
    abstract fun bindExpressionViewModel(expressionVM: ExpressionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewExpressionViewModel::class)
    abstract fun bindNewExpressionViewModel(newExpressionVM: NewExpressionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpressionsViewModel::class)
    abstract fun bindExpressionsViewModel(expressionsVM: ExpressionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authVM: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreferenceViewModel::class)
    abstract fun bindPreferenceViewModel(preferenceVM: PreferenceViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
