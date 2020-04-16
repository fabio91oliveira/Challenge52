package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.data.repository.GoalRepositoryImpl
import oliveira.fabio.challenge52.data.repository.GoalWithWeeksRepositoryImpl
import oliveira.fabio.challenge52.data.repository.ItemRepositoryImpl
import oliveira.fabio.challenge52.domain.mapper.GoalEntityMapper
import oliveira.fabio.challenge52.domain.mapper.GoalMapper
import oliveira.fabio.challenge52.domain.mapper.ItemEntityMapper
import oliveira.fabio.challenge52.domain.mapper.impl.GoalEntityMapperImpl
import oliveira.fabio.challenge52.domain.mapper.impl.GoalMapperImpl
import oliveira.fabio.challenge52.domain.mapper.impl.ItemEntityMapperImpl
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.domain.repository.ItemRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RepositoryModule {
    private val mapperModule = module {
        factory<GoalEntityMapper> { GoalEntityMapperImpl() }
        factory<ItemEntityMapper> { ItemEntityMapperImpl() }
        factory<GoalMapper> { GoalMapperImpl() }
    }
    private val repositoryModule = module {
        factory<GoalRepository> { GoalRepositoryImpl(get(), get()) }
        factory<ItemRepository> { ItemRepositoryImpl(get(), get()) }

        //
        factory<GoalWithWeeksRepository> {
            GoalWithWeeksRepositoryImpl(
                get(), get()
            )
        }
    }

    fun load() = loadKoinModules(
        listOf(
            mapperModule,
            repositoryModule
        )
    )
}