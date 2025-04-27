package com.jvdev

import com.jvdev.com.database.queryUser
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.assertNotNull

class UserDBClientTest {

    @Test
    fun testQuery() = runTest {
        val username = "zawarudo"
        val queryResult = queryUser(username)
        assertNotNull(queryResult)
    }
}