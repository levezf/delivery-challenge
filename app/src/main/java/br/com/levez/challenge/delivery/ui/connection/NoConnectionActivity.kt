package br.com.levez.challenge.delivery.ui.connection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.network.manager.ConnectionState
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoConnectionActivity : AppCompatActivity() {

    private val viewModel: NoConnectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        viewModel.internetConnectionState.observe(this) { connectionState ->
            if (connectionState == ConnectionState.CONNECTED) {
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing
    }
}
