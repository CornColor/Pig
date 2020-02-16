package browser.pig.cn.pig.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.luck.picture.lib.PictureSelector;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nanchen.compresshelper.CompressHelper;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import browser.pig.cn.pig.R;
import browser.pig.cn.pig.SaveImageUtils;
import browser.pig.cn.pig.ZpWebChromeClient;
import browser.pig.cn.pig.utils.X5WebView;
import cn.my.library.utils.util.FilePathUtil;
import cn.my.library.utils.util.FileUtils;
import cn.my.library.utils.util.Util;
import cn.pedant.SweetAlert.SweetAlertDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


import static android.os.Environment.DIRECTORY_DCIM;
import static android.view.View.VISIBLE;

public class BrowserActivity extends Activity {
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	private X5WebView mWebView;
	private ViewGroup mViewParent;
	private ImageButton mBack;
	private ImageButton mForward;
	private ImageButton mExit;
	private ImageButton mHome;
	private ImageButton mMore;
//	private Button mGo;
//	private EditText mUrl;

	private static  String mHomeUrl = "";
	private static final String TAG = "SdkDemo";
	private static final int MAX_LENGTH = 14;
	private boolean mNeedTestPage = false;

	private final int disable = 120;
	private final int enable = 255;

	private ProgressBar mPageLoadingProgressBar = null;

	private ValueCallback<Uri> uploadFile;

	private URL mIntentUrl;

	private SweetAlertDialog dialog;
	private ImageView iv_load;
	private RelativeLayout rl_load;




	public static final int REQUEST_SELECT_FILE_CODE = 100;
	private static final int REQUEST_FILE_CHOOSER_CODE = 101;
	private static final int REQUEST_FILE_CAMERA_CODE = 102;
	// 默认图片压缩大小（单位：K）
	public static final int IMAGE_COMPRESS_SIZE_DEFAULT = 400;
	// 压缩图片最小高度
	public static final int COMPRESS_MIN_HEIGHT = 900;
	// 压缩图片最小宽度
	public static final int COMPRESS_MIN_WIDTH = 675;

	private ValueCallback<Uri> mUploadMsg;
	private ValueCallback<Uri[]> mUploadMsgs;
	// 相机拍照返回的图片文件
	private File mFileFromCamera;
	private BottomSheetDialog selectPicDialog;

	private List<LocalMedia> selectList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Intent intent = getIntent();
		if (intent != null) {
			try {
				mIntentUrl = new URL(intent.getData().toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {

			} catch (Exception e) {
			}
			mHomeUrl = intent.getStringExtra("url");
		}
		//
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
				getWindow()
						.setFlags(
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
		}

		/*
		 * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		setContentView(R.layout.activity_browser);
		mViewParent = (ViewGroup) findViewById(R.id.webView1);

		initBtnListenser();
		dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
		dialog.setTitleText("上传图片...");
		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
		rl_load = findViewById(R.id.rl_load);
		iv_load = findViewById(R.id.iv_rload);

	}

	private void changGoForwardButton(WebView view) {
		if (view.canGoBack())
			mBack.setAlpha(enable);
		else
			mBack.setAlpha(disable);
		if (view.canGoForward())
			mForward.setAlpha(enable);
		else
			mForward.setAlpha(disable);
		if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
			mHome.setAlpha(disable);
			mHome.setEnabled(false);
		} else {
			mHome.setAlpha(enable);
			mHome.setEnabled(true);
		}
	}

	private void initProgressBar() {
		mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
																				// ProgressBar(getApplicationContext(),
																				// null,
																				// android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
	}

	private void init() {

		mWebView = new X5WebView(this, null);

		mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT));

		initProgressBar();

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
				mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
					changGoForwardButton(view);
				/* mWebView.showLog("test Log"); */
			}
		});

		mWebView.setWebChromeClient23(new ZpWebChromeClient() {

			@Override
			public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
					JsResult arg3) {
				return super.onJsConfirm(arg0, arg1, arg2, arg3);
			}

			View myVideoView;
			View myNormalView;
			CustomViewCallback callback;


			@Override
			public void onProgressChanged(WebView webView, int i) {
				super.onProgressChanged(webView, i);
				Log.e("进度",i+"");

				if (i == 100) {
					iv_load.clearAnimation();
					rl_load.setVisibility(View.INVISIBLE);
				} else {
                    if(iv_load.getAnimation()== null){
						RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						LinearInterpolator lin = new LinearInterpolator();
						rotate.setInterpolator(lin);
						rotate.setDuration(3000);//设置动画持续周期
						rotate.setRepeatCount(-1);//设置重复次数
						rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
						rotate.setStartOffset(0);//执行前的等待时间
						iv_load.setAnimation(rotate);
					}

					rl_load.setVisibility(VISIBLE);

				}
			}


			// /////////////////////////////////////////////////////////
			//
			/**
			 * 全屏播放配置
			 */
			@Override
			public void onShowCustomView(View view,
					CustomViewCallback customViewCallback) {
				FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
				ViewGroup viewGroup = (ViewGroup) normalView.getParent();
				viewGroup.removeView(normalView);
				viewGroup.addView(view);
				myVideoView = view;
				myNormalView = normalView;
				callback = customViewCallback;
			}

			@Override
			public void onHideCustomView() {
				if (callback != null) {
					callback.onCustomViewHidden();
					callback = null;
				}
				if (myVideoView != null) {
					ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}

			@Override
			public boolean onJsAlert(WebView arg0, String arg1, String arg2,
					JsResult arg3) {
				/**
				 * 这里写入你自定义的window alert
				 */
				return super.onJsAlert(null, arg1, arg2, arg3);
			}
		});
		mWebView.setOpenFileChooserCallBack(new ZpWebChromeClient.OpenFileChooserCallBack() {


			@Override
			public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
				mUploadMsg = uploadMsg;
				showSelectPictrueDialog(0, null);
			}

			@Override
			public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
				if (mUploadMsgs != null) {
					mUploadMsgs.onReceiveValue(null);
				}
				mUploadMsgs = filePathCallback;
				showSelectPictrueDialog(1, fileChooserParams);
			}
		});

		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String arg0, String arg1, String arg2,
					String arg3, long arg4) {
				TbsLog.d(TAG, "url: " + arg0);
				new AlertDialog.Builder(BrowserActivity.this)
						.setTitle("allow to download？")
						.setPositiveButton("yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(
												BrowserActivity.this,
												"fake message: i'll download...",
												Toast.LENGTH_LONG).show();
									}
								})
						.setNegativeButton("no",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Toast.makeText(
												BrowserActivity.this,
												"fake message: refuse download...",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {

									@Override
									public void onCancel(DialogInterface dialog) {
										// TODO Auto-generated method stub
										Toast.makeText(
												BrowserActivity.this,
												"fake message: refuse download...",
												Toast.LENGTH_SHORT).show();
									}
								}).show();
			}
		});

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		long time = System.currentTimeMillis();
		if (mIntentUrl == null) {
			mWebView.loadUrl(mHomeUrl);
		} else {
			mWebView.loadUrl(mIntentUrl.toString());
		}
		TbsLog.d("time-cost", "cost time: "
				+ (System.currentTimeMillis() - time));
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
		setWebImageLongClickListener();
	}
	/**
	 * 选择图片弹框
	 */
	private void showSelectPictrueDialog(final int tag, final WebChromeClient.FileChooserParams fileChooserParams) {
		selectPicDialog = new BottomSheetDialog(this, R.style.Dialog_NoTitle);
		selectPicDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				if (mUploadMsgs != null) {
					mUploadMsgs.onReceiveValue(null);
					mUploadMsgs = null;
				}
			}
		});
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_select_pictrue, null);
		// 相册
		TextView album = view.findViewById(R.id.tv_select_pictrue_album);
		// 相机
		TextView camera = view.findViewById(R.id.tv_select_pictrue_camera);
		// 取消
		TextView cancel = view.findViewById(R.id.tv_select_pictrue_cancel);

		album.setOnClickListener(new View.OnClickListener() {
			// @TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {
//                if (tag == 0) {
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("*/*");
//                    startActivityForResult(Intent.createChooser(i, "File Browser"), REQUEST_FILE_CHOOSER_CODE);
//                } else {
//                    try {
//                        Intent intent = fileChooserParams.createIntent();
//                        startActivityForResult(intent, REQUEST_SELECT_FILE_CODE);
//                    } catch (ActivityNotFoundException e) {
//                        mUploadMsgs = null;
//                    }
//                }
				PictureSelector.create(BrowserActivity.this)
						.openGallery(PictureMimeType.ofImage())
						.isCamera(false)
						.imageSpanCount(4)
						.selectionMode(PictureConfig.SINGLE)
						.forResult(PictureConfig.CHOOSE_REQUEST);



			}
		});
		camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// takeCameraPhoto();
				PictureSelector.create(BrowserActivity.this)
						.openCamera(PictureMimeType.ofImage())
						.forResult(PictureConfig.CHOOSE_REQUEST);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectPicDialog.dismiss();
			}
		});

		selectPicDialog.setContentView(view);
		selectPicDialog.show();
	}
	private void initBtnListenser() {
		mBack = (ImageButton) findViewById(R.id.btnBack1);
		mForward = (ImageButton) findViewById(R.id.btnForward1);
		mExit = (ImageButton) findViewById(R.id.btnExit1);
		mHome = (ImageButton) findViewById(R.id.btnHome1);
//		mGo = (Button) findViewById(R.id.btnGo1);
//		mUrl = (EditText) findViewById(R.id.editUrl1);
		mMore = (ImageButton) findViewById(R.id.btnMore);
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
			mBack.setAlpha(disable);
			mForward.setAlpha(disable);
			mHome.setAlpha(disable);
		}
		mHome.setEnabled(false);

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebView != null && mWebView.canGoBack()){
					mWebView.goBack();
				}else {
					finish();
				}

			}
		});

		mForward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebView != null && mWebView.canGoForward())
					mWebView.goForward();
			}
		});

//		mGo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String url = mUrl.getText().toString();
//				mWebView.loadUrl(url);
//				mWebView.requestFocus();
//			}
//		});

		mMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(BrowserActivity.this, "not completed",
						Toast.LENGTH_LONG).show();
			}
		});

//		mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					mGo.setVisibility(View.VISIBLE);
//					if (null == mWebView.getUrl())
//						return;
//					if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
//						mUrl.setText("");
//						mGo.setText("首页");
//						mGo.setTextColor(0X6F0F0F0F);
//					} else {
//						mUrl.setText(mWebView.getUrl());
//						mGo.setText("进入");
//						mGo.setTextColor(0X6F0000CD);
//					}
//				} else {
//					mGo.setVisibility(View.GONE);
//					String title = mWebView.getTitle();
//					if (title != null && title.length() > MAX_LENGTH)
//						mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
//					else
//						mUrl.setText(title);
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				}
//			}
//
//		});
//
//		mUrl.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//
//				String url = null;
//				if (mUrl.getText() != null) {
//					url = mUrl.getText().toString();
//				}
//
//				if (url == null
//						|| mUrl.getText().toString().equalsIgnoreCase("")) {
//					mGo.setText("请输入网址");
//					mGo.setTextColor(0X6F0F0F0F);
//				} else {
//					mGo.setText("进入");
//					mGo.setTextColor(0X6F0000CD);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//		});

		mHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebView != null)
					mWebView.loadUrl(mHomeUrl);
			}
		});

		mExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			   finish();
			}
		});
	}

	boolean[] m_selected = new boolean[] { true, true, true, true, false,
			false, true };
	/**
	 * 响应长按点击事件
	 * @param
	 */
	private void setWebImageLongClickListener() {

		mWebView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				WebView.HitTestResult result = mWebView.getHitTestResult();
				if (result != null) {
					int type = result.getType();
					if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
						String longClickUrl = result.getExtra();
						showDialog(longClickUrl);
					}
				}
				return false;
			}
		});
	}

	/**
	 * 长按 WebView 图片弹出 Dialog
	 * @param url
	 */
	private void showDialog(final String url) {
		SweetAlertDialog dialog = new SweetAlertDialog(this);
		dialog.setContentText("");
		dialog.setTitleText("保存到相册");
		dialog.hidTtitle();
		dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				String fileName = "tu"+System.currentTimeMillis() + ".jpg";
				savePicture(fileName,url);
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();

	}
	String imgurl = "";


	//Glide保存图片
	public void savePicture(final String fileName, final String url){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			try {
			final	String filePath =  FilePathUtil.getFilePath(this,"PIG");
			final	String filename1 = filePath+ File.separator + fileName;


               SaveImageUtils.donwloadImg(BrowserActivity.this,url);

//				FileDownloader.getImpl().create(url)
//						.setPath(filename1)
//						.setListener(new FileDownloadListener() {
//							@Override
//							protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//							}
//
//							@Override
//							protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//
//							}
//
//							@Override
//							protected void completed(BaseDownloadTask task) {
//								dialog.dismiss();
//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										Toast.makeText(BrowserActivity.this, "图片已成功保存到"+filePath, Toast.LENGTH_SHORT).show();
//// 其次把文件插入到系统图库
//// 其次把文件插入到系统图库
//
//										try {
//											MediaStore.Images.Media.insertImage(BrowserActivity.this.getContentResolver(),
//													filename1, fileName, null);
//										} catch (FileNotFoundException e) {
//											e.printStackTrace();
//										}
//// 最后通知图库更新
//										BrowserActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//												Uri.fromFile(new File(filename1))));
//									}
//								});
//
//							}
//
//							@Override
//							protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//							}
//
//							@Override
//							protected void error(BaseDownloadTask task, Throwable e) {
//								dialog.dismiss();
//								Toast.makeText(BrowserActivity.this,"保存图片失败",Toast.LENGTH_LONG).show();
//
//
//							}
//
//							@Override
//							protected void warn(BaseDownloadTask task) {
//
//							}
//						})
//						.start();
			} catch (Exception e) {
				e.printStackTrace();
			}


		}else Toast.makeText(this, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
					changGoForwardButton(mWebView);
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
				+ ",resultCode:" + resultCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				if (null != uploadFile) {
					Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
					uploadFile.onReceiveValue(result);
					uploadFile = null;
				}
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (null != uploadFile) {
				uploadFile.onReceiveValue(null);
				uploadFile = null;
			}

		}

		if(selectPicDialog!=null){
			selectPicDialog.dismiss();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case PictureConfig.CHOOSE_REQUEST:
					// 图片选择结果回调
					selectList = PictureSelector.obtainMultipleResult(data);
					File imgUrl = new File(selectList.get(0).getPath());


					Luban.with(this)
							.load(imgUrl)                                   // 传人要压缩的图片列表
							.ignoreBy(100)                                  // 忽略不压缩图片的大小
							.setTargetDir(FilePathUtil.getFilePath(this,"yasuo"))                        // 设置压缩后文件存储位置
							.setCompressListener(new OnCompressListener() { //设置回调
								@Override
								public void onStart() {
									// TODO 压缩开始前调用，可以在方法内启动 loading UI
									dialog.show();

								}

								@Override
								public void onSuccess(File file) {
									// TODO 压缩成功后调用，返回压缩后的图片文件
									dialog.dismiss();

									if (file != null && file.exists()) {
										String filePath = file.getAbsolutePath();
										Uri result = Uri.fromFile(file);

										if (mUploadMsg != null) {
											mUploadMsg.onReceiveValue(Uri.parse(filePath));
											mUploadMsg = null;
										}
										if (mUploadMsgs != null) {
											mUploadMsgs.onReceiveValue(new Uri[]{result});
											mUploadMsgs = null;
										}
									}

								}

								@Override
								public void onError(Throwable e) {
									// TODO 当压缩过程出现问题时调用
								}
							}).launch();    //启动压缩


					if (imgUrl != null && imgUrl.exists()) {
						String filePath = imgUrl.getAbsolutePath();
						Uri result = Uri.fromFile(imgUrl);

						if (mUploadMsg != null) {
							mUploadMsg.onReceiveValue(Uri.parse(filePath));
							mUploadMsg = null;
						}
						if (mUploadMsgs != null) {
							mUploadMsgs.onReceiveValue(new Uri[]{result});
							mUploadMsgs = null;
						}
					}

					break;
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {
		if (mTestHandler != null)
			mTestHandler.removeCallbacksAndMessages(null);
		if (mWebView != null) {
			mWebView.clearCache(true);
			mWebView.clearFormData();
			mWebView.clearHistory();
			mWebView.destroy();
		}
		super.onDestroy();
	}

	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private int mCurrentUrl = mUrlStartNum;
	private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OPEN_TEST_URL:
				if (!mNeedTestPage) {
					return;
				}

				String testUrl = "file:///sdcard/outputHtml/html/"
						+ Integer.toString(mCurrentUrl) + ".html";
				if (mWebView != null) {
					mWebView.loadUrl(testUrl);
				}

				mCurrentUrl++;
				break;
			case MSG_INIT_UI:
				init();
				break;
			}
			super.handleMessage(msg);
		}
	};

}
