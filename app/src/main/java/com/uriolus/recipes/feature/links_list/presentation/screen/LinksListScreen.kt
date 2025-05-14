package com.uriolus.recipes.feature.links_list.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uriolus.recipes.R
import com.uriolus.recipes.feature.links_list.presentation.components.RecipeLinkItem
import com.uriolus.recipes.feature.links_list.presentation.state.LinksListEvent
import com.uriolus.recipes.feature.links_list.presentation.viewmodel.LinksListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinksListScreen(
    viewModel: LinksListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(LinksListEvent.DismissError)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Links") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Add new link section
            OutlinedTextField(
                value = state.newLinkUrl,
                onValueChange = { viewModel.onEvent(LinksListEvent.OnUrlChange(it)) },
                label = { Text("Recipe URL") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = state.newLinkTitle,
                onValueChange = { viewModel.onEvent(LinksListEvent.OnTitleChange(it)) },
                label = { Text("Recipe Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { viewModel.onEvent(LinksListEvent.OnAddLink) },
                enabled = !state.isAddingLink,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Link")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // List of links
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.links.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No recipe links yet. Add your first one!")
                }
            } else {
                LazyColumn {
                    items(state.links) { link ->
                        RecipeLinkItem(
                            recipeLink = link,
                            onDeleteClick = {
                                viewModel.onEvent(LinksListEvent.DeleteLink(link))
                            }
                        )
                    }
                }
            }
        }
    }
}
