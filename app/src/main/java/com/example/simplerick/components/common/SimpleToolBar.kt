package com.example.simplerick.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplerick.R
import com.example.simplerick.ui.theme.RickTextPrimary

@Composable
fun SimpleToolbar(
    title: String, onBackAction: (() -> Unit)? = null
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            if (onBackAction != null) {
                Box(modifier = Modifier
                    .clickable { onBackAction() }
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(shape = RoundedCornerShape(12.dp), color = Color.Transparent)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back arrow",
                        tint = RickTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = title,
                fontSize = 30.sp,
                style = TextStyle(color = RickTextPrimary, fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .background(color = RickTextPrimary)
                .fillMaxWidth()
                .height(1.dp)
        ) { }
    }
}