package br.com.levez.challenge.delivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.levez.challenge.delivery.databinding.ActivityDeliveryChallengeBinding

class DeliveryChallengeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryChallengeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@DeliveryChallengeActivity,
            R.layout.activity_delivery_challenge
        )
    }
}
