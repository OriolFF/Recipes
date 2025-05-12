package com.uriolus.web_scraping

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val testUrl = "https://www.lecturas.com/recetas/bacalao-a-portuguesa-a-bras_3299.html"

    @Test
    fun `HtmlParser extracts images and text from specified URL`() {
        println("Attempting to parse URL: $testUrl")
        val parser = HtmlParser(testUrl)

        // Check if DOM was loaded, otherwise skip other tests as they will fail
        val dom = parser.getDom()
        assertNotNull(dom, "DOM should not be null. Check network connection or URL validity.")
        if (dom == null) {
            println("Failed to load DOM from $testUrl. Further assertions will be skipped.")
            return
        }

        println("DOM loaded successfully. Title: ${dom.title()}")

        val imageUrls = parser.getImageUrls()
        println("Extracted Image URLs (${"${imageUrls.size}"}):")
        imageUrls.forEach { println("  - $it") }
        assertTrue(imageUrls.isNotEmpty(), "Image URLs list should not be empty.")

        val allText = parser.getAllText()
        println("\nExtracted Text (first 500 chars):\n${allText.take(500)}...")
        assertTrue(allText.isNotBlank(), "Extracted text should not be blank.")

        // Example of a more specific assertion (optional, adjust as needed)
        // assertTrue(allText.contains("Bacalao"), "Text should contain 'Bacalao'")
    }

    @Test
    fun `HtmlParser handles invalid URL gracefully`() {
        val invalidUrl = "http://invalid-url-that-does-not-exist.com/" // Corrected htp to http
        println("Attempting to parse invalid URL: $invalidUrl")
        val parser = HtmlParser(invalidUrl)
        // In the current HtmlParser, a failed connection results in a null document
        // and a message printed to System.out. We can assert the document is null.
        assertNull(parser.getDom(), "DOM should be null for an invalid URL.")
        // Further assertions can be made if the HtmlParser is modified to throw specific exceptions
        // or return status codes.
        val text = parser.getAllText()
        assertEquals("", text, "Text should be empty for a failed connection.")
        val images = parser.getImageUrls()
        assertTrue(images.isEmpty(), "Image list should be empty for a failed connection.")
    }
}
