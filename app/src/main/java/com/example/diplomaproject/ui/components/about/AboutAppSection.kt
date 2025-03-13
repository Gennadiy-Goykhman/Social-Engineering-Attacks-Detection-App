package com.example.diplomaproject.ui.components.about

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.components.common.FeatureDescription
import com.example.diplomaproject.ui.components.home.FeatureItem
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary

@Composable
fun AboutAppSection(
    title: String,
    description: String,
    appInfo: FeatureItem,
    modifier: Modifier = Modifier,
    textPrimaryColor: Color = TextPrimary
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = textPrimaryColor
        )
        FeatureDescription(
            title = appInfo.title,
            description = appInfo.description,
            icon = appInfo.icon
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = textPrimaryColor
        )
    }
}


@Preview
@Composable
fun AboutAppSectionPreview() {
    DiplomaProjectTheme {
        AboutAppSection(
            title = "About app",
            description = "A secure and private communication app that lets you send text messages and make calls without worrying about your privacy.",
            appInfo = FeatureItem(
                title = "App info",
                description = "Version 1.0.1 (beta)",
                icon = R.drawable.ic_shield_default
            )
        )
    }
}
