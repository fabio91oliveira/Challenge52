package oliveira.fabio.challenge52.persistence.di

import oliveira.fabio.challenge52.persistence.config.Database
import oliveira.fabio.challenge52.persistence.config.provideBuilder
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DaoModule {
    private val daoModule = module {
        factory { provideBuilder(get()) }
        factory { get<Database>().goalDao() }
        factory { get<Database>().itemsDao() }
        factory { get<Database>().goalWithItemsDao() }
        factory { get<Database>().balanceDao() }
        factory { get<Database>().transactionDao() }
        factory { get<Database>().balanceWithTransactionsDao() }
    }

    fun load() = loadKoinModules(daoModule)
}