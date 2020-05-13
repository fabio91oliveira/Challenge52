package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.features.HomeNavigation
import oliveira.fabio.challenge52.main.presentation.navigation.HomeNavigationImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object MainModule {
    private val presentationModule = module {
        factory<HomeNavigation> { HomeNavigationImpl() }
    }

    private fun loadMain() = loadKoinModules(listOf(presentationModule))

    fun load() {
        loadMain()
        GoalsListsModule.load()
        OrganizerModule.load()
        HelpModule.load()
    }
}