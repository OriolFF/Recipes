package com.uriolus.recipes.feature.links_list.presentation.state

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink

data class LinksListState(
    val links: List<RecipeLink> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val newLinkUrl: String = "",
    val newLinkTitle: String = "",
    val isAddingLink: Boolean = false
)
