package com.example.diplomaproject.ui.components.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.theme.BackgroundPrimary
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary

@Composable
fun TopBar(
    title: String?,
    @DrawableRes leftIcon: Int?,
    onLeftIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = TextPrimary,
    backgroundBrush: Brush = Brush.verticalGradient(
        colors = listOf(BackgroundPrimary, Color.Transparent)
    )
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundBrush)
    ) {
        leftIcon?.let {
            Icon(
                painter = painterResource(leftIcon),
                tint = contentColor,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 29.dp, bottom = 20.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onLeftIconClick() }
            )
        }
        title?.let {
            Text(
                text = it,
                color = contentColor,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(horizontal = 64.dp)
                    .padding(top = 28.dp, bottom = 20.dp)
            )
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    DiplomaProjectTheme {
        TopBar(
            title = "Device protection",
            leftIcon = R.drawable.ic_shield_default,
            onLeftIconClick = {}
        )
    }
}