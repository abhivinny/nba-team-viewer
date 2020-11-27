package di

import android.app.Application
import androidx.room.Room
import com.score.nba.team.viewer.BuildConfig
import com.score.nba.team.viewer.core.data.repository.TeamRepositoryImpl
import com.score.nba.team.viewer.core.domain.repository.TeamRepository
import com.score.nba.team.viewer.core.usecases.PlayerUseCases
import com.score.nba.team.viewer.core.usecases.TeamsUseCases
import com.score.nba.team.viewer.framework.TeamRemoteService
import com.score.nba.team.viewer.framework.database.TeamsDatabase
import com.score.nba.team.viewer.framework.database.dao.PlayerDao
import com.score.nba.team.viewer.framework.database.dao.TeamsDao
import com.score.nba.team.viewer.presentation.viewmodel.TeamInfoViewModel
import com.score.nba.team.viewer.presentation.viewmodel.TeamsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val coreModule = module {
    factory { TeamsUseCases(get()) }
    factory { PlayerUseCases(get()) }
    factory<TeamRepository> { TeamRepositoryImpl(get(), get(), get()) }
}

val frameworkModule = module {
    factory { provideOkHttpClient() }
    factory { provideTeamApi(get()) }
    single { provideRetrofit(get()) }
}

val presentationModule: Module = module {
    viewModel { TeamsViewModel(get()) }
    viewModel { TeamInfoViewModel(get()) }
}

val databaseModule = module {
    fun provideDatabase(application: Application): TeamsDatabase {
        return Room.databaseBuilder(application, TeamsDatabase::class.java, "NBA Teams")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideTeamsDao(database: TeamsDatabase): TeamsDao {
        return  database.teamDao()
    }

    fun providePlayerDao(database: TeamsDatabase): PlayerDao {
        return  database.playerDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideTeamsDao(get()) }
    single { providePlayerDao(get()) }
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideTeamApi(retrofit: Retrofit): TeamRemoteService = retrofit.create(TeamRemoteService::class.java)
