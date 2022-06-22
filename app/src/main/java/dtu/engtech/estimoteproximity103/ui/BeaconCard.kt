package dtu.engtech.estimoteproximity103.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dtu.engtech.estimoteproximity103.core.DeviceList
import dtu.engtech.estimoteproximity103.model.AttachmentKeys
import dtu.engtech.estimoteproximity103.model.BeaconInfo

@Composable
fun BeaconCard(beaconInfo: BeaconInfo) {
    Column() {
       // Text(text = "Enhed: "+DeviceList.deviceNames[beaconInfo.deviceID.uppercase()] ?: "No device name")
       // Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Beacon tag: " +beaconInfo.tag)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Udstyr: "+beaconInfo.attachments[AttachmentKeys.DESCRIPTION.key] ?: "No description")
        Spacer(modifier = Modifier.height(16.dp))
    }
}