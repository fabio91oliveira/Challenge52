package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.persistence.source.Database
import oliveira.fabio.challenge52.persistence.source.provideBuilder
import org.koin.dsl.module

val daoModule = module {
    factory { provideBuilder(get()) }
    factory { get<Database>().goalDao() }
    factory { get<Database>().weekDao() }
}