package dtu.engtech.estimoteproximity103

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.*
import com.google.firebase.firestore.FirebaseFirestore
import dtu.engtech.estimoteproximity103.core.CloudCredentials.APP_ID
import dtu.engtech.estimoteproximity103.core.CloudCredentials.APP_TOKEN
import dtu.engtech.estimoteproximity103.core.FirestoreBeaconConstants
import dtu.engtech.estimoteproximity103.ui.BeaconListView
import dtu.engtech.estimoteproximity103.ui.ZoneEventViewModel
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
                   //testFirestoreGet()
                }
            }
        }
        //testUpdateEquipmentLocation()

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
                //testUpdateEquipmentLocation()
                updateEquipmentLocation(it.tag)
                Log.d(it.tag, "It-tag")
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
    private fun testUpdateEquipmentLocation(){
       FirebaseFirestore.getInstance().collection("equipment").document("OjRva3nQsjYIN7J90ZQj")
            .update(FirestoreBeaconConstants.LOCATION, FirestoreBeaconConstants.RECEIVERLOCATION)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d(TAG, "Error updating document", e) }

    }
    private fun updateEquipmentLocation(tag: String) {
        val docRef = FirebaseFirestore.getInstance().collection(FirestoreBeaconConstants.EQUIPMENT)
        docRef.whereEqualTo(FirestoreBeaconConstants.ZONETAG, tag)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(FirestoreBeaconConstants.FIREBASELOGTAG, "Number of documents => ${documents.size()}")
                    Log.d(FirestoreBeaconConstants.FIREBASELOGTAG, "${document.id} => ${document.data}")

                    //update
                    docRef.document(document.id)
                        .update(FirestoreBeaconConstants.LOCATION, FirestoreBeaconConstants.RECEIVERLOCATION)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(FirestoreBeaconConstants.FIREBASELOGTAG, "Error getting documents: ", exception)
            }
    }
  /*  private fun testFirestoreGet() {
        val docRef = FirebaseFirestore.getInstance().collection("equipment")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(FirestoreBeaconConstants.FIREBASELOGTAG,"${document.id} => ${document.data}")
                    Log.d(FirestoreBeaconConstants.FIREBASELOGTAG, "Henter noget")
                }
            }
            .addOnFailureListener{exception ->
                Log.w(FirestoreBeaconConstants.FIREBASELOGTAG,"Error getting documents: ", exception)
            }
    }*/
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EstimoteProximity103Theme {

    }
}