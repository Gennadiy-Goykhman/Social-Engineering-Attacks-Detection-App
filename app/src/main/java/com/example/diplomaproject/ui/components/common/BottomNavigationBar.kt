package com.example.diplomaproject.ui.components.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.screens.destinations.AboutAppDestination
import com.example.diplomaproject.ui.screens.destinations.AppDestination
import com.example.diplomaproject.ui.screens.destinations.HomeDestination
import com.example.diplomaproject.ui.theme.BackgroundIcon
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary
import com.example.diplomaproject.ui.theme.TextSecondary

@Composable
fun BottomNavigationBar(
    sections: List<BottomNavigationItem>,
    onSelect: (Int, AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = TextPrimary,
    unselectedColor: Color = TextSecondary,
    backgroundColor: Color = BackgroundIcon
){
    NavigationBar(
        containerColor = backgroundColor,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        sections.forEachIndexed { idx, element ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(element.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = element.label,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                selected = element.isSelected,
                colors = NavigationBarItemColors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    disabledIconColor = unselectedColor,
                    disabledTextColor = unselectedColor,
                    selectedIndicatorColor = Color.Transparent,
                ),
                onClick = { onSelect(idx, element.destination) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview(){
    DiplomaProjectTheme {
        BottomNavigationBar(
            sections = listOf(
                BottomNavigationItem(
                    icon = R.drawable.ic_home_default,
                    label = "Home",
                    isSelected = true,
                    destination = HomeDestination
                ),
                BottomNavigationItem(
                    icon = R.drawable.ic_home_default,
                    label = "About app",
                    isSelected = false,
                    destination = AboutAppDestination
                )
            ),
            onSelect = {_,_ -> }
        )
    }
}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val label: String,
    val isSelected: Boolean,
    val destination: AppDestination
)