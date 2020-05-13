package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.data.mapper.BalanceEntityMapper
import oliveira.fabio.challenge52.data.mapper.BalanceMapper
import oliveira.fabio.challenge52.data.mapper.GoalEntityMapper
import oliveira.fabio.challenge52.data.mapper.GoalMapper
import oliveira.fabio.challenge52.data.mapper.ItemEntityMapper
import oliveira.fabio.challenge52.data.mapper.TransactionEntityMapper
import oliveira.fabio.challenge52.data.mapper.impl.BalanceEntityMapperImpl
import oliveira.fabio.challenge52.data.mapper.impl.BalanceMapperImpl
import oliveira.fabio.challenge52.data.mapper.impl.GoalEntityMapperImpl
import oliveira.fabio.challenge52.data.mapper.impl.GoalMapperImpl
import oliveira.fabio.challenge52.data.mapper.impl.ItemEntityMapperImpl
import oliveira.fabio.challenge52.data.mapper.impl.TransactionEntityMapperImpl
import oliveira.fabio.challenge52.data.repository.BalanceRepositoryImpl
import oliveira.fabio.challenge52.data.repository.BalanceWithTransactionsRepositoryImpl
import oliveira.fabio.challenge52.data.repository.GoalRepositoryImpl
import oliveira.fabio.challenge52.data.repository.GoalWithWeeksRepositoryImpl
import oliveira.fabio.challenge52.data.repository.ItemRepositoryImpl
import oliveira.fabio.challenge52.data.repository.TransactionRepositoryImpl
import oliveira.fabio.challenge52.domain.repository.BalanceRepository
import oliveira.fabio.challenge52.domain.repository.BalanceWithTransactionsRepository
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import oliveira.fabio.challenge52.domain.repository.TransactionRepository
import oliveira.fabio.challenge52.domain.usecase.GetCurrentLocaleUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.GetCurrentLocaleUseCaseImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RepositoryModule {
    private val domainModule = module {
        factory<GetCurrentLocaleUseCase> { GetCurrentLocaleUseCaseImpl() }
    }
    private val mapperModule = module {
        factory<GoalEntityMapper> { GoalEntityMapperImpl() }
        factory<ItemEntityMapper> { ItemEntityMapperImpl() }
        factory<GoalMapper> { GoalMapperImpl() }
        factory<BalanceMapper> { BalanceMapperImpl() }
        factory<BalanceEntityMapper> { BalanceEntityMapperImpl() }
        factory<TransactionEntityMapper> { TransactionEntityMapperImpl() }
    }
    private val repositoryModule = module {
        factory<GoalRepository> { GoalRepositoryImpl(get(), get()) }
        factory<ItemRepository> { ItemRepositoryImpl(get(), get()) }
        factory<GoalWithWeeksRepository> {
            GoalWithWeeksRepositoryImpl(
                get(), get()
            )
        }
        factory<BalanceRepository> { BalanceRepositoryImpl(get(), get()) }
        factory<BalanceWithTransactionsRepository> {
            BalanceWithTransactionsRepositoryImpl(
                get(),
                get()
            )
        }
        factory<TransactionRepository> { TransactionRepositoryImpl(get(), get()) }
    }

    fun load() = loadKoinModules(
        listOf(
            domainModule,
            mapperModule,
            repositoryModule
        )
    )
}