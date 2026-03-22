package com.furniture.duet.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    viewModel: MainViewModel = hiltViewModel()
){
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.searchQuery = it },
                onSearch = {
                    viewModel.onSearch()
                },
                expanded = viewModel.searchQuery.isNotEmpty(),
                onExpandedChange = { },
                placeholder = { Text("Search") }
            )
        },
        expanded = viewModel.searchQuery.isNotEmpty(),
        onExpandedChange = { },
    ) {
        LazyColumn {
            items(viewModel.searchResultList) { searchResult ->
                Row(Modifier.fillMaxWidth()) {
                    AsyncImage(
                        //modifier = Modifier.height(dimensionResource(R.dimen.size_170)),
                        model = searchResult.imageUrl,
                        contentDescription = searchResult.name,
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.no_image)
                    )
                    Column {
                    }
                }
            }
        }
    }
}