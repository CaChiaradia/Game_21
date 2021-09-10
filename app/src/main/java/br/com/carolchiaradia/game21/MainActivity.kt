package br.com.carolchiaradia.game21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.carolchiaradia.domain.MyClass
import br.com.carolchiaradia.domain.model.Carta
import br.com.carolchiaradia.mobcomponents.customtoast.CustomToast

import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var btRecomecar: Button
    private lateinit var btProximaCarta: Button
    private lateinit var tvPontuacao: TextView
    private lateinit var ivCarta: ImageView

    private var cartas: MutableList<Carta> = mutableListOf()
    private val gerador = Random()

    private lateinit var containerPropaganda: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpView()
        setListeners()
        iniciarPartida()
        showBanner()

    }

    private fun showBanner() {
        containerPropaganda.visibility = if (isGratuito())
            View.VISIBLE else View.GONE
    }

    private fun isGratuito(): Boolean {
        return packageName == "br.com.carolchiaradia.game21.gratuito"
    }

    private fun setUpView() {
        btRecomecar = findViewById(R.id.btRecomecar)
        btProximaCarta = findViewById(R.id.btProximaCarta)
        tvPontuacao = findViewById(R.id.tvPontuacao)
        ivCarta = findViewById(R.id.ivCarta)
        containerPropaganda = findViewById(R.id.containerPropaganda)
    }

    private fun setListeners() {
        btProximaCarta.setOnClickListener {
            realizarJogada()
        }

        btRecomecar.setOnClickListener {
            iniciarPartida()
        }
    }


    private fun iniciarPartida() {
        tvPontuacao.text = "0"
        cartas = getBaralho()
        ivCarta.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.logo
            )
        )
    }


    private fun getBaralho(): MutableList<Carta> {
        return mutableListOf<Carta>(
            Carta(R.drawable.as_de_espada, 1),
            Carta(R.drawable.dois_de_espada, 2),
            Carta(R.drawable.tres_de_espada, 3),
            Carta(R.drawable.quatro_de_espada, 4),
            Carta(R.drawable.cinco_de_espada, 5),
            Carta(R.drawable.seis_de_espada, 6),
            Carta(R.drawable.sete_de_espada, 7),
            Carta(R.drawable.oito_de_espada, 8),
            Carta(R.drawable.nove_de_espada, 9),
            Carta(R.drawable.dez_de_espada, 10),
            Carta(R.drawable.valete_de_espada, 10),
            Carta(R.drawable.dama_de_espada, 10),
            Carta(R.drawable.rei_de_espada, 10)
        )
    }

    private fun realizarJogada() {
        val posicaoCartaSelecionada = gerador.nextInt(cartas.size)
        val cartaSelecionada = cartas.get(posicaoCartaSelecionada)
        val pontuacaoAtualizada = tvPontuacao.text.toString().toInt() +
                cartaSelecionada.pontuacao
        tvPontuacao.text = pontuacaoAtualizada.toString()
        exibeMensagem(pontuacaoAtualizada)
        if (pontuacaoAtualizada > 21) {
            iniciarPartida()
        } else {
            cartas.removeAt(posicaoCartaSelecionada)
            ivCarta.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    cartaSelecionada.resourceId
                )
            )
        }
    }

    private fun controlarBotoes() {
        val time = Toast.LENGTH_LONG
        habilitarBotoes(false)
        Handler().postDelayed({
            habilitarBotoes(true)
        }, 2500)
    }

    private fun habilitarBotoes(habilitar: Boolean) {
        btProximaCarta.isEnabled = habilitar
        btRecomecar.isEnabled = habilitar
    }

    private fun exibeMensagem (pontuacao: Int) {
        when {
            pontuacao == 21 -> {
                CustomToast .success( this, "Você atingiu a melhor pontuação. Hora de parar :)" )
            }
            pontuacao > 21 -> {
                CustomToast .error( this, "Você perdeu fez $pontuacao e perdeu" )
            }
            pontuacao > 11 -> {
                CustomToast .warning(
                    this,
                    "Cuidado, dependendo da carta que comprar você poderá perder"
                )
            }
            else -> {
                CustomToast .default( this, "Você ainda pode jogar com segurança" )
            }
        }
        controlarBotoes()
    }
}