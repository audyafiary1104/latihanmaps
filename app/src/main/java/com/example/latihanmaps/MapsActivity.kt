package com.example.latihanmaps

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.support.v4.view.ViewCompat.animate
import android.R.attr.translationY
import kotlinx.android.synthetic.main.activity_maps.*
import android.support.v4.view.ViewCompat.animate
import android.R.attr.translationY




class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private  var isFABOpen = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_155))
        fab4.animate().translationY(-resources.getDimension(R.dimen.standard_180))

    }

    private fun closeFABMenu() {
        isFABOpen = false
        fab1.animate().translationY(0f)
        fab2.animate().translationY(0f)
        fab3.animate().translationY(0f)
        fab4.animate().translationY(0f)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setOnMarkerClickListener(this)
        setUpMap()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentPos = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentPos)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 18.0f))

            }
        }
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        fab.setOnClickListener {
            if(!isFABOpen){
                showFABMenu();
            }else{
                closeFABMenu();
            }

        }
        fab1.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        }
        fab2.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        }
        fab3.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        }
        fab4.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }

    }

    private fun placeMarkerOnMap(loc : LatLng){
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources,R.mipmap.ic_narto)
        ))
        mMap.addMarker(markerOptions)
    }
}
