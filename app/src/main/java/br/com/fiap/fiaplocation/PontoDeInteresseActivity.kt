package br.com.fiap.fiaplocation

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.fiaplocation.model.Localidade
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class PontoDeInteresseActivity : AppCompatActivity(), OnMapReadyCallback {

    val localidades = mutableListOf<Localidade>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto_de_interesse)

        val map = supportFragmentManager.findFragmentById(R.id.map3) as SupportMapFragment
        map.getMapAsync(this)

        val localidade1 = Localidade(
            "Museu Catavento",
            "Av Mercúrio s/n\n Parque Dom Pedro II\n - São Paulo - SP",
            -23.544225824702806,
            -46.62728782963298
        )
        localidades.add(localidade1)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(localidades[0].latitude, localidades[0].longitude))
                .title(localidades[0].nome)
                .snippet(localidades[0].logradouro)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(localidades[0].latitude, localidades[0].longitude),12F))
        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter{
            override fun getInfoWindow(p0: Marker): View? {
                return null
            }
            override fun getInfoContents(marker: Marker): View? {
                val info = LinearLayout(applicationContext)
                info.orientation = LinearLayout.VERTICAL

                //titulo
                val title = TextView(applicationContext)
                title.setTextColor(Color.BLUE)
                title.gravity = Gravity.LEFT
                title.setTypeface(null, Typeface.BOLD)
                title.text = marker.title

                //complemento
                val snippet = TextView(applicationContext)
                snippet.setTextColor(Color.RED)
                snippet.text = marker.snippet

                info.addView(title)
                info.addView(snippet)
                return info
            }
        })
    }
}