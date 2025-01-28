package kr.co.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Precision
import kr.co.carousel.CarouselScrollType
import kr.co.carousel.CenterCarousel
import kr.co.sample.ui.sample.movies
import kr.co.sample.ui.sample.pokemons
import kr.co.sample.ui.theme.ComposeCarouselTheme
import kr.co.scope.zoomedCarouselItems
import okhttp3.internal.toHexString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCarouselTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CarouselSample(innerPadding)
                }
            }
        }
    }
}

@Composable
fun CarouselSample(paddingValues: PaddingValues = PaddingValues()) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("name") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFACCCC))
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CenterCarousel(
            modifier = Modifier.height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            zoomedCarouselItems(
                items = movies,
                itemSize = 100.dp,
                zoomItemSize = 100.dp * 2f,
            ) { item, isFocus ->

            Card(elevation = CardDefaults.elevatedCardElevation()) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(item)
                                .size(100 * 2)
                                .build(),
                        ),
                        contentDescription = item.toHexString(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

        val itemSize = 50.dp
        CenterCarousel(
            modifier = Modifier.height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            zoomedCarouselItems(
                items = pokemons,
                itemSize = itemSize,
                zoomItemSize = itemSize * 3f,
                scrollType = CarouselScrollType.INFINITE
            ) { item, isFocus ->
                if (isFocus) name = item.second
                Card(elevation = CardDefaults.elevatedCardElevation()) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(12.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(item.first)
                                    .size((itemSize * 3f).value.toInt())
                                    .build(),
                            ),
                            contentDescription = item.second,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Text(
            text = name,
            fontSize = 30.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeCarouselTheme {
        CarouselSample()
    }
}