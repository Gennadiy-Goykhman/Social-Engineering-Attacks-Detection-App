package com.example.diplomaproject.receiver

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestReceiverCall {
    @Test
    fun test() {
        val mockContext = mockk<Context>()
        val mockIntent = mockk<Intent>()
        val testUri = Uri.parse("https://www.test.com")

        every { mockIntent.action } returns Intent.ACTION_VIEW
        every { mockIntent.data } returns testUri
    }
}