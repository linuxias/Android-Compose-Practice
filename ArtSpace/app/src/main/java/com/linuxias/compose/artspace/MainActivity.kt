package com.linuxias.compose.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linuxias.compose.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                ArtSpaceApp()
            }
        }
    }
}

data class Art(
    val title: String,
    val contentDescription: String,
    val artist: String,
    val drawableId: Int,
    val year: Int
)

@Composable
fun ArtSpaceApp() {
    var artIndex by remember { mutableStateOf(0) }
    val arts = listOf<Art>(
        Art(
            title = "Sunset",
            contentDescription = "sunset image",
            artist = "Unknown",
            drawableId = R.drawable.sunset,
            year = 1990
        ),
        Art(
            title = "Sunrise",
            contentDescription = "sunrise image",
            artist = "Reo",
            drawableId = R.drawable.sunrise,
            year = 1990
        ),
        Art(
            title = "Load",
            contentDescription = "load image",
            artist = "Son",
            drawableId = R.drawable.road,
            year = 1990
        )

    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val art = arts[artIndex]
        ArtImage(
            painterResource(id = art.drawableId),
            art.contentDescription,
            modifier = Modifier
                .fillMaxWidth(.9f)
                .weight(8f)
        )
        //Spacer(modifier = Modifier.height(16.dp))
        ArtDescription(
            art.title,
            art.contentDescription,
            art.year,
            Modifier
                .wrapContentSize()
                .weight(2f)
        )
        ArtSelector(
            {
                artIndex = artIndex - 1;
                if (artIndex < 0)
                    artIndex = arts.size - 1
            },
            {
                artIndex = artIndex + 1;
                if (artIndex >= arts.size)
                    artIndex = 0;
            }
        )
    }
}

@Composable
fun ArtSelector(
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .padding(16.dp)
    ) {
        Button(
            onClick = onPrevious,
            modifier = Modifier.weight(3f)
        ) {
                Text(text = stringResource(R.string.previous_btn))
        }
        Spacer(modifier = Modifier.weight(.5f))
        Button(
            onClick = onNext,
            modifier = Modifier.weight(3f)
        ) {
            Text(text = stringResource(R.string.next_btn))
        }
    }
}

@Composable
fun ArtImage(
    painter: Painter,
    contentDescription: String,
    modifier:Modifier = Modifier,
) {
    Surface (
        modifier = modifier
            .wrapContentSize()
            .padding(vertical = 16.dp)
            .border(width = 1.dp, color = Color.Black),
        elevation = 9.dp
    ) {
        Image(painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
        )
    }
}

@Composable
fun ArtDescription(
    title: String,
    artist: String,
    year: Int,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp,
                color = Color.Gray,
                shape = RectangleShape)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 28.sp
            )
            Row {
                Text(
                    text = artist,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${year.toString()})"
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtButton() {
    ArtSpaceTheme {
        ArtSelector(
            { },
            { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtDescriptionPreview() {
    ArtSpaceTheme {
        ArtDescription(
            "Title",
            "artist",
            2013,
            Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}