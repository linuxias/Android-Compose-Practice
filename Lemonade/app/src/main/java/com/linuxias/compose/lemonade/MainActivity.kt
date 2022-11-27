package com.linuxias.compose.lemonade

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linuxias.compose.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeApp()
        }
    }
}

@Composable
fun LemonadeApp() {
    var currentStep by remember { mutableStateOf(1) }
    var squeezeCount by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        when (currentStep) {
            1 -> LemonTextAndImage(
                text = stringResource(id = R.string.lemon_select),
                painter = painterResource(id = R.drawable.lemon_tree),
                contentDescription = stringResource(id = R.string.lemon_tree_content_description),
                onImageClick = {
                    currentStep = 2
                    squeezeCount = (2..4).random()
                }
            )
            2 -> LemonTextAndImage(
                text = stringResource(id = R.string.lemon_squeeze),
                painter = painterResource(id = R.drawable.lemon_squeeze),
                contentDescription = stringResource(id = R.string.lemon_content_description),
                onImageClick = {
                    squeezeCount--
                    if (squeezeCount == 0) {
                        currentStep = 3
                    }
                })
            3 -> LemonTextAndImage(
                text = stringResource(id = R.string.lemon_drink),
                painter = painterResource(id = R.drawable.lemon_drink),
                contentDescription = stringResource(id = R.string.lemonade_content_description),
                onImageClick = {
                    currentStep = 4
                })
            else -> LemonTextAndImage(
                text = stringResource(id = R.string.lemon_empty_glass),
                painter = painterResource(id = R.drawable.lemon_restart),
                contentDescription = stringResource(id = R.string.empty_glass_content_description),
                onImageClick = {
                    currentStep = 1
                })
        }
    }
}

@Composable
fun LemonTextAndImage(
    text: String,
    painter: Painter,
    contentDescription: String, 
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text)
        Spacer(Modifier.size(16.dp))
        Image (
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier
                .wrapContentSize()
                .clickable (onClick = onImageClick)
                .border(
                    BorderStroke(2.dp, Color(105, 205, 216)),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun LemonPreview() {
    LemonadeTheme() {
        LemonadeApp()
    }
}