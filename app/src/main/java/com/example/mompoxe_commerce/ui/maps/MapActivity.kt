package com.example.mompoxe_commerce.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.mompoxe_commerce.R

class MapActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContentView(R.layout.activity_map)

        map = findViewById(R.id.map)
        val btnGuardarDireccion: Button = findViewById(R.id.btnGuardarDireccion)

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        val startPoint = GeoPoint(-25.2637, -57.5759) // Asunción
        mapController.setZoom(15.0)
        mapController.setCenter(startPoint)

        val marker = Marker(map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Mi ubicación"
        map.overlays.add(marker)

        // Mover marcador con toque (opcional)
        map.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val projection = map.projection
                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                marker.position = geoPoint
                map.invalidate()
            }
            false
        }

        btnGuardarDireccion.setOnClickListener {
            val geoPoint = marker.position
            val lat = geoPoint.latitude
            val lon = geoPoint.longitude

            val intent = Intent().apply {
                putExtra("lat", lat)
                putExtra("lon", lon)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}
