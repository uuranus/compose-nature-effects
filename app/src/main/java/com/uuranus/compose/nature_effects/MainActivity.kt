package com.uuranus.compose.nature_effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.uuranus.compose.nature_effects.hang.PendulumEffect
import com.uuranus.compose.nature_effects.ui.theme.ComposenatureeffectsTheme
import com.uuranus.compose.nature_effects.water.ripple.RippleEffect
import com.uuranus.compose.nature_effects.water.wave.WaveEffect
import com.uuranus.compose.nature_effects.wind.WindBlownDiagonalEffect
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {

            ComposenatureeffectsTheme {
                var size by remember {
                    mutableStateOf(
                        IntSize.Zero
                    )
                }

                val density = LocalDensity.current

                val heightDp by remember {
                    derivedStateOf {
                        (with(density) {
                            size.height.toDp()
                        })
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val startHeight by remember {
                        mutableStateOf(with(density) {
                            (size.height / 10).toDp()
                        })
                    }

                    var targetHeight by remember {
                        mutableStateOf(
                            startHeight
                        )
                    }

                    LaunchedEffect(Unit) {
                        while (targetHeight <= heightDp) {
                            targetHeight += 30.dp
                            delay(50)
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
//                        RippleEffect(modifier = Modifier.fillMaxSize(),
//
//                            )
                        PendulumEffect(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(100.dp)
                                .aspectRatio(1f),
                            initialAngle = 90f,
                        )
//                        WaveEffect(
//                            modifier = Modifier
//                                .width(500.dp)
//                                .aspectRatio(1f)
//                                .onGloballyPositioned {
//                                    size = it.size
//                                }
//                                .clip(CircleShape),
//                            waveAmplitude = with(
//                                LocalDensity.current
//                            ) {
//                                20.dp.toPx()
//                            },
//                            waterHeight = targetHeight,
//                            waveProgressDuration = 2000,
//                            numberOfPeaks = 1
//                        )

//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(color = Color(0xFF3A3A3A))
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.img),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize(),
//                                contentScale = ContentScale.Crop
//                            )
//                            WindBlownDiagonalEffect(
//                                R.drawable.leaf
//                            )
//                            WindBlownEffect(
//                                backgroundColor = Color.Transparent,
//                                lightColor = Color(0xFFFdFF99)
//                            )


//                            FloatingUpEffect()
//                        Greeting()
                    }

//                    Greeting(name = "Hello", modifier = Modifier.fillMaxSize())
                }


            }
        }
    }

}

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        val name by remember {
            mutableStateOf("Anrdoid")
        }
        Text(
            text = "Hello $name!",
            modifier = modifier,
            textAlign = TextAlign.Center
        )
    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposenatureeffectsTheme {
        Greeting()
    }
}