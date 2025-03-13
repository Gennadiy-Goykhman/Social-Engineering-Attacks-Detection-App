package com.example.diplomaproject.ui.components.about


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
fun AdditionalList(
    title: String,
    additionalItemInfos: List<AdditionalItemInfo>,
    modifier: Modifier = Modifier,
    textColor: Color = TextPrimary
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        additionalItemInfos.forEach {
            AdditionalItem(
                text = it.text,
                onClick = it.onClick,
                textColor = textColor
            )
        }
    }
}

@Composable
fun AdditionalItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = TextPrimary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 12.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            tint = textColor,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AdditionalItemPreview() {
    DiplomaProjectTheme {
        AdditionalItem(
            text = "Licensing",
            onClick = {}
        )
    }
}


@Preview
@Composable
fun AdditionalListPreview() {
    DiplomaProjectTheme {
        AdditionalList(
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
    }
}

data class AdditionalItemInfo(
    val text: String,
    val onClick: () -> Unit
)