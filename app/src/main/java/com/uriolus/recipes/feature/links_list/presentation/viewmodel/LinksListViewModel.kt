package com.uriolus.recipes.feature.links_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.use_case.LinkUseCases
import com.uriolus.recipes.feature.links_list.presentation.state.LinksListEvent
import com.uriolus.recipes.feature.links_list.presentation.state.LinksListState
import com.uriolus.web_scraping.HtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LinksListViewModel @Inject constructor(
    private val linkUseCases: LinkUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(LinksListState())
    val state: StateFlow<LinksListState> = _state.asStateFlow()

    init {
        loadLinks()
    }

    fun onEvent(event: LinksListEvent) {
        when (event) {
            is LinksListEvent.LoadLinks -> loadLinks()
            is LinksListEvent.DeleteLink -> deleteLink(event.link)
            is LinksListEvent.OnUrlChange -> {
                _state.update { it.copy(newLinkUrl = event.url) }
            }
            is LinksListEvent.OnTitleChange -> {
                _state.update { it.copy(newLinkTitle = event.title) }
            }
            is LinksListEvent.OnAddLink -> addLink()
            is LinksListEvent.DismissError -> {
                _state.update { it.copy(error = null) }
            }
            is LinksListEvent.AnalyzeWebsite -> analyzeWebsite()
            is LinksListEvent.SelectThumbnail -> {
                _state.update { 
                    it.copy(
                        selectedThumbnailUrl = event.url,
                        showImageSelectionDialog = false
                    ) 
                }
            }
            is LinksListEvent.CloseImageSelectionDialog -> {
                _state.update { it.copy(showImageSelectionDialog = false) }
            }
        }
    }

    private fun loadLinks() {
        linkUseCases.getAllLinks()
            .onEach { links ->
                _state.update { it.copy(links = links, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun analyzeWebsite() {
        val url = state.value.newLinkUrl.trim()
        
        if (url.isBlank()) {
            _state.update { it.copy(error = "URL cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isAnalyzingWebsite = true) }
            try {
                val images = withContext(Dispatchers.IO) {
                    val parser = HtmlParser(url)
                    parser.getImageUrls()
                }
                _state.update { 
                    it.copy(
                        extractedImages = images,
                        isAnalyzingWebsite = false,
                        showImageSelectionDialog = true
                    ) 
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Failed to analyze website: ${e.message}",
                        isAnalyzingWebsite = false
                    )
                }
            }
        }
    }

    private fun addLink() {
        val url = state.value.newLinkUrl.trim()
        val title = state.value.newLinkTitle.trim()
        val thumbnailUrl = state.value.selectedThumbnailUrl

        if (url.isBlank()) {
            _state.update { it.copy(error = "URL cannot be empty") }
            return
        }

        if (title.isBlank()) {
            _state.update { it.copy(error = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isAddingLink = true) }
            try {
                val recipeLink = RecipeLink(
                    url = url,
                    title = title,
                    thumbnailUrl = thumbnailUrl
                )
                linkUseCases.addLink(recipeLink)
                _state.update {
                    it.copy(
                        newLinkUrl = "",
                        newLinkTitle = "",
                        selectedThumbnailUrl = "",
                        isAddingLink = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Failed to add link: ${e.message}",
                        isAddingLink = false
                    )
                }
            }
        }
    }

    private fun deleteLink(link: RecipeLink) {
        viewModelScope.launch {
            try {
                linkUseCases.deleteLink(link)
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to delete link: ${e.message}") }
            }
        }
    }
}
