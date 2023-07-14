package com.example.paralox_scrolleffect

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.paralox_scrolleffect.ui.theme.Paralox_ScrollEffectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Paralox_ScrollEffectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   val moonScrollEffect = 0.08f
                    val midBgScrollEffect = 0.03f
                    var moonOffeset by remember {
                        mutableStateOf(0f)
                    }
                    var midBgOffset by remember {
                        mutableStateOf(0f)
                    }

                    val imageheight = (LocalConfiguration.current.screenWidthDp * (2f/3f)).dp
                    val lazylist = rememberLazyListState()
                    val nestedScrollConnection = object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            val delta = available.y
                            val layoutInfo = lazylist.layoutInfo
                            // Check if the first item is visible
                            if (lazylist.firstVisibleItemIndex == 0) {
                                return Offset.Zero
                            }
                            if (layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1) {
                                return Offset.Zero
                            }
                            moonOffeset += delta * moonScrollEffect
                            midBgOffset += delta * midBgScrollEffect
                            return Offset.Zero
                        }
                    }
                    LazyColumn(modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection), state = lazylist){ items(10){
                        Text(text = "Hello You are experiencing parallax effect", modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp))
                    }

                        item {
                            Box(
                                modifier = Modifier
                                    .clipToBounds()
                                    .fillMaxWidth()
                                    .height(imageheight + midBgOffset.toDp())
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFFf36b21),
                                                Color(0xFFf9a521)
                                            )
                                        )
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_moonbg),
                                    contentDescription = "moon",
                                    contentScale = ContentScale.FillWidth,
                                    alignment = Alignment.BottomCenter,
                                    modifier = Modifier.matchParentSize()
                                        .graphicsLayer {
                                            translationY = moonOffeset
                                        }
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_midbg),
                                    contentDescription = "mid bg",
                                    contentScale = ContentScale.FillWidth,
                                    alignment = Alignment.BottomCenter,
                                    modifier = Modifier.matchParentSize()
                                        .graphicsLayer {
                                            translationY = midBgOffset
                                        }
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_outerbg),
                                    contentDescription = "outer bg",
                                    contentScale = ContentScale.FillWidth,
                                    alignment = Alignment.BottomCenter,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                        }

                       items(20){ Text(text = "Hello You are experiencing parallax effect", modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp))
                    }
                    }
                }
            }
        }
    }
    private fun Float.toDp(): Dp {
        return (this / Resources.getSystem().displayMetrics.density).dp
    }
}

