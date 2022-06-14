package dtu.engtech.estimoteproximity103

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estimote.internal_plugins_api.scanning.BluetoothScanner
import com.estimote.internal_plugins_api.scanning.ScanHandler
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.*
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory
import dtu.engtech.estimoteproximity103.CloudCredentials.APP_ID
import dtu.engtech.estimoteproximity103.CloudCredentials.APP_TOKEN
import dtu.engtech.estimoteproximity103.ui.theme.EstimoteProximity103Theme

private const val TAG = "PROXIMITY"
private const val SCANTAG = "SCANNING"

class MainActivity : ComponentActivity() {

    private lateinit var proximityObserver: ProximityObserver
    private var proximityObservationHandler: ProximityObserver.Handler? = null

    private val cloudCredentials = EstimoteCloudCredentials(
        APP_ID,
        APP_TOKEN
    )

    val zoneEventViewModel by viewModels<ZoneEventViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EstimoteProximity103Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BeaconListView(zoneEventViewModel.zoneInfo)
                }
            }
        }

        // Requirements check
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
            this,
            onRequirementsFulfilled = { startProximityObservation() },
            onRequirementsMissing = displayToastAboutMissingRequirements,
            onError = displayToastAboutError
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        proximityObservationHandler?.stop()
    }


    private fun startProximityObservation() {
        proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
            .onError(displayToastAboutError)
            .withTelemetryReportingDisabled()
            .withAnalyticsReportingDisabled()
            .withEstimoteSecureMonitoringDisabled()
            .withBalancedPowerMode()
            .build()

        val proximityZones = ArrayList<ProximityZone>()
        proximityZones.add(zoneBuild("519"))
        proximityZones.add(zoneBuild("520"))
        proximityZones.add(zoneBuild("521"))

        proximityObservationHandler = proximityObserver.startObserving(proximityZones)
    }

    private fun zoneBuild(tag: String): ProximityZone {
        return ProximityZoneBuilder()
            .forTag(tag)
            .inNearRange()
            .onEnter {
                Log.d(TAG, "Enter: ${it.tag}")
                //Skriv til Firebase
            }
            .onExit {
                Log.d(TAG, "Exit: ${it.tag}")
            }
            .onContextChange {
                Log.d(TAG, "Change: ${it}")
                zoneEventViewModel.updateZoneContexts(it)
            }
            .build()
    }

    // Lambda functions for displaying errors when checking requirements
    private val displayToastAboutMissingRequirements: (List<Requirement>) -> Unit = {
        Toast.makeText(
            this,
            "Unable to start proximity observation. Requirements not fulfilled: ${it.size}",
            Toast.LENGTH_SHORT
        ).show()
    }
    private val displayToastAboutError: (Throwable) -> Unit = {
        Toast.makeText(
            this,
            "Error while trying to start proximity observation: ${it.message}",
            Toast.LENGTH_SHORT
        ).show()
    }

}

@Composable
fun BeaconListView(zoneInfo: List<BeaconInfo>) {
    LazyColumn {
        items(zoneInfo) { beaconInfo ->
            BeaconCard(beaconInfo)
        }
    }
}


@Composable
fun BeaconCard(beaconInfo: BeaconInfo) {
    Column() {
        Text(text = DeviceList.deviceNames[beaconInfo.deviceID.uppercase()] ?: "No device name")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = beaconInfo.tag)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = beaconInfo.attachments[AttachmentKeys.DESCRIPTION.key] ?: "No description")
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EstimoteProximity103Theme {
    }
}