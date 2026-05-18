package com.example.kalavidarabalaga

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kalavidarabalaga.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArtistAdapter
    private var fullArtistList: List<Artist> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupSearch()
        loadLocalArtists()
    }

    private fun loadLocalArtists() {
        // Now loading data from the local ArtistData.kt file instead of Firebase
        fullArtistList = ArtistData.getArtists()
        setupFilters()
        applyFilters()
    }

    private fun setupRecyclerView() {
        adapter = ArtistAdapter(emptyList()) { artist ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("artist", artist)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                applyFilters()
                return true
            }
        })
    }

    private fun setupFilters() {
        if (fullArtistList.isEmpty()) return

        val districts = mutableListOf("All Districts")
        districts.addAll(fullArtistList.map { it.district }.filter { it.isNotEmpty() }.distinct().sorted())
        
        val artTypes = mutableListOf("All Art Types")
        artTypes.addAll(fullArtistList.map { it.artType }.filter { it.isNotEmpty() }.distinct().sorted())

        binding.districtSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.artTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, artTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val filterListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.districtSpinner.onItemSelectedListener = filterListener
        binding.artTypeSpinner.onItemSelectedListener = filterListener
    }

    private fun applyFilters() {
        val query = binding.searchView.query.toString().lowercase().trim()
        val selectedDistrict = binding.districtSpinner.selectedItem?.toString() ?: "All Districts"
        val selectedArtType = binding.artTypeSpinner.selectedItem?.toString() ?: "All Art Types"

        val filteredList = fullArtistList.filter { artist ->
            val matchesSearch = artist.name.lowercase().contains(query) || 
                               artist.location.lowercase().contains(query) ||
                               artist.phone.contains(query)
            
            val matchesDistrict = selectedDistrict == "All Districts" || artist.district == selectedDistrict
            val matchesArtType = selectedArtType == "All Art Types" || artist.artType == selectedArtType
            
            matchesSearch && matchesDistrict && matchesArtType
        }
        
        adapter.updateList(filteredList)
    }
}
