package com.example.diplomaproject.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.theme.BackgroundIcon
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.IconPrimary
import com.example.diplomaproject.ui.theme.IconSecondary

@Composable
fun SwitchFunctionalityButton(
    isActivated: Boolean,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundBrush: Brush = Brush.radialGradient(
        colors = listOf(BackgroundIcon, BackgroundIcon, BackgroundIcon, Color.Transparent)
    ),
    activeIconColor: Color = IconPrimary,
    inactiveIconColor: Color = IconSecondary
) {
    Surface(
        shape = CircleShape,
        color = Color.Transparent,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(backgroundBrush)
            .size(300.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
            .padding(64.dp, vertical = 50.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                tint = if (isActivated) activeIconColor else inactiveIconColor,
                contentDescription = null
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun SwitchFunctionalityButtonPreview() {
    DiplomaProjectTheme {
        SwitchFunctionalityButton(
            isActivated = true,
            icon = R.drawable.ic_protected_person,
            onClick = {}
        )
    }
}