package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.help.presentation.viewmodel.HelpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object HelpModule {
    private val presentationModule = module {
        viewModel { HelpViewModel() }
    }

    fun load() = loadKoinModules(listOf(presentationModule))
}