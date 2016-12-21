package com.jackpan.TaiwanpetadoptionApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.MyAPI.VersionChecker;
import com.adlocus.PushAd;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Appkey.MyAdKey;
import bolts.AppLinks;

//import com.adlocus.AdLocusLayout$ErrorCode;
import com.facebook.ads.*;
public class MainActivity extends Activity {
	private ListView petlist;
	//	private ArrayAdapter<String>petadp;
	//	private List<String>listpet =new ArrayList<String>();
	private ArrayList<ResultData> mAllData =new ArrayList<ResultData>();
	private TextView numtext;
	MyAdapter mydapter=null;
	private boolean isCencel = false;
	private ProgressDialog progressDialog;

	private AdView adView;
	private InterstitialAd interstitial;
	private Spinner mSpinner,mSpinner2;
	HashMap<String, ArrayList<ResultData>> mKind;
	HashMap<String, ArrayList<String>> mCity;
	private MyAdapter mAdapter;
	private ArrayAdapter<String> mAdapter2= null;
	private DatabaseReference mDatabase;
	private Button mInviteBtn;
	private ArrayList<TaipeiZoo> list = new ArrayList<>();
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 //開啟全螢幕
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
//                             WindowManager.LayoutParams.FLAG_FULLSCREEN);	
//        //設定隱藏APP標題	
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		MyGAManager.sendScreenName(this,"搜尋頁面");
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);



		mInviteBtn = (Button)findViewById(R.id.inviteBtn);
		mInviteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String appLinkUrl, previewImageUrl;
				appLinkUrl = "https://play.google.com/store/apps/details?id=com.jackpan.TaipeiZoo";
				previewImageUrl = "https://lh3.googleusercontent.com/2TPsyspPyf6WOYUEjduISOrg0HZH_xqtwa0G5LJsclL-knggHE0-KdbisjutLpr7lo8=w300-rw";

				if (AppInviteDialog.canShow()) {
					AppInviteContent content = new AppInviteContent.Builder()
							.setApplinkUrl(appLinkUrl)
							.setPreviewImageUrl(previewImageUrl)
							.build();
					AppInviteDialog.show(MainActivity.this, content);
				}
			}
		});

		Uri targetUrl =
				AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
		if (targetUrl != null) {
			Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
		} else {
			AppLinkData.fetchDeferredAppLinkData(
					getApplication(),
					new AppLinkData.CompletionHandler() {
						@Override
						public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
							//process applink data
						}
					});
		}
		progressDialog = ProgressDialog.show(MainActivity.this, "讀取中", "目前資料量比較龐大，請耐心等候！！", false, false, new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				//
				isCencel = true;
				finish();
			}
		});
		configVersionCheck();
		
		boolean isbuy =MySharedPrefernces.getIsBuyed(this);
		if (isbuy){
			Intent promotionIntent = new Intent(this, MainActivity.class);
			PushAd.enablePush(this, MyAdKey.AdLoucskey, promotionIntent);
		}else {
			PushAd.disablePush(MainActivity.this);
		}
//		FirebaseMessaging.getInstance().subscribeToTopic("news");
//
//
//		mDatabase = FirebaseDatabase.getInstance().getReference();
//		final String userId = "123456";
//		mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
//				new ValueEventListener() {
//					@Override
//					public void onDataChange(DataSnapshot dataSnapshot) {
//						// Get user value
//						User user = dataSnapshot.getValue(User.class);
//
//						// [START_EXCLUDE]
//						if (user == null) {
//							// User is null, error out
//							Log.e("Jack", "User " + userId + " is unexpectedly null");
//							Toast.makeText(MainActivity.this,
//									"Error: could not fetch user.",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							// Write new post
//							writeNewPost(userId, user.username, "name", "message");
//						}
//
//						// Finish this Activity, back to the stream
//						finish();
//						// [END_EXCLUDE]
//					}
//
//					@Override
//					public void onCancelled(DatabaseError databaseError) {
//						Log.d("Jack", "getUser:onCancelled", databaseError.toException());
//					}
//				});

//		PushAd.test(this);
//		AdView mAdView = (AdView) findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mAdView.loadAd(adRequest);
//		interstitial = new InterstitialAd(this);
//		interstitial.setAdUnitId(MyAdKey.AdmobinterstitialBannerId);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		interstitial.loadAd(adRequest);

		//		numtext= (TextView) findViewById(R.id.textView1);
		//		numtext.setVisibility(View.GONE);
		petlist = (ListView) findViewById(R.id.listView1);
		//		petadp= new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,android.R.id.text1);

		mSpinner = (Spinner) findViewById(R.id.spinner);
		mSpinner2 = (Spinner) findViewById(R.id.spinner2);
		mSpinner2.setVisibility(View.GONE);
		petlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent petint = new Intent(MainActivity.this,TwoActivity.class);
				petint.putExtra("json", new Gson().toJson(mAdapter.mDatas.get(position)));

				startActivity(petint);


			}
		});


		mAdapter = new MyAdapter(mAllData);

		petlist.setAdapter(mAdapter);

		LoadNetAsyncTask loadNetAsyncTask = new LoadNetAsyncTask();
		loadNetAsyncTask.execute(MyAdKey.jsondata);
//		MobileAds.initialize(this, "ca-app-pub-7019441527375550~9403733429");
//		adapterWrapper = new AdmobAdapterWrapper(this);
//		String admobUnitId =getResources().getString(R.string.admob_unit_id);
//
//		adapterWrapper.setAdmobReleaseUnitId(admobUnitId);
//		adapterWrapper.setAdapter(mAdapter);
//		adapterWrapper.setLimitOfAds(3);
//		adapterWrapper.setNoOfDataBetweenAds(3);
//
//		petlist.setAdapter(adapterWrapper);
//		mAdapter.notifyDataSetChanged();
//		initUpdateAdsTimer();
		RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

		adView = new AdView(this, "583698071813390_587400221443175", AdSize.BANNER_320_50);
		adViewContainer.addView(adView);
		adView.loadAd();
		}

	private void writeNewPost(String userId, String username, String title, String body) {
		// Create new post at /user-posts/$userid/$postid and at
		// /posts/$postid simultaneously
		String key = mDatabase.child("posts").push().getKey();
		Post post = new Post(userId, username, title, body);
		Map<String, Object> postValues = post.toMap();

		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put("/posts/" + key, postValues);
		childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

		mDatabase.updateChildren(childUpdates);
	}

	private class LoadNetAsyncTask extends AsyncTask<String, Void, ArrayList<ResultData>> {

		@Override
		protected void onPostExecute(final ArrayList<ResultData> result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == null){
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("出錯囉!!")
				.setMessage("很抱歉，系統暫時無法提供服務。請您稍後再試～")
				.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();
//						interstitial.show();
					}
				}).show();
				return;

			}

			final ArrayList<String> kindStrings = new ArrayList<String>(mCity.keySet());


			String id = kindStrings.toString().substring(0,kindStrings.toString().length());
		
			kindStrings.add(0, "全部");

			ArrayAdapter<String> animalKindSpinner = new
					ArrayAdapter<String>(MainActivity.this,R.layout.myspinnerlayout,kindStrings);
			mAdapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.myspinnerlayout, new ArrayList<String>());


			animalKindSpinner.setDropDownViewResource(R.layout.myspinnerlayout);
			mAdapter2.setDropDownViewResource(R.layout.myspinnerlayout);
			mSpinner.setAdapter(animalKindSpinner);
			mSpinner2.setAdapter(mAdapter2);


			mSpinner.setOnItemSelectedListener(new
					AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int
						position, long id) {
					if (position == 0) {
						mAdapter.updateData(mAllData);
						mSpinner2.setVisibility(View.GONE);
					} else {
						selectSpinner(kindStrings.get(position));
						mSpinner2.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					mAdapter.updateData(mAllData);
				}
			});
			mSpinner2.setOnItemSelectedListener(new
					AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int
						position, long id) {

					String city = (String) mSpinner.getSelectedItem();
					String township = (String) mSpinner2.getSelectedItem();

					selectSpinner2(city+","+township);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
//					mAdapter2.updateData(mAllData);
				}
			});

			mAllData = result;
			mAdapter.updateData(mAllData);

		}

			@Override
		protected ArrayList<ResultData> doInBackground(String... params) {
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			try {
				URL url = new URL(params[0]);
				HttpURLConnection httpUrlCon =
						(HttpURLConnection)url.openConnection();
				httpUrlCon.setConnectTimeout(20000);//連線
				httpUrlCon.setReadTimeout(20000);//讀取
				
				InputStream inputStream = httpUrlCon.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				br = new BufferedReader(inputStreamReader);
				String value = null;

				while((value = br.readLine()) != null){
					sb.append(value);
				}
				String result = sb.toString();

				ArrayList<ResultData> allData = new ArrayList<ResultData>();
				mKind = new HashMap<String, ArrayList<ResultData>>();//city
				mCity = new HashMap<String,ArrayList<String>>();
				try {

//					JSONArray jsonarry = new JSONArray(result);
					JSONObject o = new JSONObject(result);
					JSONObject resultObj = o.getJSONObject("result");
					JSONArray jsonarry = resultObj.getJSONArray("results");
					for(int i = 0 ; i < jsonarry.length();i++){
						JSONObject jsonObject = jsonarry.getJSONObject(i);
						Gson gson = new Gson();
						ResultData data = gson.fromJson(jsonObject.toString(), ResultData.class);
						String key =data.A_Location+","+data.A_Name_Ch;
						ArrayList<ResultData> animalKind = mKind.get(key);
						if (animalKind == null) {
							animalKind = new ArrayList<ResultData>();

						}
						mKind.put(key, animalKind);
							
						animalKind.add(data);
						
						
						ArrayList<String> towmShip = mCity.get(data.A_Location);
						if (towmShip == null) {
							towmShip = new ArrayList<String>();

						}
						mCity.put(data.A_Location, towmShip);
						if(!towmShip.contains(data.A_Name_Ch)) towmShip.add(data.A_Name_Ch);

//						data.startTime = MyApi.getTime(data.animal_opendate);

						allData.add(data);

					}
//					Collections.sort(allData);




				} catch (JSONException e) {
				}

				return allData;
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}finally{

				try {
					if(br!=null)br.close();
				} catch (IOException e) {
				}
			}

			return	null;

		}


	}
	public void selectSpinner(String kinds) {
		ArrayList<String> kindList = mCity.get(kinds);
		mAdapter2.clear();

		mAdapter2.addAll(kindList);
		
	}
	public void selectSpinner2(String kinds) {
		
		ArrayList<ResultData> kindList = mKind.get(kinds);

		mAdapter.updateData(kindList);

	}
	public class MyAdapter extends BaseAdapter {
		private ArrayList<ResultData> mDatas;

		public MyAdapter(ArrayList<ResultData> datas) {
			mDatas = datas;
		}
		public void updateData(ArrayList<ResultData> datas) {
			mDatas = datas;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = LayoutInflater.from(MainActivity.this).inflate(
						R.layout.mylayout, null);
			ResultData data = mDatas.get(position);
			TextView textname = (TextView) convertView.findViewById(R.id.name);
			TextView list = (TextView) convertView.findViewById(R.id.txtengname);
			TextView bigtext= (TextView) convertView.findViewById(R.id.bigtext);
			TextView place= (TextView) convertView.findViewById(R.id.palace);
			TextView time= (TextView) convertView.findViewById(R.id.time);
			textname.setText("館區:"+data.A_Location);
			list.setText("中文名:"+data.A_Name_Ch);
//			bigtext.setText(data.A_Keywords);
			bigtext.setVisibility(View.GONE);
			place.setText("英文名:"+data.A_Name_En );
			time.setText("地理分布:"+data.A_Distribution );
			ImageView imageView = (ImageView) convertView.findViewById(R.id.photoimg);
			//			loadImage(data.album_file, img);
			//			Glide.with(MainActivity.this).load(data.album_file).into(imageView);

			Glide.with(MainActivity.this)
			.load(data.A_Pic01_URL)
			.centerCrop()
			.placeholder(R.drawable.nophoto)
			.crossFade()
			.into(imageView);
			return convertView;
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

			ConfirmExit(); //呼叫ConfirmExit()函數

			return true;  

		}  

		return super.onKeyDown(keyCode, event);  

	}



	public void ConfirmExit(){

		AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊

		ad.setTitle("離開");

		ad.setMessage("確定要離開?");

		ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

			public void onClick(DialogInterface dialog, int i) {

				
				MainActivity.this.finish();//關閉activity
//				interstitial.show();

			}

		});

		ad.setNegativeButton("否",new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

			public void onClick(DialogInterface dialog, int i) {

			}

		});

		ad.show();//顯示訊息視窗

	}



	private void loadImage(final String path,
			final ImageView imageView){

		new Thread(){

			@Override
			public void run() {

				try {
					URL imageUrl = new URL(path);
					HttpURLConnection httpCon = 
							(HttpURLConnection) imageUrl.openConnection();
					InputStream imageStr =  httpCon.getInputStream();
					final Bitmap bitmap =  BitmapFactory.decodeStream(imageStr);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});


				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					Log.e("Howard", "MalformedURLException:"+e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("Howard", "IOException:"+e);
				}



			}


		}.start();

	}

	public class User {

		public String username;
		public String email;

		public User() {
			// Default constructor required for calls to DataSnapshot.getValue(User.class)
		}

		public User(String username, String email) {
			this.username = username;
			this.email = email;
		}

	}
	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
	private void configVersionCheck() {

//        if (!GtApi.checkNetwork(IndexActivity.this)) return;

		VersionChecker.checkOnce(MainActivity.this, new VersionChecker.DoneAdapter() {

			@Override
			public void onHasNewVersion() {
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("已有最新版本!")
						.setMessage("目前有最新版本上架,請盡快更新")
						.setNegativeButton("確定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(VersionChecker.openMartketIntent());
								dialog.dismiss();
							}
						})
						.show();
			}


		});

	}
}