package com.example.bookshelf.ui.screens.menu_screen

//import androidx.compose.ui.tooling.data.EmptyGroup.data
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.bookshelf.R
import com.example.bookshelf.ui.theme.BookshelfTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalTextApi::class)
private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
private val googleFont = GoogleFont("Creepster")

@OptIn(ExperimentalTextApi::class)
private val fontFamily = FontFamily(
    Font(googleFont = googleFont, fontProvider = fontProvider)
)

@Composable
fun MenuScreen(
    onSearchClick: () -> Unit,
    onFavClick: () -> Unit

) {

    // Add these lines at the beginning of your MenuScreen function
    val isPhoneNumberClicked = remember { mutableStateOf(false) }
    val isUrlClicked = remember { mutableStateOf(false) }
val purple = Color(0xFF800080)

    val context = LocalContext.current

    val isError = remember { mutableStateOf(false) }

    val phoneNumber = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = if (isPhoneNumberClicked.value) purple else Color.Blue
            )
        ) {
            append("(819) 848-8008")
        }
    }


    val annotatedLink = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = if (isUrlClicked.value) purple else Color.Blue
            )
        ) {
            append("www.flyingspaghettimonster.org")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {


        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            var loadWebView by remember { mutableStateOf(false) }
            var isLoading by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                isLoading = true
                delay(500) // Delay for 1 second, adjust as needed
                loadWebView = true
                isLoading = false
            }

            Text(
                "WELCOME TO THE FLYING SPAGHETTI MONSTER BREWERY", textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 36.sp, // Change this to your desired size

                color = Color.Red,
                fontFamily = fontFamily,
                letterSpacing = 1.5.sp,
                lineHeight = 35.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.fsm2),
                contentDescription = "Flying Spaghetti Baby Jesus Monster",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = onSearchClick
//            ) {
//                Text(text = stringResource(R.string.btn_search))
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = onFavClick
//            ) {
//                Text(text = stringResource(R.string.btn_favorite))
//            }


            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Row() {
                    Text("Address:", fontSize = 22.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "123 Main Street,\n Lennoxville",
                        fontSize = 22.sp,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text("Phone:", fontSize = 22.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = phoneNumber,
                        fontSize = 22.sp,
                        textAlign = TextAlign.End,
                        // Modify your clickable modifiers like this:
                        modifier = Modifier.clickable {
                            isPhoneNumberClicked.value = true
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:(819) 848-8008")
                            )
                            val chooser = Intent.createChooser(intent, "Choose a Dialer")
                            context.startActivity(chooser)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Website:", fontSize = 22.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = annotatedLink,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            isUrlClicked.value = true
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://www.flyingspaghettimonster.org")
                            )
                            val chooser = Intent.createChooser(intent, "Choose a Browser")
                            context.startActivity(chooser)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {

                if (loadWebView) {


                    if (isError.value) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.ic_connection_error),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            stringResource(R.string.failed_to_load_map_msg),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        val url =
                            "https://www.bing.com/maps?osid=d25be20c-2239-4c4d-b164-61dafb8fd7da&cp=45.418444~-72.059228&lvl=11&imgid=2e87d9d1-122f-4311-a199-24c1259f5406&v=2&sV=2&form=S00027"
                        AndroidView(
                            factory = {
                                WebView(it).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    webViewClient = object : WebViewClient() {
                                        override fun onReceivedError(
                                            view: WebView?,
                                            request: WebResourceRequest?,
                                            error: WebResourceError?
                                        ) {
                                            super.onReceivedError(view, request, error)
                                            isError.value = true
                                        }

                                        override fun onPageStarted(
                                            view: WebView?,
                                            url: String?,
                                            favicon: Bitmap?
                                        ) {
                                            super.onPageStarted(view, url, favicon)
                                            isLoading = true
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            isLoading = false
                                        }
                                    }
                                    webChromeClient = WebChromeClient()
                                    settings.javaScriptEnabled = true
                                    loadUrl(url)
                                }
                            }, update = {
                                it.loadUrl(url)
                            }, modifier = Modifier.height(300.dp)
                        )
                    }
                }
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            }
        }

        FloatingActionButton(
            onClick = {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:flyingspaghetti@monster.ca")
                }
                val chooser = Intent.createChooser(emailIntent, "Choose an Email client")
                context.startActivity(chooser)
            },
            modifier = Modifier.align(Alignment.BottomEnd).testTag("Email FAB"),
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon"
            )
        }


    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun MenuScreenPreview() {
//    BookshelfTheme {
//        MenuScreen(onSearchClick = { }, onFavClick = { })
//    }
//}
