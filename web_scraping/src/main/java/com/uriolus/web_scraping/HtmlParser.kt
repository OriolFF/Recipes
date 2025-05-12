package com.uriolus.web_scraping

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class HtmlParser(private val url: String) {

    private var document: Document? = null

    init {
        try {
            document = Jsoup.connect(url).get()
        } catch (e: IOException) {
            // Consider more specific error handling or logging
            println("Error connecting to URL '$url': ${e.message}")
            // Optionally rethrow or handle as a failed state
        }
    }

    /**
     * Returns the Jsoup Document object.
     * Can be null if the initial connection failed.
     */
    fun getDom(): Document? {
        return document
    }

    /**
     * Extracts all text content from the parsed HTML body.
     * Returns an empty string if the document is null or body is not present.
     */
    fun getAllText(): String {
        return document?.body()?.text() ?: ""
    }

    /**
     * Extracts all image URLs (src attributes) from img tags.
     * Returns an empty list if the document is null or no images are found.
     */
    fun getImageUrls(): List<String> {
        val images: Elements? = document?.select("img")
        return images?.mapNotNull { it.attr("abs:src") } ?: emptyList()
    }

    /**
     * Extracts text from elements matching a CSS selector.
     * @param cssQuery The CSS selector (e.g., "h1", ".classname", "#idname p").
     * @return A list of text content from matching elements.
     */
    fun getTextBySelector(cssQuery: String): List<String> {
        val elements: Elements? = document?.select(cssQuery)
        return elements?.mapNotNull { it.text() } ?: emptyList()
    }

    /**
     * Extracts attribute values from elements matching a CSS selector.
     * @param cssQuery The CSS selector.
     * @param attributeKey The name of the attribute to extract (e.g., "href", "title").
     * @return A list of attribute values from matching elements.
     */
    fun getAttributesBySelector(cssQuery: String, attributeKey: String): List<String> {
        val elements: Elements? = document?.select(cssQuery)
        return elements?.mapNotNull { it.attr(attributeKey) } ?: emptyList()
    }
}
