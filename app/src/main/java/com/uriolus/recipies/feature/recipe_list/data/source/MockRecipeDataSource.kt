package com.uriolus.recipies.feature.recipe_list.data.source

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockRecipeDataSource @Inject constructor() : RecipeDataSource {
    
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        emit(mockRecipes)
    }
    
    companion object {
        private val mockRecipes = listOf(
            Recipe(
                id = "1",
                title = "Spaghetti Carbonara",
                description = "A classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2FyYm9uYXJhfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "400g spaghetti",
                    "200g pancetta or guanciale, diced",
                    "4 large eggs",
                    "50g pecorino cheese, grated",
                    "50g parmesan, grated",
                    "Freshly ground black pepper",
                    "Salt"
                ),
                instructions = listOf(
                    "Cook the spaghetti in salted water according to package instructions.",
                    "While pasta cooks, fry the pancetta until crispy.",
                    "In a bowl, mix eggs, cheese, and pepper.",
                    "Drain pasta, reserving some cooking water.",
                    "Mix pasta with pancetta, then quickly stir in egg mixture off the heat.",
                    "Add a splash of pasta water to create a creamy sauce.",
                    "Serve immediately with extra cheese and pepper."
                )
            ),
            Recipe(
                id = "2",
                title = "Chicken Tikka Masala",
                description = "A flavorful Indian curry dish with marinated chicken in a creamy tomato sauce.",
                imageUrl = "https://images.unsplash.com/photo-1565557623262-b51c2513a641?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dGlra2ElMjBtYXNhbGF8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "800g chicken breast, cut into chunks",
                    "2 cups plain yogurt",
                    "3 tbsp lemon juice",
                    "4 tsp ground cumin",
                    "4 tsp paprika",
                    "2 tsp cinnamon",
                    "2 tsp black pepper",
                    "2 tbsp ginger, minced",
                    "6 cloves garlic, minced",
                    "2 tsp salt",
                    "2 tbsp butter",
                    "2 onions, diced",
                    "3 tbsp tomato paste",
                    "1 can tomato sauce",
                    "1 cup heavy cream",
                    "Fresh cilantro for garnish"
                ),
                instructions = listOf(
                    "Combine yogurt, lemon juice, and spices to make marinade.",
                    "Add chicken to marinade and refrigerate for at least 1 hour.",
                    "Preheat grill or oven and cook chicken until done.",
                    "In a large pan, melt butter and sauté onions until soft.",
                    "Add tomato paste, tomato sauce, and cream. Simmer for 10 minutes.",
                    "Add the cooked chicken and simmer for another 5 minutes.",
                    "Garnish with cilantro and serve with rice or naan."
                )
            ),
            Recipe(
                id = "3",
                title = "Avocado Toast",
                description = "A simple and nutritious breakfast with mashed avocado on toasted bread.",
                imageUrl = "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8YXZvY2FkbyUyMHRvYXN0fGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "2 slices of bread",
                    "1 ripe avocado",
                    "1 tbsp lemon juice",
                    "Salt and pepper to taste",
                    "Red pepper flakes (optional)",
                    "2 eggs (optional)"
                ),
                instructions = listOf(
                    "Toast the bread until golden and crisp.",
                    "Cut the avocado in half, remove the pit, and scoop the flesh into a bowl.",
                    "Add lemon juice, salt, and pepper, then mash with a fork.",
                    "Spread the mashed avocado on the toast.",
                    "Top with red pepper flakes if desired.",
                    "For extra protein, add a poached or fried egg on top."
                )
            ),
            Recipe(
                id = "4",
                title = "Greek Salad",
                description = "A refreshing Mediterranean salad with crisp vegetables, feta cheese, and olives.",
                imageUrl = "https://images.unsplash.com/photo-1551248429-40975aa4de74?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Z3JlZWslMjBzYWxhZHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "1 cucumber, diced",
                    "4 large tomatoes, diced",
                    "1 red onion, thinly sliced",
                    "1 green bell pepper, diced",
                    "200g feta cheese, cubed",
                    "100g kalamata olives",
                    "2 tbsp extra virgin olive oil",
                    "1 tbsp red wine vinegar",
                    "1 tsp dried oregano",
                    "Salt and pepper to taste"
                ),
                instructions = listOf(
                    "Combine cucumber, tomatoes, onion, and bell pepper in a large bowl.",
                    "Add olives and feta cheese.",
                    "In a small bowl, whisk together olive oil, vinegar, oregano, salt, and pepper.",
                    "Pour dressing over the salad and toss gently.",
                    "Let sit for 10 minutes before serving to allow flavors to blend."
                )
            ),
            Recipe(
                id = "5",
                title = "Chocolate Chip Cookies",
                description = "Classic homemade cookies with a perfect balance of chewy centers and crisp edges.",
                imageUrl = "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2hvY29sYXRlJTIwY2hpcCUyMGNvb2tpZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "225g unsalted butter, softened",
                    "200g brown sugar",
                    "100g granulated sugar",
                    "2 large eggs",
                    "1 tsp vanilla extract",
                    "280g all-purpose flour",
                    "1 tsp baking soda",
                    "1/2 tsp salt",
                    "300g chocolate chips",
                    "100g chopped nuts (optional)"
                ),
                instructions = listOf(
                    "Preheat oven to 375°F (190°C) and line baking sheets with parchment paper.",
                    "Cream together butter and both sugars until light and fluffy.",
                    "Beat in eggs one at a time, then stir in vanilla.",
                    "In a separate bowl, combine flour, baking soda, and salt.",
                    "Gradually add dry ingredients to the wet mixture.",
                    "Fold in chocolate chips and nuts if using.",
                    "Drop rounded tablespoons of dough onto baking sheets.",
                    "Bake for 9-11 minutes until golden brown.",
                    "Cool on baking sheets for 2 minutes, then transfer to wire racks."
                )
            ),
            Recipe(
                id = "6",
                title = "Vegetable Stir Fry",
                description = "A quick and healthy Asian-inspired dish with colorful vegetables and a savory sauce.",
                imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmVnZXRhYmxlJTIwc3RpciUyMGZyeXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "2 tbsp vegetable oil",
                    "2 cloves garlic, minced",
                    "1 tbsp ginger, grated",
                    "1 bell pepper, sliced",
                    "1 carrot, julienned",
                    "1 broccoli head, cut into florets",
                    "1 cup snap peas",
                    "1 cup mushrooms, sliced",
                    "2 tbsp soy sauce",
                    "1 tbsp oyster sauce",
                    "1 tsp sesame oil",
                    "1 tsp cornstarch mixed with 2 tbsp water",
                    "Sesame seeds for garnish",
                    "Cooked rice for serving"
                ),
                instructions = listOf(
                    "Heat oil in a wok or large frying pan over high heat.",
                    "Add garlic and ginger, stir for 30 seconds until fragrant.",
                    "Add vegetables, starting with the ones that take longer to cook (carrots, broccoli).",
                    "Stir-fry for 3-4 minutes, then add quicker-cooking vegetables.",
                    "Continue cooking until vegetables are crisp-tender.",
                    "Mix soy sauce, oyster sauce, and sesame oil, then add to the pan.",
                    "Add cornstarch slurry and stir until sauce thickens.",
                    "Garnish with sesame seeds and serve over rice."
                )
            ),
            Recipe(
                id = "7",
                title = "Homemade Pizza",
                description = "Create your own delicious pizza with a crispy crust and your favorite toppings.",
                imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGl6emF8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "For the dough:",
                    "500g all-purpose flour",
                    "1 tsp instant yeast",
                    "1 tsp salt",
                    "1 tbsp olive oil",
                    "300ml warm water",
                    "For the sauce:",
                    "1 can (400g) crushed tomatoes",
                    "2 cloves garlic, minced",
                    "1 tbsp olive oil",
                    "1 tsp dried oregano",
                    "1 tsp dried basil",
                    "Salt and pepper to taste",
                    "Toppings:",
                    "200g mozzarella cheese, shredded",
                    "Your choice of toppings (pepperoni, vegetables, etc.)"
                ),
                instructions = listOf(
                    "For the dough: Mix flour, yeast, and salt in a large bowl.",
                    "Add olive oil and warm water, mix until a dough forms.",
                    "Knead for 5-7 minutes until smooth and elastic.",
                    "Place in an oiled bowl, cover, and let rise for 1-2 hours.",
                    "For the sauce: Sauté garlic in olive oil until fragrant.",
                    "Add crushed tomatoes and herbs, simmer for 15 minutes.",
                    "Preheat oven to highest setting (usually 475-500°F/245-260°C).",
                    "Divide dough and roll out into rounds.",
                    "Top with sauce, cheese, and desired toppings.",
                    "Bake for 10-12 minutes until crust is golden and cheese is bubbly."
                )
            ),
            Recipe(
                id = "8",
                title = "Banana Smoothie",
                description = "A creamy and nutritious breakfast or post-workout drink that's ready in minutes.",
                imageUrl = "https://images.unsplash.com/photo-1553530666-ba11a7da3888?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YmFuYW5hJTIwc21vb3RoaWV8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "2 ripe bananas",
                    "1 cup milk (dairy or plant-based)",
                    "1/2 cup Greek yogurt",
                    "1 tbsp honey or maple syrup",
                    "1/2 tsp vanilla extract",
                    "1/2 tsp cinnamon",
                    "1 cup ice cubes",
                    "Optional add-ins: nut butter, protein powder, flax seeds"
                ),
                instructions = listOf(
                    "Peel and break bananas into chunks.",
                    "Add all ingredients to a blender.",
                    "Blend on high speed until smooth and creamy.",
                    "Adjust consistency with more milk if needed.",
                    "Pour into glasses and serve immediately."
                )
            )
        )
    }
}
