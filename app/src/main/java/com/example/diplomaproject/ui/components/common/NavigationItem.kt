package com.example.diplomaproject.ui.components.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomaproject.R
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary

@Composable
fun NavigationItem(
    @DrawableRes icon: Int,
    sectionName: String,
    modifier: Modifier = Modifier,
    contentColor: Color = TextPrimary
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            tint = contentColor,
            contentDescription = null,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 8.dp
            )
        )
        Text(
            text = sectionName,
            color = contentColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun NavigationItemPreview(){
    DiplomaProjectTheme {
        NavigationItem(
            icon = R.drawable.ic_home_default,
            sectionName = "Home"
        )
    }
}