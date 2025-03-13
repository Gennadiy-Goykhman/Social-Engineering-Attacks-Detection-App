package com.example.diplomaproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.components.about.AboutAppSection
import com.example.diplomaproject.ui.components.about.AdditionalItemInfo
import com.example.diplomaproject.ui.components.about.AdditionalList
import com.example.diplomaproject.ui.components.common.TopBar
import com.example.diplomaproject.ui.components.home.FeatureItem
import com.example.diplomaproject.ui.theme.BackgroundPrimary
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme

@Composable
fun AboutScreen(
    aboutAppInfo: AboutAppInfo,
    additionalListInfo: AdditionalListInfo,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundPrimary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(
            title = null,
            leftIcon = R.drawable.ic_shield_default,
            onLeftIconClick = {},
            backgroundBrush = Brush.verticalGradient(colors = listOf(BackgroundPrimary, BackgroundPrimary))
        )
        AboutAppSection(
            title = aboutAppInfo.title,
            description = aboutAppInfo.description,
            appInfo = aboutAppInfo.appInfo,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        AdditionalList(
            title = additionalListInfo.title,
            additionalItemInfos = additionalListInfo.additionalItemInfos,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

    }
}

@Preview
@Composable
fun AboutScreenPreview(){
    DiplomaProjectTheme {
        AboutScreen(
            aboutAppInfo = aboutAppInfoMock,
            additionalListInfo = additionalListInfoMock
        )
    }
}

val aboutAppInfoMock = AboutAppInfo(
    title = "About app",
    description = "A secure and private communication app that lets you send text messages and make calls without worrying about your privacy.",
    appInfo = FeatureItem(
        title = "App info",
        description = "Version 1.0.1 (beta)",
        icon = R.drawable.ic_shield_default
    )
)

val additionalListInfoMock = AdditionalListInfo(
    title = "Additional Info",
    additionalItemInfos = listOf(
        AdditionalItemInfo(
            text = "Licensing",
            onClick = {}
        ),
        AdditionalItemInfo(
            text = "Permissions",
            onClick = {}
        ),
        AdditionalItemInfo(
            text = "Accessibility",
            onClick = {}
        ),
    )
)

data class AboutAppInfo(
    val title: String,
    val description: String,
    val appInfo: FeatureItem
)

data class AdditionalListInfo(
    val title: String,
    val additionalItemInfos: List<AdditionalItemInfo>
)