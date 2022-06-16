package dtu.engtech.estimoteproximity103.ui


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dtu.engtech.estimoteproximity103.model.BeaconInfo

@Composable
fun BeaconListView(zoneInfo: List<BeaconInfo>) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Row( horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextFelt1()
        }
        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            zoneInfo.forEach { beaconInfo ->
                BeaconCard(beaconInfo)
            }
        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            SimpleOutlinedTextFieldSample()
        }
        }
    }
}
@Preview
@Composable
fun TextFelt1(){
    Text(
        text = "Udstyr detekteret",
        textAlign = TextAlign.Center,
        color = Color(255,255,255),
        modifier = Modifier
            .width(150.dp)
            .height(30.dp)
            .padding(top = 10.dp)
            .border(
                width = 2.dp,
                color = Color(137, 207, 240),
                shape = RoundedCornerShape(25.dp)
            )
            .background(color = Color(137, 207, 240), shape = RoundedCornerShape(10.dp))
    )
}

@Preview
@Composable
fun SimpleOutlinedTextFieldSample() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField (
        value = text,
        onValueChange = { text = it },
        label = { Text("Angiv lokale") },

    )

}

@Preview
@Composable
fun Knap (){
    Button(
        onClick = {
            /*TODO*/
        }
    ) {
        Text(text = "Gem")
    }
}

fun HentData (){

}
