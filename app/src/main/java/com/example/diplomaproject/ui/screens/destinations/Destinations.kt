package com.example.diplomaproject.ui.screens.destinations

import kotlinx.serialization.Serializable

sealed interface AppDestination

@Serializable data object HomeDestination: AppDestination
@Serializable data object AboutAppDestination: AppDestination

