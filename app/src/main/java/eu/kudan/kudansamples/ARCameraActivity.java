package eu.kudan.kudansamples;

//import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jme3.math.Quaternion;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;

public class ARCameraActivity extends ARActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    //Tracking enum
    enum ARBITRACK_STATE {
        ARBI_PLACEMENT,
        ARBI_TRACKING
    }

    private ARImageTrackable trackableApple, trackableBanana, trackableGrapes, trackableBrain, trackableSkeleton, trackableHandshake, trackableCar, trackableRoadcross;
    ARImageNode imageNode;
    ARModelNode modelNode1, modelNode2, modelNode3;
    ARVideoNode videoNode1, videoNode2;
    RelativeLayout relative;
    private ARBITRACK_STATE arbitrack_state;
    float dX, dY;
    int val;
    Button flowerBtn;
    TextView textView;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    double lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
        relative = (RelativeLayout) findViewById(R.id.relativeLayout);
        flowerBtn = (Button) findViewById(R.id.FlowerButton);
        textView = (TextView) findViewById(R.id.textView7);

        connectToApi();

        relative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:


                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        rotate(newX, newY);

                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        Intent intent = getIntent();
        val = intent.getIntExtra("value", -1);
        switch (val) {
            case 0:
                textView.setText("Point the camera at the object to get the 360 view 3D figure. Have fun playing with it!");
                break;
            case 1:
                textView.setText("Point the camera at the picture to get the actual interaction in motion");
                break;
            case 2:
                textView.setText("Align the object properly as shown in the figure ");
                break;
            case 3:
                textView.setText("Select the \" Apple \" from the pictures of fruits given");
                break;
        }
        if (val == 4) {
            arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
            flowerBtn.setVisibility(View.VISIBLE);
        } else {
            flowerBtn.setVisibility(View.GONE);
        }

    }

    private void rotate(float newX, float newY) {

//        imageNode.rotateByDegrees(newY / 10, 1, 0, 0);
//        imageNode.rotateByDegrees(newX / 10, 0, 1, 0);
        if (val == 0) {
            modelNode1.rotateByDegrees(newY / 10, 1, 0, 0);
            modelNode2.rotateByDegrees(newY / 10, 1, 0, 0);
            modelNode1.rotateByDegrees(newX / 10, 0, 1, 0);
            modelNode2.rotateByDegrees(newX / 10, 0, 1, 0);
        } else if (val == 2) {
            modelNode3.rotateByDegrees(newY / 10, 1, 0, 0);
            modelNode3.rotateByDegrees(newX / 10, 0, 1, 0);
            Quaternion q = modelNode3.getOrientation();
            Log.d("TAG", "x:" + q.getX() + " y:" + q.getY() + " z:" + q.getZ());
//            Log.d("TAG", (newX/10)+":"+(newY/10));
        }

    }

    public void setup() {
        addImageTrackable();
        addImageNode();
        addVideoNode();
        addModelNode();
        if (val == 4)
            setupArbiTrack();
    }

    private void addImageTrackable() {
        ARImageTracker trackableManager = ARImageTracker.getInstance();

        trackableApple = new ARImageTrackable("apple");
        trackableApple.loadFromAsset("apple_marker.jpg");
        trackableManager.addTrackable(trackableApple);


        trackableBanana = new ARImageTrackable("banana");
        trackableBanana.loadFromAsset("banana.jpg");
        trackableManager.addTrackable(trackableBanana);


        trackableGrapes = new ARImageTrackable("grapes");
        trackableGrapes.loadFromAsset("Grapes.jpg");
        trackableManager.addTrackable(trackableGrapes);


        trackableBrain = new ARImageTrackable("brain");
        trackableBrain.loadFromAsset("brain_marker.jpg");
        trackableManager.addTrackable(trackableBrain);

        trackableSkeleton = new ARImageTrackable("skeleton");
        trackableSkeleton.loadFromAsset("skeleton_marker.jpg");
        trackableManager.addTrackable(trackableSkeleton);

        trackableCar = new ARImageTrackable("car");
        trackableCar.loadFromAsset("porche_marker.jpg");
        trackableManager.addTrackable(trackableCar);

        trackableHandshake = new ARImageTrackable("hand");
        trackableHandshake.loadFromAsset("shake_hand_marker.jpg");
        trackableManager.addTrackable(trackableHandshake);

        trackableRoadcross = new ARImageTrackable("road");
        trackableRoadcross.loadFromAsset("road_crossing_marker.jpg");
        trackableManager.addTrackable(trackableRoadcross);
    }

    private void addModelNode() {

        ARModelImporter modelImporter1 = new ARModelImporter();
        modelImporter1.loadFromAsset("brain.jet");
        modelNode1 = modelImporter1.getNode();
        ARTexture2D texture2D1 = new ARTexture2D();
        texture2D1.loadFromAsset("brain.jpg");
        ARLightMaterial material1 = new ARLightMaterial();
        material1.setTexture(texture2D1);
        material1.setAmbient(0.8f, 0.8f, 0.8f);
        for (ARMeshNode meshNode : modelImporter1.getMeshNodes()) {
            meshNode.setMaterial(material1);
        }
        modelNode1.rotateByDegrees(90, 1, 0, 0);
        modelNode1.scaleByUniform(500.25f);
        trackableBrain.getWorld().addChild(modelNode1);

        ARModelImporter modelImporter2 = new ARModelImporter();
        modelImporter2.loadFromAsset("skeleton.jet");
        modelNode2 = modelImporter2.getNode();
        ARTexture2D texture2D2 = new ARTexture2D();
        texture2D2.loadFromAsset("skeleton.jpg");
        ARLightMaterial material2 = new ARLightMaterial();
        material2.setTexture(texture2D2);
        material2.setAmbient(0.8f, 0.8f, 0.8f);
        for (ARMeshNode meshNode : modelImporter2.getMeshNodes()) {
            meshNode.setMaterial(material2);
        }
        modelNode2.rotateByDegrees(90, 1, 0, 0);
        modelNode2.scaleByUniform(25.25f);
        trackableSkeleton.getWorld().addChild(modelNode2);

        if (val == 0) {
            modelNode1.setVisible(true);
            modelNode2.setVisible(true);
        } else {
            modelNode1.setVisible(false);
            modelNode2.setVisible(false);
        }


        ARModelImporter modelImporter3 = new ARModelImporter();
        modelImporter3.loadFromAsset("car.jet");
        modelNode3 = modelImporter3.getNode();
        ARTexture2D texture2D3 = new ARTexture2D();
        texture2D3.loadFromAsset("car.png");
        ARLightMaterial material3 = new ARLightMaterial();
        material3.setTexture(texture2D3);
        material3.setAmbient(0.8f, 0.8f, 0.8f);
        for (ARMeshNode meshNode : modelImporter3.getMeshNodes()) {
            meshNode.setMaterial(material3);
        }
        modelNode3.rotateByDegrees(90, 0, 1, 0);
        modelNode3.rotateByDegrees(180, 1, 0, 0);
        modelNode3.scaleByUniform(500.25f);
        trackableCar.getWorld().addChild(modelNode3);
//        if (val == 2)
        modelNode3.setVisible(true);
//        else
//            modelNode3.setVisible(false);


    }

//    private void addAlphaVideoNode() {
//
//        // Initialise video texture
//        ARVideoTexture videoTexture = new ARVideoTexture();
//        videoTexture.loadFromAsset("kaboom.mp4");
//
//        // Initialise alpha video node with video texture
//        ARAlphaVideoNode alphaVideoNode = new ARAlphaVideoNode(videoTexture);
//
//        // Add alpha video node to image trackable
//        trackable.getWorld().addChild(alphaVideoNode);
//
//        // Alpha video scale
//        float scale = trackable.getWidth() / videoTexture.getWidth();
//        alphaVideoNode.scaleByUniform(scale);
//
//        alphaVideoNode.setVisible(false);
//
//    }

    private void addVideoNode() {

        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("road.mp4");
        videoNode1 = new ARVideoNode(videoTexture);
        trackableRoadcross.getWorld().addChild(videoNode1);
        float scale = trackableRoadcross.getWidth() / videoTexture.getWidth();
        videoNode1.scaleByUniform(scale);
//        videoNode.setVisible(false);

        ARVideoTexture videoTexture2 = new ARVideoTexture();
        videoTexture2.loadFromAsset("hand.mp4");
        videoNode2 = new ARVideoNode(videoTexture2);
        trackableHandshake.getWorld().addChild(videoNode2);
        scale = trackableHandshake.getWidth() / videoTexture.getWidth();
        videoNode2.scaleByUniform(scale);

        if (val == 1) {
            videoNode1.setVisible(true);
            videoNode2.setVisible(true);
        } else {
            videoNode1.setVisible(false);
            videoNode2.setVisible(false);
        }

    }

    private void addImageNode() {

        imageNode = new ARImageNode("transparent-yes.png");
        trackableApple.getWorld().addChild(imageNode);
        ARTextureMaterial textureMaterial = (ARTextureMaterial) imageNode.getMaterial();
        float scale = trackableApple.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);

        imageNode = new ARImageNode("transparent-no.png");
        trackableBanana.getWorld().addChild(imageNode);
        scale = trackableBanana.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);

        imageNode = new ARImageNode("transparent-no.png");
        trackableGrapes.getWorld().addChild(imageNode);
        scale = trackableGrapes.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);
//        imageNode.setVisible(true);
        if (val == 3)
            imageNode.setVisible(true);
        else
            imageNode.setVisible(false);

    }

    public void setupArbiTrack() {

        // Create an image node to be used as a target node
        ARImageNode targetImageNode = new ARImageNode("flower.png");

        // Scale and rotate the image to the correct transformation.
        targetImageNode.scaleByUniform(0.25f);
        targetImageNode.rotateByDegrees(90, 0, 1, 0);

        // Initialise gyro placement. Gyro placement positions content on a virtual floor plane where the device is aiming.
        ARGyroPlaceManager gyroPlaceManager = ARGyroPlaceManager.getInstance();
        gyroPlaceManager.initialise();

        // Add target node to gyro place manager
        gyroPlaceManager.getWorld().addChild(targetImageNode);

        // Initialise the arbiTracker
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        // Set the arbiTracker target node to the node moved by the user.
        arbiTrack.setTargetNode(targetImageNode);

        ARImageNode imageNodeFlower = new ARImageNode("flower.png");

        // Add model node to world
        arbiTrack.getWorld().addChild(imageNodeFlower);
    }


    private void hideAll() {
//        List<ARNode> nodes = trackable.getWorld().getChildren();
//        for (ARNode node : nodes) {
//            node.setVisible(false);
//        }
    }


    public void plantFlower(View view) {

        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        // If in placement mode start arbi track, hide target node and alter label
        if (arbitrack_state.equals(ARBITRACK_STATE.ARBI_PLACEMENT)) {

            //Start Arbi Track
            arbiTrack.start();

            //Hide target node
            arbiTrack.getTargetNode().setVisible(false);
            arbiTrack.getWorld();
            //Change enum and label to reflect Arbi Track state
            arbitrack_state = ARBITRACK_STATE.ARBI_TRACKING;
            flowerBtn.setText("View other flowers");
        }

        // If tracking stop tracking, show target node and alter label
        else {

            Intent intent = new Intent(ARCameraActivity.this, MapsActivity.class);
            Log.d("TAG", "plantFlower: "+lat+": "+lon);
            intent.putExtra("lat", (double) lat);
            intent.putExtra("lon", (double) lon);
            startActivity(intent);

//            // Stop Arbi Track
//            arbiTrack.stop();
//
//            // Display target node
//            arbiTrack.getTargetNode().setVisible(true);
//
//            //Change enum and label to reflect Arbi Track state
//            arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;

        }

    }

    public void connectToApi() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
                Log.d("TAG", "connect");
            }
        } else {
            Log.e("TAG", "unable to connect to google play services.");
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getLocation();

    }

    private void getLocation() {
        // shows an error but works if this permission check is not added.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // here you get the location with location service/gps is on
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();


//                        textView.setText("Latitude:" + lat + "\nLongitude:" + lon + "\nAddress:" + fullAddress);

                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
