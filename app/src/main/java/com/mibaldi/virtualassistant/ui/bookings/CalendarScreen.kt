package com.mibaldi.virtualassistant.ui.bookings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mibaldi.virtualassistant.ui.common.GifImage
import com.mibaldi.virtualassistant.ui.common.MainAppBar

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun CalendarScreen(onClick: ()->Unit) {
    Column {
        GifImage(url="https://firebasestorage.googleapis.com/v0/b/virtualassistant-b1514.appspot.com/o/sam-sam-from-samsung.gif?alt=media&token=47bd221f-89ca-4673-b170-70a136af7d16")
        Text(text = "Calendario",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)
        /*Button(onClick = {

            navController.navigate("booking")
        }) {
            Text(text = "Booking")
        }*/
        Button(onClick) {
            Text(text = "Booking")
        }
    }
}