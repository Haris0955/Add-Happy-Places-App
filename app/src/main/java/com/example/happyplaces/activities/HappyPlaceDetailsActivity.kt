package com.example.happyplaces.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.databinding.ActivityHappyPlaceDetailsBinding
import com.example.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailsActivity : AppCompatActivity() {
    private var binding: ActivityHappyPlaceDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var happyPlaceDetailModel : HappyPlaceModel? =null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel?
        }
        if (happyPlaceDetailModel!=null){
            setSupportActionBar(binding?.TBHappyPlaceDetails) // Use the toolbar to set the action bar.
            if (supportActionBar != null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true) // This is to use the home back button.
            }
            // Setting the click event to the back button
            binding?.TBHappyPlaceDetails?.setNavigationOnClickListener {
                onBackPressed()
            }
            binding?.ivPlaceImage?.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            binding?.tvDescription?.text= happyPlaceDetailModel.description
            binding?.tvLocation?.text= happyPlaceDetailModel.location

            binding?.btnViewOnMap?.setOnClickListener {
                val intent = Intent(this@HappyPlaceDetailsActivity, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
                startActivity(intent)
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding!= null){
            binding= null
        }
    }
}