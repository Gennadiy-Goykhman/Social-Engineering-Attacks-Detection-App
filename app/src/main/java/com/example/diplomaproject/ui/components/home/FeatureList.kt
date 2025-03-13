package com.example.diplomaproject.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.components.common.FeatureDescription
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary

@Composable
fun FeatureList(
    title: String,
    features: List<FeatureItem>,
    modifier: Modifier = Modifier,
    titleColor: Color = TextPrimary,
){
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 12.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
        features.forEach {
            FeatureDescription(
                icon = it.icon,
                title = it.title,
                description = it.description,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun FeatureListPreview() {
    DiplomaProjectTheme {
        FeatureList(
            title = "Features",
            features = List(5) {
                FeatureItem(
                    icon = R.drawable.ic_shield_default,
                    title = "Download Protection",
                    description = "Prevents you from downloading dangerous files or apps, and warns you if you try to."
                )
            }
        )
    }
}

data class FeatureItem(
    @DrawableRes val icon: Int,
    val title: String,
    val description: String
)