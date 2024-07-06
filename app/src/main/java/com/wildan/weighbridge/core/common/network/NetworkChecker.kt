package com.wildan.weighbridge.core.common.network


/**
 * Created by Wildan Nafian on 24/08/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
fun interface NetworkChecker {

    suspend fun isConnected(): Boolean
}
