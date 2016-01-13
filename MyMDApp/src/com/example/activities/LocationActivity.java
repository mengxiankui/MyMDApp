package com.example.activities;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.WheelOptions.WheelOptionsAdapter;
import com.example.mymdapp.R;
import com.example.util.AddressUtil;
import com.gc.materialdesign.widgets.Dialog;
import com.mxk.baseapplication.LBaseActivity;
import com.mxk.baseframe.util.log.Logger;
import com.mxk.baseframe.util.toast.frenchtoast.FrenchToast;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class LocationActivity extends LBaseActivity {
	private static final String LOG_TAG = LocationActivity.class
			.getSimpleName();
	@Bind(R.id.txt_location1)
	TextView txtLoaction1;
	@Bind(R.id.txt_location2)
	TextView txtLoaction2;
	@Bind(R.id.txt_location3)
	TextView txtLoaction3;
	@Bind(R.id.txt_address3)
	TextView txtAddress3;
	@Bind(R.id.txt_address4)
	TextView txtAddress4;
	@Bind(R.id.txt_address5)
	TextView txtAddress5;

	@Bind(R.id.btn_gps)
	Button btnGPS;
	@Bind(R.id.btn_network)
	Button btnNetwork;
	@Bind(R.id.btn_tencent)
	Button btnTencent;
	@Bind(R.id.btn_auto)
	Button btnAuto;
	@Bind(R.id.btn_choose)
	Button btnChoose;

	private Location lGPS;
	private Location lNetwork;
	private TencentLocation lTencent;

	private LocationManager locationManager;
	private TencentLocationManager mLocationManager;

	private LocationListener gpsLocationListener, networkLocationListener;
	private TencentLocationListener tencentLocationListener;

	OptionsPickerView pvOptions;
	WheelOptionsAdapter wheelOptionsAdapter;
	AddressUtil addressUtil;

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_location;
	}

	@Override
	public void setToolBar(Toolbar mToolbar) {
		// TODO Auto-generated method stub
		mToolbar.setTitle("位置定位测试");
	}

	@Override
	public void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ButterKnife.bind(this);
		setupToolBar();
		init();
		initAction();

	}

	private void initAction() {
		// TODO Auto-generated method stub
		btnGPS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 2000, 10,
						gpsLocationListener);
			}
		});
		btnNetwork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 2000, 10,
						networkLocationListener);
			}
		});

		btnTencent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 创建定位请求
				TencentLocationRequest request = TencentLocationRequest
						.create()
						.setInterval(5000)
						// 设置定位周期
						.setRequestLevel(
								TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA); // 设置定位level

				// 开始定位
				mLocationManager.requestLocationUpdates(request,
						tencentLocationListener);
			}
		});

		btnAuto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FrenchToast.with(LocationActivity.this).shortLength()
						.showText("btnAuto Clicked");
			}
		});

		btnChoose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pvOptions = new OptionsPickerView(LocationActivity.this, 3);
				pvOptions.setLinkage(true);
				pvOptions.setTextSize(20);
				pvOptions.setAdapter(wheelOptionsAdapter);
				pvOptions
						.setOnoptionsSelectListener(new OnOptionsSelectListener() {

							@Override
							public void onOptionsSelect(
									ArrayList<String> currentItems) {
								// TODO Auto-generated method stub
								StringBuffer sb = new StringBuffer();
								for (String str : currentItems) {
									sb.append(str).append(",");
								}
								sb.deleteCharAt(sb.length() - 1);
								txtAddress5.setText(sb.toString());
							}
						});
				pvOptions.show();

			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager = TencentLocationManager.getInstance(this);
		addressUtil = new AddressUtil(this);
		// lGPS = locationManager
		// .getLastKnownLocation(LocationManager.GPS_PROVIDER);//
		// 调用getLastKnownLocation()方法获取当前的位置信息
		// lNetwork = locationManager
		// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		setLocation1();
		setLocation2();
		setLocation3();
		setAdress3();

		gpsLocationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "gpsLocationListener onStatusChanged, "
						+ status);
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "gpsLocationListener onProviderEnabled");
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "gpsLocationListener onProviderDisabled");
			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "gpsLocationListener onLocationChanged, "
						+ "坐标：纬度-" + location.getLatitude() + ", 经度-"
						+ location.getLongitude());
				lGPS = location;
				setLocation1();
			}
		};

		networkLocationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "networkLocationListener onStatusChanged "
						+ status);
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "networkLocationListener onProviderEnabled");
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "networkLocationListener onProviderDisabled");
			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Logger.d(LOG_TAG, "networkLocationListener onLocationChanged, "
						+ "坐标：纬度-" + location.getLatitude() + ", 经度-"
						+ location.getLongitude());
				lNetwork = location;
				setLocation2();
			}
		};

		tencentLocationListener = new TencentLocationListener() {

			@Override
			public void onStatusUpdate(String name, int status, String desc) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(TencentLocation location, int error,
					String reason) {
				// TODO Auto-generated method stub
				if (TencentLocation.ERROR_OK == error) {
					// 定位成功
					Logger.d(LOG_TAG,
							"tencentLocationListener onLocationChanged, "
									+ "定位成功");
					lTencent = location;
					setLocation3();
					setAdress3();
					setAdress4();
					mLocationManager.removeUpdates(tencentLocationListener);

				} else {
					// 定位失败
					Logger.d(LOG_TAG,
							"tencentLocationListener onLocationChanged, "
									+ "定位失败, 原因" + reason);
					// Dialog dialog = new Dialog(LocationActivity.this, "定位失败",
					// "网络连接不稳定，请稍后重试");
					// dialog.add
					mLocationManager.removeUpdates(tencentLocationListener);
				}

			}

		};
		
		wheelOptionsAdapter = new WheelOptionsAdapter() {

			@Override
			public SparseArray<ArrayList<String>> getThirdWheelData(
					int parentIndex) {
				// TODO Auto-generated method stub
				return addressUtil.getAreaByPid(parentIndex);
			}

			@Override
			public SparseArray<ArrayList<String>> getSecondWheelData(
					int parentIndex) {
				// TODO Auto-generated method stub
				return addressUtil.getCityByPid(parentIndex);
			}

			@Override
			public SparseArray<ArrayList<String>> getFirstWheelData() {
				// TODO Auto-generated method stub
				return addressUtil.getProvince();
			}
		};
	}

	private void setLocation1() {
		if (null != lGPS) {
			txtLoaction1.setText("坐标：纬度-" + lGPS.getLatitude() + ", 经度-"
					+ lGPS.getLongitude());
		} else {
			txtLoaction1.setText("坐标：纬度-未获取" + ", 经度-未获取");
		}

	}

	private void setLocation2() {
		// TODO Auto-generated method stub
		if (null != lNetwork) {
			txtLoaction2.setText("坐标：纬度-" + lNetwork.getLatitude() + ", 经度-"
					+ lNetwork.getLongitude());
		} else {
			txtLoaction2.setText("坐标：纬度-未获取" + ", 经度-未获取");
		}

	}

	private void setLocation3() {
		// TODO Auto-generated method stub
		if (null != lTencent) {
			txtLoaction3.setText("坐标：纬度-" + lTencent.getLatitude() + ", 经度-"
					+ lTencent.getLongitude());
		} else {
			txtLoaction3.setText("坐标：纬度-未获取" + ", 经度-未获取");
		}

	}

	private void setAdress3() {
		// TODO Auto-generated method stub
		if (null != lTencent) {
			txtAddress3.setText(lTencent.getNation() + ", "
					+ lTencent.getProvince() + ", " + lTencent.getCity() + ", "
					+ lTencent.getDistrict() + ", " + lTencent.getTown() + ", "
					+ lTencent.getVillage() + ", " + lTencent.getStreet()
					+ ", " + lTencent.getStreetNo());
		} else {
			txtAddress3.setText("地址信息未获取");
		}
	}

	private void setAdress4() {
		// TODO Auto-generated method stub
		if (null != lTencent) {
			txtAddress4.setText(lTencent.getProvince() + ", "
					+ lTencent.getCity() + ", " + lTencent.getDistrict());
		}
		btnAuto.setVisibility(View.VISIBLE);
	}

	private void setupToolBar() {
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getToolBar().setNavigationOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (locationManager != null) {
			locationManager.removeUpdates(gpsLocationListener);
			locationManager.removeUpdates(networkLocationListener);
		}
		if (null != mLocationManager) {
			mLocationManager.removeUpdates(tencentLocationListener);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 2000, 10, gpsLocationListener);
		//
		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 2000, 10, networkLocationListener);
	}

	class Adress {
		// nation 国家
		// province 省
		// city 市
		// district 区
		// town 镇
		// village 村
		// street 街道
		// streetNo 门号

		private String nation, province, city, district, town, village, street,
				streetNo;

		public String getNation() {
			return nation;
		}

		public void setNation(String nation) {
			this.nation = nation;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public String getTown() {
			return town;
		}

		public void setTown(String town) {
			this.town = town;
		}

		public String getVillage() {
			return village;
		}

		public void setVillage(String village) {
			this.village = village;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getStreetNo() {
			return streetNo;
		}

		public void setStreetNo(String streetNo) {
			this.streetNo = streetNo;
		}

	}

}