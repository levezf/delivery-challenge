package br.com.levez.challenge.delivery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.databinding.ActivityDeliveryChallengeBinding
import br.com.levez.challenge.delivery.ui.common.extension.applyNavigationBackIcon
import br.com.levez.challenge.delivery.ui.common.extension.isAppBarVisible

class DeliveryChallengeActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityDeliveryChallengeBinding

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDeliveryChallengeBinding?>(
            this@DeliveryChallengeActivity,
            R.layout.activity_delivery_challenge
        ).apply {
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(
                navController,
                AppBarConfiguration(navController.graph)
            )
        }

        navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        binding.apply {
            toolbar.applyNavigationBackIcon()
            layoutAppBar.isVisible = arguments.isAppBarVisible()
        }
    }
}
