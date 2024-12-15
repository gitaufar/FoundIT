package com.example.foundit.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foundit.R
import com.example.foundit.enumclass.Categories
import com.example.foundit.ui.theme.poppinsFamily

@Preview
@Composable
fun CardCategory(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: Painter = painterResource(R.drawable.dompet),
    category: String = "Dompet"
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .width(80.dp)
                .height(80.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 12.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(1.dp)
                    .width(40.dp)
                    .height(40.dp),
                tint = Color(0xFF193A6F)
            )
            Text(
                text = category,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF193A6F),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}