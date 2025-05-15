package com.uriolus.recipes.feature.links_list.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uriolus.recipes.R
import com.uriolus.recipes.feature.links_list.presentation.components.ImageSelectionDialog
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
                title = { Text(text = stringResource(R.string.links_list_title)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Add new link section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    OutlinedTextField(
                        value = state.newLinkUrl,
                        onValueChange = { viewModel.onEvent(LinksListEvent.OnUrlChange(it)) },
                        label = { Text(text = stringResource(R.string.recipe_url_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = state.newLinkTitle,
                        onValueChange = { viewModel.onEvent(LinksListEvent.OnTitleChange(it)) },
                        label = { Text(text = stringResource(R.string.recipe_title_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { viewModel.onEvent(LinksListEvent.AnalyzeWebsite) },
                            enabled = !state.isAnalyzingWebsite && !state.isAddingLink && state.newLinkUrl.isNotBlank()
                        ) {
                            Text(text = stringResource(R.string.analyze_website_button))
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = { viewModel.onEvent(LinksListEvent.OnAddLink) },
                            enabled = !state.isAddingLink
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = stringResource(R.string.add_link_button))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Links list
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
                        Text(text = stringResource(R.string.no_links_message))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.links) { link ->
                            RecipeLinkItem(
                                link = link,
                                onDeleteClick = {
                                    viewModel.onEvent(LinksListEvent.DeleteLink(link))
                                }
                            )
                        }
                    }
                }
            }
            
            // Image selection dialog
            if (state.showImageSelectionDialog) {
                ImageSelectionDialog(
                    images = state.extractedImages,
                    isLoading = state.isAnalyzingWebsite,
                    onImageSelected = { url ->
                        viewModel.onEvent(LinksListEvent.SelectThumbnail(url))
                    },
                    onDismiss = {
                        viewModel.onEvent(LinksListEvent.CloseImageSelectionDialog)
                    }
                )
            }
        }
    }
}
