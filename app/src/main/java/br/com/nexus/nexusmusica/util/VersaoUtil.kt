package br.com.nexus.nexusmusica.util

import android.os.Build

object VersaoUtil{
    fun androidQ(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun androidS(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    fun androidT(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun androidR(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
}