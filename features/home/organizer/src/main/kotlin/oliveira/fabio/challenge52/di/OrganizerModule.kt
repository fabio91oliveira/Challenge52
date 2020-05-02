package oliveira.fabio.challenge52.di

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object OrganizerModule {
    private val presentationModule = module {

    }

    fun load() = loadKoinModules(listOf(presentationModule))
}