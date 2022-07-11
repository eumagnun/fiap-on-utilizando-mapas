package br.com.fiap.fiaplocation

import android.graphics.Color
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
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class PrimeiraRotaActivity : AppCompatActivity(), OnMapReadyCallback {

    val localidades = mutableListOf<Localidade>()
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primeira_rota)

        val map = supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
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

        localidades.add(localidade1)
        localidades.add(localidade2)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
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
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    localidades[1].latitude,
                    localidades[1].longitude
                ), 12F
            )
        )
        renderizarRota()
    }


    private fun renderizarRota() {
        CoroutineScope(Dispatchers.IO).launch {
                val resultado = getPolylines()
            withContext(Dispatchers.Main) {
                val lineOption = PolylineOptions()
                for (i in resultado.indices) {
                    lineOption.add(resultado[i])
                    lineOption.width(10f)
                    lineOption.color(Color.BLUE)
                }
                googleMap.addPolyline(lineOption)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            -23.594777910036253,
                            -46.68721000000001
                        ), 12.5F
                    )
                )
            }
        }
    }

    private fun getPolylines(): ArrayList<LatLng> {
        val data =
            requestLocationData(getUrlMaps(localidades[0], localidades[1]))
        val resultado = ArrayList<LatLng>()

        val responseObejct = Gson().fromJson(data, MapData::class.java)
        for (i in 0 until responseObejct.routes[0].legs[0].steps.size) {
            resultado.addAll(decodificarPolylines(responseObejct.routes[0].legs[0].steps[i].polyline.points))
        }
        return resultado
    }

    private fun getUrlMaps(localOrigem:Localidade, localDestino:Localidade): String {
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${localOrigem.latitude},${localOrigem.longitude}&" +
                "destination=${localDestino.latitude},${localDestino.longitude}&sensor=false&mode=driving&key=AIzaSyBm_MMvE5k84T2wF6CdC-_HRp2Axb8vO7w"
        return url
    }

    private fun decodificarPolylines(points: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = points.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = points[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = points[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    private fun requestLocationData(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val data = response.body!!.string()
        return data
    }
}