package oliveira.fabio.challenge52.persistence.di

import oliveira.fabio.challenge52.persistence.datasource.BalanceLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.BalanceWithTransactionsLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.ItemLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.TransactionLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.impl.BalanceLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.BalanceWithTransactionsLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.GoalLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.GoalWithWeeksLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.ItemLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.TransactionLocalDataSourceImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataSourceModule {
    private val dataSourceModule = module {
        factory<GoalLocalDataSource> { GoalLocalDataSourceImpl(get()) }
        factory<ItemLocalDataSource> { ItemLocalDataSourceImpl(get()) }
        factory<GoalWithWeeksLocalDataSource> { GoalWithWeeksLocalDataSourceImpl(get()) }
        factory<BalanceLocalDataSource> { BalanceLocalDataSourceImpl(get()) }
        factory<TransactionLocalDataSource> { TransactionLocalDataSourceImpl(get()) }
        factory<BalanceWithTransactionsLocalDataSource> {
            BalanceWithTransactionsLocalDataSourceImpl(
                get()
            )
        }
    }

    fun load() = loadKoinModules(dataSourceModule)
}