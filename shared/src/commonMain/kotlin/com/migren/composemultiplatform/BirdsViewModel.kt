package com.migren.composemultiplatform

import com.migren.composemultiplatform.model.BirdModel
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BirdsUiState(
    val images: List<BirdModel> = emptyList(),
    val selectedCategory: String? = null
) {
    val categories = images.map { it.category }.toSet()
    val selectedImages = images.filter { it.category == selectedCategory }
}

class BirdsViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<BirdsUiState> = MutableStateFlow(BirdsUiState())
    val uiState: StateFlow<BirdsUiState> = _uiState.asStateFlow()

    init {
        updateImages()
    }
    override fun onCleared() {
        httpClient.close()
    }

    fun selectCategory(category: String) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }
    fun updateImages() {
        viewModelScope.launch {
            val images = getBirdModel()
            _uiState.update {
                it.copy(images = images)
            }
        }
    }
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun getBirdModel(): List<BirdModel> {
        val model = httpClient
            .get("https://sebi.io/demo-image-api/pictures.json")
            .body<List<BirdModel>>()
        return model
    }

}