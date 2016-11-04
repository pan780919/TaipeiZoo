package com.jackpan.TaiwanpetadoptionApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Appkey.MyAdKey;

//import com.adlocus.AdLocusLayout$ErrorCode;
//import com.google.analytics.tracking.android.EasyTracker;

public class TwoActivity extends Activity {
	private TextView textview,textview2,textview3,textview4,
	textview5,textview6,textview7,textview8,textview9,textview10,textview11,
	textview12,textview13,textview14,textview15,textview16,textview17,textview18,textview19,textview20,textview21;
	private ImageView img,img2,img3,img4;
	private Button btn;
	private boolean isCencel = false;
	private ProgressDialog progressDialog;
	private String name;
	private Bitmap bitmap=null;
	private InterstitialAd interstitial;
	private 	AdView mAdView;
	private  Button mShareBtn;
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	private com.facebook.ads.AdView adView,adView2,adView3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 //開啟全螢幕
//		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
//		                             WindowManager.LayoutParams.FLAG_FULLSCREEN);	
//		        //設定隱藏APP標題	
//		        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_two);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mAdView = (AdView) findViewById(R.id.adView);
		setAd();
		checkBuyAd();
		initLayout();
		loadIntent();
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
		// this part is optional
		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result) {

			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException error) {

			}
		});

	}
	private void checkBuyAd(){

		boolean isBuy = MySharedPrefernces.getIsBuyed(TwoActivity.this);
		if(!isBuy){
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(MyAdKey.AdmobinterstitialBannerId);
			interstitial.loadAd(adRequest);
		}
		else {
			mAdView.setVisibility(View.GONE);
		}

	}
	private  void initLayout(){

//		MyGAManager.sendScreenName(this,"資料頁面");
		mShareBtn = (Button) findViewById(R.id.b_button_share);

		textview = (TextView) findViewById(R.id.textView1);
		textview2 = (TextView) findViewById(R.id.textView2);
		textview3 = (TextView) findViewById(R.id.textView3);
		textview4 = (TextView) findViewById(R.id.textView4);
		textview5 = (TextView) findViewById(R.id.textView5);
		textview6 = (TextView) findViewById(R.id.textView6);
		textview7 = (TextView) findViewById(R.id.textView7);
		textview8= (TextView) findViewById(R.id.textView8);
		textview9= (TextView) findViewById(R.id.textView9);
		textview10= (TextView) findViewById(R.id.textView10);
		textview11= (TextView) findViewById(R.id.textView11);
		textview12= (TextView) findViewById(R.id.textView12);
		textview13= (TextView) findViewById(R.id.textView13);
		textview14= (TextView) findViewById(R.id.textView14);
		textview15= (TextView) findViewById(R.id.textView15);
		textview17= (TextView) findViewById(R.id.textView17);
		textview19= (TextView) findViewById(R.id.textView19);
		img2= (ImageView) findViewById(R.id.pageimg2);
		textview16= (TextView) findViewById(R.id.textView16);
		img3= (ImageView) findViewById(R.id.pageimg3);
		textview18= (TextView) findViewById(R.id.textView18);
		img4= (ImageView) findViewById(R.id.pageimg4);
		textview20= (TextView) findViewById(R.id.textView20);
		textview21= (TextView) findViewById(R.id.textView21);
		img = (ImageView) findViewById(R.id.pageimg);

	}

	private void loadIntent() {
		String json = getIntent().getStringExtra("json");
		final ResultData data = new Gson().fromJson(json, ResultData.class);
		loadImage(data.A_Pic01_URL, img);
		textview.setText("中文名:"+data.A_Name_Ch);
		textview2.setText("摘要說明:"+data.A_Summary);
		textview3.setText("別名:"+data.A_AlsoKnown);
		textview4.setText("關鍵字:"+data.A_Keywords);
		textview5.setText("英文名:"+data.A_Name_En);
		textview6.setText("學名:"+data.A_Name_Latin);
		textview7.setText("分類學_門:"+data.A_Phylum);
		textview8.setText("分類學_綱:"+data.A_Class);
		textview9.setText("分類學_目:"+data.A_Order);
		textview10.setText("分類學_科:"+data.A_Family);
		textview11.setText("保育等級:"+data.A_Conservation);
		textview12.setText("地理分布:"+data.A_Distribution);
		textview13.setText("棲地型態:"+data.A_Habitat);
		textview14.setText("解說:"+data.A_Interpretation);
		textview15.setText("棲地型態:"+data.A_Habitat );
		textview17.setText("形態特徵:"+data.A_Feature );
		textview19.setText("生態習性:"+data.A_Behavior);
		textview21.setText("食性:"+data.A_Diet);
		loadImage(data.A_Pic02_URL, img2);
		textview16.setText(data.A_Pic02_ALT);
		loadImage(data.A_Pic03_URL, img3);
		textview18.setText(data.A_Pic03_ALT);
		loadImage(data.A_Pic04_URL, img4);
		textview20.setText(data.A_Pic04_ALT);
		mShareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ShareDialog.canShow(ShareLinkContent.class)) {
					ShareLinkContent linkContent = new ShareLinkContent.Builder()
							.setContentTitle("我只推薦好APP")
							.setContentDescription("動物園好好玩")
							.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.jackpan.TaipeiZoo"))
							.setImageUrl(Uri.parse(data.A_Pic01_URL))
							.build();
					shareDialog.show(linkContent);
				}
//				MyApi.copyToClipboard(getApplication(),"類型:"+data.animal_kind+"\n"+"性別:"+data.animal_sex+"\n"+"年紀:"+data.animal_age+"\n"+"收容所名稱:"+data.shelter_name+"\n"+"開放認養時間(起):"+data.animal_opendate+"開放認養時間(迄):"+data.animal_closeddate+"\n"+"資料備註:"+data.animal_remark);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.two, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent();
			i.setClass(TwoActivity.this,InAppBillingActivity.class);
			startActivity(i);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();



	}
	private void loadImage(final String path,
			final ImageView imageView){


		imageView.setImageResource(R.drawable.nophoto);

		new Thread(){

			@Override
			public void run() {

				try {
					URL imageUrl = new URL(path);
					HttpURLConnection httpCon = 
							(HttpURLConnection) imageUrl.openConnection();
					InputStream imageStr =  httpCon.getInputStream();
					bitmap = decodeSampledBitmapFromResourceMemOpt(imageStr, 640, 640);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							imageView.setImageBitmap(bitmap);

						}
					});


				} catch (Exception e) {
					e.printStackTrace();

				}



			}


		}.start();

	}
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public Bitmap decodeSampledBitmapFromResourceMemOpt(
			InputStream inputStream, int reqWidth, int reqHeight) {

		byte[] byteArr = new byte[0];
		byte[] buffer = new byte[1024];
		int len;
		int count = 0;

		try {
			while ((len = inputStream.read(buffer)) > -1) {
				if (len != 0) {
					if (count + len > byteArr.length) {
						byte[] newbuf = new byte[(count + len) * 2];
						System.arraycopy(byteArr, 0, newbuf, 0, count);
						byteArr = newbuf;
					}

					System.arraycopy(buffer, 0, byteArr, count, len);
					count += len;
				}
			}

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(byteArr, 0, count, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			//            int[] pids = { android.os.Process.myPid() };
			//            MemoryInfo myMemInfo = mAM.getProcessMemoryInfo(pids)[0];
			//            Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss);

			return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
			finish();
			boolean isBuy = MySharedPrefernces.getIsBuyed(TwoActivity.this);

			if(!isBuy)interstitial.show();
			return true;  

		}  

		return super.onKeyDown(keyCode, event);  

	}
	WebViewClient mWebViewClient = new WebViewClient() {
		  @Override
		  public boolean shouldOverrideUrlLoading(WebView view, String url) {
		   view.loadUrl(url);
		   return true;
		  }
		 };

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	private  void setAd(){
		RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
		adView = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.RECTANGLE_HEIGHT_250);
		adViewContainer.addView(adView);
		adView.loadAd();

		RelativeLayout adViewContainer2 = (RelativeLayout) findViewById(R.id.adViewContainer2);
		adView2 = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.RECTANGLE_HEIGHT_250);
		adViewContainer2.addView(adView2);
		adView2.loadAd();

		RelativeLayout adViewContainer3 = (RelativeLayout) findViewById(R.id.adViewContainer3);
		adView3 = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.RECTANGLE_HEIGHT_250);
		adViewContainer3.addView(adView3);
		adView3.loadAd();

	}
}
