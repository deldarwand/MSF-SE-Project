package mycompany.osmdroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class MainActivity extends ActionBarActivity {

    private static final String TAG_HOSP = "Hospital";
    private static final String TAG_UNI = "University";
    private ItemizedOverlay<OverlayItem> hospOverlay;
    private ItemizedOverlay<OverlayItem> uniOverlay;
    private MapView mapView;
    private IMapController mapController;

    private GeoPoint currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
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
        scaleBarOverlay.setScaleBarOffset(
                (int) (getResources().getDisplayMetrics().widthPixels / 2 - getResources()
                .getDisplayMetrics().xdpi / 2), 10);
        mapView.getOverlays().add(scaleBarOverlay);

        // Add markers
        ArrayList<OverlayItem> hospItems = parseJson();
        //final ArrayList<OverlayItem> uniItems = new ArrayList<>();

        OverlayItem uniOverlayItem = new OverlayItem(TAG_UNI, "UCL",
                new GeoPoint(51.524173, -0.131792));

        //uniItems.add(uniOverlayItem);

        // Generate Hospital nodes Overlay
        hospOverlay = new ItemizedIconOverlay<>(hospItems, this.getDrawable(R.drawable.marker_hospital),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                MainActivity.this,
                                item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                MainActivity.this,
                                item.getTitle() + ": " + item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(this.hospOverlay);

        // Generate University nodes Overlay
//        uniOverlay = new ItemizedIconOverlay<>(uniItems, this.getDrawable(R.drawable.marker_uni),
//                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
//                        Toast.makeText(
//                                MainActivity.this,
//                                item.getSnippet(), Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                    public boolean onItemLongPress(final int index, final OverlayItem item) {
//                        Toast.makeText(
//                                MainActivity.this,
//                                item.getTitle() + ": " + item.getSnippet(), Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                }, resourceProxy);
//        this.mapView.getOverlays().add(this.uniOverlay);
    }

    // DANIEL: start from here
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
            InputStream is = getAssets().open("LND_hospital.json");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
