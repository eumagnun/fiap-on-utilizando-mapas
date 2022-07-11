package br.com.fiap.fiaplocation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navegarUnidadesFiap(view: View){
        val intent = Intent(this,UnidadesFiapActivity::class.java)
        startActivity(intent)
    }

    fun navegarLocalizacao(view: View){
        val intent = Intent(this,LocalizacaoActivity::class.java)
        startActivity(intent)
    }

    fun navegarPrimeiraRota(view: View){
        val intent = Intent(this,PrimeiraRotaActivity::class.java)
        startActivity(intent)
    }

    fun navegarPontoInteresse(view: View){
        val intent = Intent(this,PontoDeInteresseActivity::class.java)
        startActivity(intent)
    }

}