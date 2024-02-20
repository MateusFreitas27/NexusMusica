package br.com.nexus.nexusmusica.util

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar


class CalendarioUtil {
    private val calendario = Calendar.getInstance()

    val tempoDecorrido: Long
        get() = (calendario[Calendar.HOUR_OF_DAY] * 60 + calendario[Calendar.MINUTE]) *
                MS_POR_MINUTO + calendario[Calendar.SECOND] * 1000 + calendario[Calendar.MILLISECOND]

    val semanaDecorrido: Long
        get(){
            var tempo = tempoDecorrido
            val diasPassados = calendario[Calendar.DAY_OF_WEEK] - 1 - calendario.firstDayOfWeek
            if (diasPassados > 0){
                tempo += diasPassados * MS_POR_DIA
            }
            return diasPassados.toLong()
        }

    val mesDecorrido: Long
        get() = tempoDecorrido + (calendario[Calendar.DAY_OF_WEEK] - 1) * MS_POR_DIA

    val anoDecorrido: Long
        get() {
            var decorrido = mesDecorrido
            var mes = calendario[Calendar.MONTH] - 1
            while (mes > Calendar.JANUARY){
                decorrido += diasEmMes(mes) * MS_POR_DIA
                mes--
            }
            return decorrido
        }

    fun tempoDecorridoMes(numMes: Int): Long{
        var decorrido = mesDecorrido

        var mes = calendario[Calendar.MONTH]
        var ano = calendario[Calendar.YEAR]
        for(i in 0 until numMes){
            mes--
            if (mes < Calendar.JANUARY){
                mes = Calendar.DECEMBER
                ano--
            }
            decorrido += diasEmMes(mes) * MS_POR_DIA
        }
        return decorrido
    }

    fun diasEmMes(mes: Int): Int{
        val mesCal: Calendar = GregorianCalendar(calendario[Calendar.YEAR], mes, 1)
        return mesCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun tempoDias(numDias: Int): Long{
        var decorrido = tempoDecorrido
        decorrido += numDias * MS_POR_DIA
        return decorrido
    }

    companion object{
        private const val MS_POR_MINUTO = (60 * 1000).toLong()
        private const val MS_POR_DIA = 24 * 60 * MS_POR_MINUTO
    }

}

