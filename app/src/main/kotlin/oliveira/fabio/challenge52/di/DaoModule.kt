package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.persistence.source.Database
import oliveira.fabio.challenge52.persistence.source.provideBuilder
import org.koin.dsl.module.module

val daoModule = module {
    single { provideBuilder(get()) }
    single { get<Database>().goalDao() }
    single { get<Database>().weekDao() }
}