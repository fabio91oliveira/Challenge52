package oliveira.fabio.challenge52.help.di

import oliveira.fabio.challenge52.help.presentation.viewmodel.HelpViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private val helpListModule = module {
    viewModel { HelpViewModel() }
}

private val loadHelpModule by lazy { loadKoinModules(helpListModule) }

internal fun injectHelpDependencies() = loadHelpModule