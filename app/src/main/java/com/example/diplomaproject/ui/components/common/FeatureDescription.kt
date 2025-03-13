package com.example.diplomaproject.ui.components.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.theme.BackgroundIcon
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.IconPrimary
import com.example.diplomaproject.ui.theme.TextPrimary
import com.example.diplomaproject.ui.theme.TextSecondary

@Composable
fun FeatureDescription(
    title: String,
    description: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    titleColor: Color = TextPrimary,
    descriptionColor: Color = TextSecondary,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        IconContainer(
            icon = icon,
            modifier = Modifier
                .padding(vertical = 17.dp)
                .padding(end = 16.dp)
        )
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.titleSmall
                )
            Text(
                text = description,
                color = descriptionColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun IconContainer(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    iconColor: Color = IconPrimary,
    containerColor: Color = BackgroundIcon
) {
    Surface(
        shape = RoundedCornerShape(CornerSize(8.dp)),
        color = containerColor,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconColor
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun FeatureDescriptionPreview(){
    DiplomaProjectTheme {
        FeatureDescription(
            title = "Download Protection",
            description = "Prevents you from downloading dangerous files or apps, and warns you if you try to.",
            icon = R.drawable.ic_shield_default,
        )
    }
}

@Preview
@Composable
fun IconContainerPreview(){
    DiplomaProjectTheme {
        IconContainer(
            icon = R.drawable.ic_shield_default
        )
    }
}