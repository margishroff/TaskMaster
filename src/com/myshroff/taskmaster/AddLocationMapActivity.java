package com.myshroff.taskmaster;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.myshroff.views.AddressOverlay;

public class AddLocationMapActivity extends MapActivity {
	
	public static final String ADDRESS_RESULT = "address";
	
	private Button mapLocationButton;
	private Button useLocationButton;
	private EditText addressText;
	private MapView mapView;
	private Address address;
	private MyLocationOverlay myLocationOverlay;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_location);
		setUpViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	protected void mapCurrentAddress() {
		String addressString = addressText.getText().toString();
		Geocoder g = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = g.getFromLocationName(addressString, 1);
			if (addresses.size() > 0) {
				address = addresses.get(0);
				List<Overlay> mapOverlays = mapView.getOverlays();
				AddressOverlay addressOverlay = new AddressOverlay(address);
				mapOverlays.add(myLocationOverlay);
				mapOverlays.add(addressOverlay);
				mapView.invalidate();
				final MapController mapController = mapView.getController();
				mapController.animateTo(addressOverlay.getGeopoint(), new Runnable() {
					public void run() {
						mapController.setZoom(12);
					}
				});
				useLocationButton.setEnabled(true);
			} else {
				// show the user a note that we failed to get an address
			}
		} catch (IOException e) {
			// show the user a note that we failed to get an address
			e.printStackTrace();
		}
	}

	private void setUpViews() {
		addressText = (EditText)findViewById(R.id.task_address);
		mapLocationButton = (Button)findViewById(R.id.map_location_button);
		useLocationButton = (Button)findViewById(R.id.use_this_location_button);
		useLocationButton.setEnabled(false);
		mapView = (MapView)findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.invalidate();
		
		useLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			 
				if (null != address) {
					Intent intent = new Intent();
					intent.putExtra(ADDRESS_RESULT, address);
					setResult(RESULT_OK, intent);
				}
				
				finish();
			}
		});
		mapLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mapCurrentAddress();
			}
		});
	}
	
	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
