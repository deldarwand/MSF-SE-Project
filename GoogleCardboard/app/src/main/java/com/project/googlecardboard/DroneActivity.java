package com.project.googlecardboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.project.googlecardboard.bluetooth.BluetoothService;
import com.project.googlecardboard.projection.CameraUtil;
import com.project.googlecardboard.render.Renderer;
import com.project.googlecardboard.util.BackgroundThread;
import com.project.googlecardboard.util.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Garrett on 22/10/2015.
 */
public class DroneActivity extends CardboardActivity{

    private Vibrator vibrator;
    private BackgroundThread backgroundThread;
    private CardboardOverlayView view;

    private static final String TAG_HOSP = "Hospital";
    private static final String TAG_LOCATION = "Your Location";
    private MapView mapView;
    private MapView mapView2;
    private GeoPoint currentLocation;
    private ItemizedOverlay<OverlayItem> hospOverlay;
    private ItemizedOverlay<OverlayItem> locationOverlay;

    // CARDBOARD ACTIVITY

    /**
     * Called when the activity is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.backgroundThread = new BackgroundThread();
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        IO.init(getBaseContext(), vibrator);
        BluetoothService.INSTANCE.init(this);
        while(!BluetoothService.INSTANCE.isEnabled()
                && !BluetoothService.INSTANCE.isDiscovering()){
            enableBluetoothService(BluetoothService.INSTANCE);
        }
        CameraUtil cameraUtil = new CameraUtil(this);
        enableBackgroundThread(backgroundThread);
        enableRenderer(Renderer.INSTANCE);

        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setX(1000 - mapView.getWidth());

        final IMapController mapController = mapView.getController();
        mapController.setZoom(16);
        this.currentLocation = new GeoPoint(51.524173, -0.131792); //TODO: get GPS location

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Updating current location");
                mapController.setCenter(currentLocation);//TODO: update center as drone moves
            }
        }, 0, 10000); // Updates center every 10 secs

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        // Add scale overlay
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
       // scaleBarOverlay.setScaleBarOffset(
         //       (int) (getResources().getDisplayMetrics().widthPixels / 2 - getResources()
           //             .getDisplayMetrics().xdpi / 2), 10);
        mapView.getOverlays().add(scaleBarOverlay);

        // Add markers
        ArrayList<OverlayItem> hospItems = parseJson();
        final ArrayList<OverlayItem> myLocationItems = new ArrayList<>();

        OverlayItem myLocationOverlayItem = new OverlayItem(TAG_LOCATION, "Drone location", currentLocation);
        myLocationItems.add(myLocationOverlayItem);

        // Generate Hospital nodes Overlay
        hospOverlay = new ItemizedIconOverlay<>(hospItems, this.getDrawable(R.drawable.marker_hospital),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getTitle() + ": " + item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(this.hospOverlay);


        // Generate myLocation nodes Overlay
        locationOverlay = new ItemizedIconOverlay<>(myLocationItems, this.getDrawable(R.drawable.person),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getTitle() + ": " + item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(this.locationOverlay);
        setupMap2();

    }


    void setupMap2()
    {
        mapView2 = (MapView) findViewById(R.id.map2);
        mapView2.setTileSource(TileSourceFactory.MAPNIK);
        mapView2.setBuiltInZoomControls(true);
        mapView2.setMultiTouchControls(true);
        mapView2.setX(0);

        final IMapController mapController = mapView2.getController();
        mapController.setZoom(16);
        this.currentLocation = new GeoPoint(/*51.524173, -0.131792*/51.5252657f,-0.1390035f); //TODO: get GPS location

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Updating current location");
                mapController.setCenter(currentLocation);//TODO: update center as drone moves
            }
        }, 0, 10000); // Updates center every 10 secs

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        // Add scale overlay
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView2);
        scaleBarOverlay.setCentred(true);
        // scaleBarOverlay.setScaleBarOffset(
        //       (int) (getResources().getDisplayMetrics().widthPixels / 2 - getResources()
        //             .getDisplayMetrics().xdpi / 2), 10);
        mapView2.getOverlays().add(scaleBarOverlay);

        // Add markers
        ArrayList<OverlayItem> hospItems = parseJson();
        //final ArrayList<OverlayItem> uniItems = new ArrayList<>();

        //uniItems.add(uniOverlayItem);

        // Generate Hospital nodes Overlay
        hospOverlay = new ItemizedIconOverlay<>(hospItems, this.getDrawable(R.drawable.marker_hospital),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                DroneActivity.this,
                                item.getTitle() + ": " + item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                }, resourceProxy);
        this.mapView2.getOverlays().add(this.hospOverlay);

    }

    /**/

    public ArrayList<OverlayItem> parseJson() {
        ArrayList<OverlayItem> hospItemList = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset());
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("elements");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String mtype = jsonObject.optString("type");
                String mname = jsonObject.getJSONObject("tags").optString("name");

                if (mtype.equals("node")) {
                    double mlat = Double.parseDouble(jsonObject.optString("lat"));
                    double mlon = Double.parseDouble(jsonObject.optString("lon"));
                    if (isCloseToLocation(mlat, mlon, currentLocation)) {
                        hospItemList.add(new OverlayItem(TAG_HOSP, mname, new GeoPoint(mlat, mlon)));
                    }
                }
                else if (mtype.equals("way")) {
                    double tlat = 0;
                    double tlon = 0;
                    JSONArray nodes = jsonObject.getJSONArray("nodes");
                    for (int n=0; n<nodes.length(); n++) {
                        long nodeId = nodes.getLong(n);
                        GeoPoint mgeopoint = findElementWithId(nodeId, jsonArray);
                        tlat = tlat + mgeopoint.getLatitude();
                        tlon = tlon + mgeopoint.getLongitude();
                    }
                    tlat = tlat/nodes.length();
                    tlon = tlon/nodes.length();
                    if (isCloseToLocation(tlat, tlon, currentLocation)) {
                        OverlayItem centerItem = new OverlayItem(TAG_HOSP, mname, new GeoPoint(tlat, tlon));
                        hospItemList.add(centerItem);
                    }
                }
            }

        } catch (JSONException e) {e.printStackTrace();}

        return hospItemList;
    }

    public GeoPoint findElementWithId(long nodeId, JSONArray jsonArray){
        GeoPoint geoPoint = null;
        try {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong("id");
                if (id == nodeId) {
                    geoPoint = new GeoPoint(Double.parseDouble(jsonObject.optString("lat")),
                            Double.parseDouble(jsonObject.optString("lon")));
                    return geoPoint;
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
        return geoPoint;
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getResources().getAssets().open("LND_hospital.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /*
    Latitude: 1 deg = 110.574 km
    1km = 1/110.574 deg lat

    Longitude: 1 deg = 111.320*cos(latitude) km
    1km = 1/(111.320*cos(lat)) deg lon

    */
    public boolean isCloseToLocation(double lat, double lon, GeoPoint current) {
        int radius = 1; //in km
        return Math.abs(lat - current.getLatitude()) < radius/110.574 &&
                Math.abs(lon-current.getLongitude()) < radius/(111.320 * Math.cos(radius/ 110.574));
    }


    /**
     * Called when the cardboard's trigger is activated
     */
    @Override
    public void onCardboardTrigger(){
        //view.show3DToast("Hello world");
        Renderer.INSTANCE.onCardboardTrigger();
        //vibrate(50);
    }

    // MISCELLANEOUS

    /**
     * Vibrates the phone
     * @param time Time in milliseconds
     */
    public void vibrate(long time){
        vibrator.vibrate(time);
    }

    private void enableBackgroundThread(BackgroundThread backgroundThread){
        backgroundThread.start();
    }

    private void enableRenderer(Renderer renderer){
        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);


        view = (CardboardOverlayView) findViewById(R.id.overlay);
    }

    private void enableBluetoothService(BluetoothService bluetoothService){
        System.out.println("G::Turning bluetooth on.");
        bluetoothService.enable();
        bluetoothService.enableDiscovering();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        BluetoothService.INSTANCE.teardown();
        Renderer.INSTANCE.onRendererShutdown();
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
