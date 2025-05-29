package com.uriolus.recipes.rules // IMPORTANT: User needs to move this file to app/src/test/java/com/uriolus/recipes/rules/

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * A JUnit 5 Extension that sets the Main dispatcher to a TestDispatcher for unit tests.
 * This allows coroutines launched on Dispatchers.Main to be executed synchronously in tests.
 *
 * Usage in a JUnit 5 test class:
 * ```kotlin
 * @JvmField
 * @RegisterExtension
 * val mainDispatcherRule = MainDispatcherRule()
 * ```
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher() // Using UnconfinedTestDispatcher for simplicity
) : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
