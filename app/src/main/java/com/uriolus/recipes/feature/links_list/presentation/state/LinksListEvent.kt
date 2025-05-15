package com.uriolus.recipes.feature.links_list.presentation.state

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink

sealed class LinksListEvent {
    data object LoadLinks : LinksListEvent()
    data class DeleteLink(val link: RecipeLink) : LinksListEvent()
    data class OnUrlChange(val url: String) : LinksListEvent()
    data class OnTitleChange(val title: String) : LinksListEvent()
    data object OnAddLink : LinksListEvent()
    data object DismissError : LinksListEvent()
    data object AnalyzeWebsite : LinksListEvent()
    data class SelectThumbnail(val url: String) : LinksListEvent()
    data object CloseImageSelectionDialog : LinksListEvent()
}
