package com.wildan.weighbridge.ui.helper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * Created by Wildan Nafian on 16/08/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
@ExperimentalCoroutinesApi
open class BaseTestInstantTaskExecutorRule {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

}