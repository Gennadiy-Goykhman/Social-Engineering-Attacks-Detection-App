package com.example.diplomaproject.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.components.home.FeatureItem
import com.example.diplomaproject.ui.components.home.FeatureList
import com.example.diplomaproject.ui.components.home.SwitchFunctionalityButton
import com.example.diplomaproject.ui.components.common.TopBar
import com.example.diplomaproject.ui.theme.BackgroundPrimary
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary


@Composable
fun HomeScreen(
    featuresState: FeaturesListState,
    switchFunctionState: SwitchFunctionState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundPrimary
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(
            title = stringResource(R.string.app_name),
            leftIcon = R.drawable.ic_shield_default,
            onLeftIconClick = {}
        )
        SwitchFunctionalityButton(
            isActivated = switchFunctionState.isActivated,
            icon = switchFunctionState.icon,
            onClick = switchFunctionState.onClick,
        )
        FeatureList(
            title = featuresState.title,
            features = featuresState.features,
            titleColor = featuresState.titleColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    DiplomaProjectTheme {
        HomeScreen(
            featuresState = featureStateMock,
            switchFunctionState = switchFunctionStateMock
        )
    }
}

val featureStateMock = FeaturesListState(
    title = "Features",
    titleColor = TextPrimary,
    features = List(5) {
        FeatureItem(
            icon = R.drawable.ic_shield_default,
            title = "Download Protection",
            description = "Prevents you from downloading dangerous files or apps, and warns you if you try to."
        )
    }
)

val switchFunctionStateMock = SwitchFunctionState(
    isActivated = true,
    icon = R.drawable.ic_protected_person,
    onClick = {}
)

data class FeaturesListState(
    val title: String,
    val titleColor: Color,
    val features: List<FeatureItem>
)

data class SwitchFunctionState(
    val isActivated: Boolean,
    @DrawableRes val  icon: Int,
    val onClick: () -> Unit,
)