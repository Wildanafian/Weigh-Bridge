package com.wildan.weighbridge.ui.helper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.Executors


/**
 * Created by Wildan Nafian on 15/08/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher() {

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(singleThreadExecutor.asCoroutineDispatcher())
    }

    override fun finished(description: Description) {
        super.finished(description)
        singleThreadExecutor.shutdownNow()
        Dispatchers.resetMain()
    }
}