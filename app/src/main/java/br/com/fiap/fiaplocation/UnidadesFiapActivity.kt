package br.com.fiap.fiaplocation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.fiaplocation.model.Localidade
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class UnidadesFiapActivity : AppCompatActivity(), OnMapReadyCallback {

    val localidades = mutableListOf<Localidade>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidades_fiap)

        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        val localidade1 = Localidade(
            "FIAP Vila Olimpia",
            "Rua Fidêncio Ramos, 308 - São Paulo - SP",
            -23.594777910036253,
            -46.68721000000001
        )

        val localidade2 = Localidade(
            "FIAP Aclimação",
            "Av Lins de Vasconcelos, 1222 - São Paulo - SP",
            -23.57379531438815,
            -46.62348388840317
        )
        val localidade3 = Localidade(
            "FIAP Paulista",
            "Av Paulista, 1106 - Sãpo Paulo - SP",
            -23.56387866381928,
            -46.65234735771986
        )

        localidades.add(localidade1)
        localidades.add(localidade2)
        localidades.add(localidade3)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(localidades[0].latitude, localidades[0].longitude))
                .title(localidades[0].nome)
                .snippet(localidades[0].logradouro)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(localidades[1].latitude, localidades[1].longitude))
                .title(localidades[1].nome)
                .snippet(localidades[1].logradouro)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(localidades[2].latitude, localidades[2].longitude))
                .title(localidades[2].nome)
                .snippet(localidades[2].logradouro)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(localidades[1].latitude, localidades[1].longitude),12F))
    }
}