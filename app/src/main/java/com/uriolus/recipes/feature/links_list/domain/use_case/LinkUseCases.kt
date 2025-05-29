package com.uriolus.recipes.feature.links_list.domain.use_case

import javax.inject.Inject

data class LinkUseCases @Inject constructor(
    val getAllLinks: GetAllLinksUseCase,
    val addLink: AddLinkUseCase,
    val deleteLink: DeleteLinkUseCase
)
