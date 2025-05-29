package com.uriolus.recipes

object NavRoutes {
    const val LOGIN = "login"
    const val RECIPE_LIST = "recipe_list"
    const val RECIPE_DETAIL = "recipe_detail/{recipeId}"
    const val LINKS_LIST = "links_list"

    fun recipeDetail(recipeId: String): String {
        return "recipe_detail/$recipeId"
    }
}
