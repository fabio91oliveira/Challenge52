package oliveira.fabio.challenge52.help.di

import oliveira.fabio.challenge52.help.presentation.viewmodel.HelpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val helpListModule = module {
    viewModel { HelpViewModel() }
}

private val loadHelpModule by lazy { loadKoinModules(helpListModule) }

internal fun injectHelpDependencies() = loadHelpModule