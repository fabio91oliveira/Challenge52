package oliveira.fabio.challenge52.di

import androidx.room.Room
import oliveira.fabio.challenge52.model.persistence.source.Database
import org.koin.dsl.module.module

val daoModule = module {
    single {
        Room.databaseBuilder(
            get(),
            Database::class.java,
            "52Challenge.db"
        )
            .build()
    }
    single { get<Database>().goalDao() }
    single { get<Database>().weekDao() }
}