package dtu.engtech.estimoteproximity103.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dtu.engtech.estimoteproximity103.model.BeaconInfo
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dtu.engtech.estimoteproximity103.ui.theme.Blue200
import org.intellij.lang.annotations.JdkConstants

var receiverLocation = ""

@Composable
fun BeaconListView(zoneInfo: List<BeaconInfo>) {
    Column (
        Modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ){
        Row( horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextFelt1()
        }
        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            zoneInfo.forEach { beaconInfo ->
                BeaconCard(beaconInfo)
            }
        }
        Row (){
            TekstFeltLokation()
        }
    }
}
@Preview
@Composable
fun TextFelt1(){
    Text(
        text = "Udstyr detekteret",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color(255,255,255),
        modifier = Modifier
            .width(200.dp)
            .height(32.dp)
            .background(color = Color(137, 207, 240), shape = RoundedCornerShape(10.dp))
    )
}


@Composable
fun TekstFeltLokation() {
    var text by remember { mutableStateOf("") }

    TextField (
        value = text,
        onValueChange = { text = it },
        label = { Text("Angiv lokale") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardActions = KeyboardActions ( onDone = {
            receiverLocation = text
        } ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

/*@Preview
@Composable
fun Knap (){
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors( Color.White),
        border = BorderStroke(2.dp, Color.Black),
    )
     {
        Text("Gem",
            color = Color(137, 207, 240),
            fontWeight = FontWeight.Bold,
         )

    }
}*/


