package com.example.kalavidarabalaga

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kalavidarabalaga.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar setup
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)

        val artist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("artist", Artist::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("artist")
        }

        if (artist == null) {
            Toast.makeText(this, getString(R.string.error_artist_not_found), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set data
        binding.tvName.text = artist.name
        binding.tvArtType.text = artist.artType
        binding.tvLocation.text = getString(R.string.format_location, artist.location)
        binding.tvDistrict.text = getString(R.string.format_district, artist.district)
        binding.tvDescription.text = artist.description

        Glide.with(this)
            .load(artist.imageUrl)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(binding.ivArtist)

        // Call button
        binding.btnCall.setOnClickListener {
            if (artist.phone.isNotEmpty()) {
                try {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${artist.phone}")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, getString(R.string.error_dialer_failed), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
