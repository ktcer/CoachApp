//package com.cn.aihuexpert.ui.patient.others.myaccount;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cn.coachs.R;
//import com.cn.coachs.http.FormFile;
//import com.cn.coachs.http.NetTool;
//import com.cn.coachs.model.nurse.BeanRegion;
//import com.cn.coachs.ui.basic.ActivityBasic;
//import com.cn.coachs.ui.chat.common.utils.ToastUtil;
//import com.cn.coachs.ui.patient.main.healthdiary.ActivitySearchHospital;
//import com.cn.coachs.ui.patient.others.myaccount.AscyncLogin;
//import com.cn.coachs.ui.patient.others.myaccount.GalleryImageLoader;
//import com.cn.coachs.ui.patient.others.myaccount.ProgressGenerator;
//import com.cn.coachs.ui.patient.others.myaccount.QueryregionList;
//import com.cn.coachs.ui.patient.setting.User;
//import com.cn.coachs.util.AbsParam;
//import com.cn.coachs.util.Constant;
//import com.cn.coachs.util.CreateFolder;
//import com.cn.coachs.util.FileUtils;
//import com.cn.coachs.util.Regex;
//import com.cn.coachs.util.RegexNumber;
//import com.cn.coachs.util.StringUtil;
//import com.cn.coachs.util.UtilsLogin;
//import com.cn.coachs.util.UtilsSharedData;
//import com.cn.coachs.util.customgallery.GalleryHelper;
//import com.cn.coachs.util.customgallery.model.BeanPhotoInfo;
//import com.cn.coachs.util.progressbutton.iml.ActionProcessButton;
//import com.cn.coachs.util.superdatepicker.CustomNumberPicker;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ActivityRegisterInfo extends ActivityBasic implements
//		OnClickListener {
//	private TextView titleText;
//	private EditText etName, etGoodat, etPosition, etIntroduction, etCommand;
//	private EditText etHospital;
//	private CustomNumberPicker etSection;
//	private ActionProcessButton register_btn;
//	private ImageView ivName, ivGoodat, ivIntroduction, ivPosition, iv_photo1,
//			 ivCommand, ivHospital;
//	// 点击的添加图片类型
//	private static byte clickPicType = 0;
//	private static final int IDENTIFY_PHOTO = 1;
//	private static final int CREDIT_PHOTO = 2;
//	private static final int NORMAL_PHOTO = 3;
//	private String commomandTel = "";
//	// 上传头像的地址map
//	HashMap<String, String> picAddr = new HashMap<String, String>();
//	/** mac地址 */
//	private static String macAddress;
//	/** 获取验证码接口 */
//	/** 注册使用的电话号码 */
//	private static String telephonenum;
//	/** 注册时获取的验证码 */
//	private static String identifyingCode;
//	/** 输入的密码 */
//	private static String password1;
//	private int RegisterResultBack;
//	private String RegisterDetailBack;
//	private Map<String, String> map;
//
//	private String introduction = "";
//	private String gender = "男";
//	private String name = "";
//	private String hospital = "";
//	private String goodat = "";
//	private String position = "";
//	private long selectedSectionId;
//	private List<BeanRegion> RegionList = new ArrayList<BeanRegion>();
//	private ProgressGenerator progressGenerator;
//	/** 后台返回的数据 */
//	private String retutrnStr;
//	private Message msg;
//	private static final int FailToRegister = 2;
//	private static final int SuccessToResgister = 3;
//	private Regex reGexNumber;
//	/** 用户类型，0表示患者 */
//	private static final String userType = "1";
//
//	// /*上传照片*/
////	private GridView gridView; // 网格显示缩略图
////
////	private GridAdapter adapter; // 适配器
//
//	private Handler mHandler = new Handler();
//	private ScrollView scrolllview;
//
//	/**
//	 * 生活照存储的list
//	 */
////	public static List<BeanPhotoInfo> photoInfoList = new ArrayList<BeanPhotoInfo>();
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
//		// |
//		// WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		setContentView(R.layout.activity_registerinfo);
//		setdata();
//		getData();
//		initial();
//	}
//
//	private void setdata() {
//		UtilsSharedData.initDataShare(this);
//	}
//
//	private void getData() {
//		telephonenum = getIntent().getStringExtra(Constant.USER_ACCOUNT);
//		password1 = getIntent().getStringExtra(Constant.USER_PASS);
//		identifyingCode = getIntent().getStringExtra(Constant.USER_IDENTIFY);
//
//		// 创建文件夹
//		CreateFolder.createRegisterFolder(telephonenum);
//	}
//
//	private void initial() {
//		reGexNumber = new RegexNumber();
//		titleText = (TextView) findViewById(R.id.middle_tv);
//		titleText.setText("填写信息");
//		ivName = (ImageView) findViewById(R.id.delete_Name);
//		scrolllview = (ScrollView) findViewById(R.id.scrollview);
//		ivGoodat = (ImageView) findViewById(R.id.delete_goodat);
//		ivPosition = (ImageView) findViewById(R.id.delete_position);
//		ivIntroduction = (ImageView) findViewById(R.id.delete_introduction);
//		ivHospital = (ImageView) findViewById(R.id.delete_hospital);
//		ivCommand = (ImageView) findViewById(R.id.delete_commod);
//		iv_photo1 = (ImageView) findViewById(R.id.image_certification);
////		iv_photo2 = (ImageView) findViewById(R.id.image_certification1);
//
//		etName = (EditText) findViewById(R.id.register_name);
//		etGoodat = (EditText) findViewById(R.id.register_goodat);
//		etPosition = (EditText) findViewById(R.id.register_cardnum);
//		etIntroduction = (EditText) findViewById(R.id.register_introduction);
//		etCommand = (EditText) findViewById(R.id.register_commod);
//		etHospital = (EditText) findViewById(R.id.register_hospital);
//		etHospital.setClickable(true);
//		etHospital.setOnClickListener(this);
//
//		etClick();
////		GridView();
//		register_btn = (ActionProcessButton) findViewById(R.id.submit_Register);
//		register_btn.setOnClickListener(this);
//		register_btn.setMode(ActionProcessButton.Mode.ENDLESS);
//		progressGenerator = new ProgressGenerator(register_btn);
//		ivName.setOnClickListener(this);
//		ivGoodat.setOnClickListener(this);
//		ivPosition.setOnClickListener(this);
//		ivIntroduction.setOnClickListener(this);
//		ivCommand.setOnClickListener(this);
//		iv_photo1.setOnClickListener(this);
////		iv_photo2.setOnClickListener(this);
//		etSection = (CustomNumberPicker) findViewById(R.id.register_section);
//
//		spinner();
//		// 根据ID找到RadioGroup实例
//		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
//		// 绑定一个匿名监听器
//		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//				// 获取变更后的选中项的ID
//				int radioButtonId = arg0.getCheckedRadioButtonId();
//				// 根据ID获取RadioButton的实例
//				RadioButton rb = (RadioButton) ActivityRegisterInfo.this
//						.findViewById(radioButtonId);
//				// 更新文本内容，以符合选中项
//				gender = rb.getText().toString();
//			}
//		});
//
//	}
//
//	/**
//	 * @author kuangtiecheng 选择地区和医院的选择项
//	 */
//	private void spinner() {
//		QueryregionList task = new QueryregionList() {
//
//			@Override
//			protected void onPostExecute(List<BeanRegion> result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				RegionList.addAll(result);
//				List<String> arrayList = new ArrayList<String>();
//				for (BeanRegion bean : regionList) {
//					arrayList.add(bean.getRegionName());
//				}
//				etSection.setList(arrayList);
//				etSection.setTips("请选择您所在地区");
//			}
//
//		};
//		task.execute();
//	}
//
////	private void GridView() {
////		gridView = (GridView) findViewById(R.id.photo1);
////		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
////		adapter = new GridAdapter(this);
////		gridView.setAdapter(adapter);
////		gridView.setOnItemClickListener(new OnItemClickListener() {
////
////			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
////					long arg3) {
////				if (arg2 == photoInfoList.size()) {
////					clickPicType = NORMAL_PHOTO;
////					GalleryHelper.openGalleryMuti(ActivityRegisterInfo.this, 6,
////							new GalleryImageLoader());
////				} else {
////					Intent intent = new Intent(ActivityRegisterInfo.this,
////							PhotoActivity.class);
////					intent.putExtra("ID", arg2);
////					startActivity(intent);
////				}
////			}
////		});
////	}
//
//	private void judgeInfo() {
//		name = etName.getText().toString();
//		goodat = etGoodat.getText().toString();
//		// hospital = etHospital.getText().toString();
//		String selectedSection = etSection.getText().toString();
//		for (BeanRegion bean : RegionList) {
//			if (bean.getRegionName().equals(selectedSection)) {
//				selectedSectionId = bean.getRegionID();
//				break;
//			}
//		}
//		position = etPosition.getText().toString();
//		introduction = etIntroduction.getText().toString();
//		commomandTel = etCommand.getText().toString();
//
//		if (!name.equals("") & !gender.equals("") & !selectedSection.equals("")
//				& !hospital.equals("") & !position.equals("")
//				& !goodat.equals("")) {
//			setInputEnabled(false);
//			progressGenerator.start();
//			new Thread(runnableRegister).start();
//		} else {
//			showToastDialog("请填写所有信息");
//		}
//	}
//
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
////		if (photoInfoList != null) {
////			// 多选的情况
////			adapter.notifyDataSetChanged();
////		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.submit_Register:
//			judgeInfo();
//			/** 提交注册信息 */
//			break;
//
//		case R.id.delete_Login:
//			etName.setText("");
//			break;
//		case R.id.delete_goodat:
//			etGoodat.setText("");
//			break;
//		case R.id.delete_position:
//			etPosition.setText("");
//			break;
//		case R.id.delete_introduction:
//			etIntroduction.setText("");
//			break;
//		case R.id.delete_commod:
//			etCommand.setText("");
//			break;
//		case R.id.image_certification:
//			clickPicType = IDENTIFY_PHOTO;
//			GalleryHelper.openGallerySingle(ActivityRegisterInfo.this, true,
//					new GalleryImageLoader());
//			break;
//		case R.id.image_certification1:
//			clickPicType = CREDIT_PHOTO;
//			GalleryHelper.openGallerySingle(ActivityRegisterInfo.this, true,
//					new GalleryImageLoader());
//			break;
//		case R.id.register_hospital:
//			Intent intent = new Intent(ActivityRegisterInfo.this,
//					ActivitySearchHospital.class);
//			intent.putExtra(Constant.HOSPITAL_EDIT, false);
//			startActivityForResult(intent, Constant.HOSPITAL_RESULT_CODE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	// public void onActivityResult(int requestCode, int resultCode, Intent
//	// data){
//	// switch (resultCode) {
//	// case RESULT_OK:
//	// if (requestCode == Constant.HOSPITAL_RESULT_CODE){
//	// String hospitalName = data.getStringExtra(Constant.HOSPITAL_NAME);
//	// String hospitalId = data.getStringExtra(Constant.HOSPITAL_ID);
//	// System.out.println("=-=-=RESULT_CODE_SUCCESS=-=-="+hospitalName+": "+hospitalId);
//	// }
//	// break;
//	// default:
//	// break;
//	// }
//	// }
//
//	/**********************************************************************/
//	public void etClick() {
//		etName.setFocusable(true);
//		etGoodat.setFocusable(true);
//		etPosition.setFocusable(true);
//		etIntroduction.setFocusable(true);
//		etCommand.setFocusable(true);
//		etHospital.setFocusable(true);
//		etName.addTextChangedListener(new TextWatcher() {
//			private CharSequence temp;
//			private int editStart;
//			private int editEnd, count;
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				temp = arg0;
//				count = arg3;
//				if (arg0.toString().equals("")) {
//					ivName.setVisibility(View.GONE);
//				} else {
//					ivName.setVisibility(View.VISIBLE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				// ivName.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				ivName.setVisibility(View.GONE);
//				editStart = etName.getSelectionStart();
//				editEnd = etName.getSelectionEnd();
//				String name1 = temp.toString();
//				if (StringUtil.isEmpty(name1)) {
//					ToastUtil.showMessage("输入不能为空");
//				} else {
//					if (StringUtil.isName(name1)) {
//					} else {
//						arg0.delete(editEnd - count, editEnd);
//						etName.setText(arg0);
//						etName.setSelection(arg0.toString().length());
//						ToastUtil.showMessage("请输入1-15位字母或数字或者中文或三者组合,请重新输入");
//					}
//
//				}
//			}
//		});
//		etGoodat.addTextChangedListener(new TextWatcher() {
//			private int cursorPos = 0;
//			// //输入表情前EditText中的文本
//			private String tmp;
//			// 是否重置了EditText的内容
//			private boolean resetText = false;
//			private int editStart;
//			private int editEnd;
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				editEnd = etGoodat.getSelectionEnd();
//				if (!resetText) {
//					if (arg3 >= 0) {// 表情符号的字符长度最小为3
//						// 提取输入的长度大于3的文本
//						CharSequence input = arg0.subSequence(editEnd - arg3,
//								editEnd);
//						// 正则匹配是否是表情符号
//						String in = input.toString();
//						if (!StringUtil.isText(in)) {
//							resetText = true;
//							// 是表情符号就将文本还原为输入表情符号之前的内容
//							etGoodat.setText(tmp);
//							etGoodat.setSelection(tmp.length());
//							Toast.makeText(ActivityRegisterInfo.this, "不支持输入",
//									Toast.LENGTH_SHORT).show();
//
//						}
//					}
//				} else {
//					resetText = false;
//				}
//				// TODO Auto-generated method stub
//				if (arg0.toString().equals("")) {
//					ivGoodat.setVisibility(View.GONE);
//				} else {
//					ivGoodat.setVisibility(View.VISIBLE);
//					// 这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
//					mHandler.postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							// 将ScrollView滚动到底
//							// scrolllview.fullScroll(View.FOCUS_DOWN);
//						}
//					}, 100);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				if (!resetText) {
//					cursorPos = etGoodat.getSelectionEnd();
//					tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
//				}
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				ivGoodat.setVisibility(View.GONE);
//			}
//		});
//		etPosition.addTextChangedListener(new TextWatcher() {
//			private int cursorPos = 0;
//			// //输入表情前EditText中的文本
//			private String tmp;
//			// 是否重置了EditText的内容
//			private boolean resetText = false;
//			private int editStart;
//			private int editEnd;
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				editEnd = etPosition.getSelectionEnd();
//				if (!resetText) {
//					if (arg3 >= 0) {// 表情符号的字符长度最小为3
//						// 提取输入的长度大于3的文本
//						CharSequence input = arg0.subSequence(editEnd - arg3,
//								editEnd);
//						// 正则匹配是否是表情符号
//						String in = input.toString();
//						if (!StringUtil.isText(in)) {
//							resetText = true;
//							// 是表情符号就将文本还原为输入表情符号之前的内容
//							etPosition.setText(tmp);
//							etPosition.setSelection(tmp.length());
//							Toast.makeText(ActivityRegisterInfo.this, "不支持输入",
//									Toast.LENGTH_SHORT).show();
//
//						}
//					}
//				} else {
//					resetText = false;
//				}
//				if (arg0.toString().equals("")) {
//					ivPosition.setVisibility(View.GONE);
//				} else {
//					ivPosition.setVisibility(View.VISIBLE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				if (!resetText) {
//					cursorPos = etPosition.getSelectionEnd();
//					tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
//				}
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				ivPosition.setVisibility(View.GONE);
//			}
//		});
//		etIntroduction.addTextChangedListener(new TextWatcher() {
//			private int cursorPos = 0;
//			// //输入表情前EditText中的文本
//			private String tmp;
//			// 是否重置了EditText的内容
//			private boolean resetText = false;
//			private int editStart;
//			private int editEnd;
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				editEnd = etIntroduction.getSelectionEnd();
//				if (!resetText) {
//					if (arg3 >= 0) {// 表情符号的字符长度最小为3
//						// 提取输入的长度大于3的文本
//						CharSequence input = arg0.subSequence(editEnd - arg3,
//								editEnd);
//						// 正则匹配是否是表情符号
//						String in = input.toString();
//						if (!StringUtil.isText(in)) {
//							resetText = true;
//							// 是表情符号就将文本还原为输入表情符号之前的内容
//							etIntroduction.setText(tmp);
//							etIntroduction.setSelection(tmp.length());
//							Toast.makeText(ActivityRegisterInfo.this, "不支持输入",
//									Toast.LENGTH_SHORT).show();
//
//						}
//					}
//				} else {
//					resetText = false;
//				}
//				if (arg0.toString().equals("")) {
//					ivIntroduction.setVisibility(View.GONE);
//				} else {
//					ivIntroduction.setVisibility(View.VISIBLE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				if (!resetText) {
//					cursorPos = etIntroduction.getSelectionEnd();
//					tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
//				}
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				ivIntroduction.setVisibility(View.GONE);
//			}
//		});
//		etCommand.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				if (arg0.toString().equals("")) {
//					ivCommand.setVisibility(View.GONE);
//				} else {
//					ivCommand.setVisibility(View.VISIBLE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				ivCommand.setVisibility(View.GONE);
//				int length = etCommand.getText().toString().length();
//				if (length > 11) {
//					int editEnd = etCommand.getSelectionEnd();
//					arg0.delete(length - 1, editEnd);
//				}
//			}
//		});
//		// etHospital.addTextChangedListener(new TextWatcher() {
//		// private int cursorPos = 0;
//		// // //输入表情前EditText中的文本
//		// private String tmp;
//		// //是否重置了EditText的内容
//		// private boolean resetText = false;
//		// private int editStart ;
//		// private int editEnd ;
//		// @Override
//		// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//		// int arg3) {
//		// editEnd = etHospital.getSelectionEnd();
//		// if(!resetText){
//		// if(arg3 >= 0){//表情符号的字符长度最小为3
//		// //提取输入的长度大于3的文本
//		// CharSequence input = arg0.subSequence(editEnd-arg3, editEnd);
//		// //正则匹配是否是表情符号
//		// String in = input.toString();
//		// if(!StringUtil.isText(in)){
//		// resetText = true;
//		// //是表情符号就将文本还原为输入表情符号之前的内容
//		// etHospital.setText(tmp);
//		// etHospital.setSelection(tmp.length());
//		// Toast.makeText(ActivityRegisterInfo.this,
//		// "不支持输入", Toast.LENGTH_SHORT).show();
//		//
//		// }
//		// }
//		// }else{
//		// resetText = false;
//		// }
//		// if (arg0.toString().equals("")) {
//		// ivHospital.setVisibility(View.GONE);
//		// } else {
//		// ivHospital.setVisibility(View.VISIBLE);
//		// }
//		// }
//		//
//		// @Override
//		// public void beforeTextChanged(CharSequence arg0, int arg1,
//		// int arg2, int arg3) {
//		// if(!resetText){
//		// cursorPos = etHospital.getSelectionEnd();
//		// tmp =
//		// arg0.toString();//这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
//		// }
//		//
//		// }
//		//
//		// @Override
//		// public void afterTextChanged(Editable arg0) {
//		// ivHospital.setVisibility(View.GONE);
//		// }
//		// });
//	}
//
//	public boolean isNumeric(String str) {
//		for (int i = str.length(); --i >= 0;) {
//			if (!Character.isDigit(str.charAt(i))) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	Runnable runnableRegister = new Runnable() {
////		ArrayList<FormFile> picFiles;
//
//		@Override
//		public void run() {
//			String url = AbsParam.getBaseUrl() + "/base/app/edituserinfo";
////			for (int i = 0; i < photoInfoList.size(); i++) {
////				picAddr.put("shz0" + i, photoInfoList.get(i).getPhotoPath());
////			}
//			// 高清的压缩图片全部就在 list 路径里面了
//			// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
//			// 将图片文件都打开
////			picFiles = new ArrayList<FormFile>();
////			Iterator iter = picAddr.entrySet().iterator();
////			while (iter.hasNext()) {
////				Map.Entry entry = (Map.Entry) iter.next();
////				Object key = entry.getKey();
////				Object val = entry.getValue();
////				picFiles.add(new FormFile(key.toString(), new File(val
////						.toString()), key.toString(), null));
////			}
//
//			map = new HashMap<String, String>();
//			param.put("id", UtilsSharedData.getValueByKey(Constant.USER_ID));
//			param.put("name", name);
//			param.put("programType", programType);
//			param.put("resume", resume);
//			param.put("rewards", rewards);
//			param.put("sex", sex);
//			param.put("workAddress", workAddress);
//
//			map.put("telephoneNum", telephonenum);
//			map.put("password", password1);
//			map.put("identifyingCode", identifyingCode);
//			map.put("userType", userType);
//			map.put("videoNumber", "0");
//			map.put("inviter", commomandTel);
//			map.put("terminal", "ANDROID");
//			map.put("name", name);
//			map.put("scbz", goodat);
//			map.put("position", position);
//			map.put("gender", gender.equals("男") ? "00070002" : "00070003");
//			map.put("hospital", hospital);
//			map.put("regionId", selectedSectionId + "");
//			map.put("grjj", introduction);
//			try {
//
////				int size = picFiles.size();
//				retutrnStr = NetTool.post(url, map,
//						(FormFile[]) picFiles.toArray(new FormFile[size]));// uploadMultipleFiles(url,
//																			// map,
//																			// picFiles);
//																			// retutrnStr=NetTool.sendHttpClientPost(url,
//																			// map,
//				UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "1");														// "utf-8");
//				JSONObject RegisterResult = new JSONObject(retutrnStr);
//				RegisterResultBack = RegisterResult.getInt("resultID");
//				RegisterDetailBack = RegisterResult.getString("detail");
//				msg = new Message();
//				if (RegisterResultBack == 0) {
//					msg.what = FailToRegister;
//				} else if (RegisterResultBack == 1) {
//					msg.what = SuccessToResgister;
//				}
//				RegisterHandler.sendMessage(msg);
//			} catch (Exception e) {
//				// TODO: handle exception
//				msg = new Message();
//				msg.what = FailToRegister;
//				RegisterHandler.sendMessage(msg);
//			}
//		}
//	};
//	private Handler RegisterHandler = new Handler() {
//		public void handleMessage(Message m) {
//			switch (m.what) {
//			case FailToRegister:
//				Toast.makeText(getApplicationContext(), RegisterDetailBack,Toast.LENGTH_LONG)
//						.show();
//				setInputEnabled(true);
//				break;
//			case SuccessToResgister:
//				// 完成上传服务器后 .........
//				FileUtils.deleteDir();
//				// 清除生活照列表中已经加载的照片
//				Toast.makeText(getApplicationContext(), RegisterDetailBack,Toast.LENGTH_LONG)
//						.show();
//				macAddress = getLocalMacAddress();
//				List<User> userList = UtilsLogin
//						.getUserList(ActivityRegisterInfo.this);
//				User newUser = new User(telephonenum, password1, 0);
//				ArrayList<User> tempList = new ArrayList<User>();
//				tempList.add(newUser);
//				int i = 1;
//				for (User user : userList) {
//					user.setAddOrder(i);
//					tempList.add(user);
//					i++;
//				}
//				try {
//					UtilsLogin
//							.saveUserList(ActivityRegisterInfo.this, tempList);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				AscyncLogin async = new AscyncLogin(ActivityRegisterInfo.this,
//						telephonenum, password1, macAddress);
//				async.execute();
//				break;
//			default:
//				setInputEnabled(true);
//				break;
//			}
//		}
//	};
//
//	/**
//	 * 设置界面是否使能输入
//	 *
//	 * @param enabled
//	 */
//
//	@Override
//	public void setInputEnabled(boolean enabled) {
//		// TODO Auto-generated method stub
//		super.setInputEnabled(enabled);
//		if (enabled) {
//			progressGenerator.stop();
//			etName.setEnabled(true);
//			etGoodat.setEnabled(true);
//			etPosition.setEnabled(true);
//			etIntroduction.setEnabled(true);
//			etCommand.setEnabled(true);
//			etHospital.setEnabled(true);
//			etSection.setEnabled(true);
//			iv_photo1.setEnabled(true);
////			iv_photo2.setEnabled(true);
////			gridView.setEnabled(true);
//			register_btn.setEnabled(true);
//		} else {
//			etName.setEnabled(false);
//			etGoodat.setEnabled(false);
//			etPosition.setEnabled(false);
//			etIntroduction.setEnabled(false);
//			etCommand.setEnabled(false);
//			etHospital.setEnabled(false);
//			etSection.setEnabled(false);
//			iv_photo1.setEnabled(false);
////			iv_photo2.setEnabled(false);
////			gridView.setEnabled(false);
//			register_btn.setEnabled(false);
//		}
//	}
//
//	public String getLocalMacAddress() {
//		String result = "";
//		String Mac = "";
//		result = callCmd("busybox ifconfig", "HWaddr");
//
//		if (result == null) {
//			return "网络出错，请检查网络";
//		}
//		if (result.length() > 0 && result.contains("HWaddr")) {
//			Mac = result.substring(result.indexOf("HWaddr") + 6,
//					result.length() - 1);
//			if (Mac.length() > 1) {
//				result = Mac.toLowerCase();
//			}
//		}
//		return result.trim();
//	}
//
//	private String callCmd(String cmd, String filter) {
//		String result = "";
//		String line = "";
//		try {
//			Process proc = Runtime.getRuntime().exec(cmd);
//			InputStreamReader is = new InputStreamReader(proc.getInputStream());
//			BufferedReader br = new BufferedReader(is);
//
//			// 执行命令cmd，只取结果中含有filter的这一行
//			while ((line = br.readLine()) != null
//					&& line.contains(filter) == false) {
//				// result += line;
//				Log.i("test", "line: " + line);
//			}
//
//			result = line;
//			Log.i("test", "result: " + result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	/*
//	 * 选择生活照adapter
//	 */
//
////	@SuppressLint("HandlerLeak")
////	public class GridAdapter extends BaseAdapter {
////		private LayoutInflater inflater; // 视图容器
////		private int selectedPosition = -1;// 选中的位置
////		private boolean shape;
////
////		public boolean isShape() {
////			return shape;
////		}
////
////		public void setShape(boolean shape) {
////			this.shape = shape;
////		}
////
////		public GridAdapter(Context context) {
////			inflater = LayoutInflater.from(context);
////		}
////
////		// public void update() {
////		// loading();
////		// }
////
////		public int getCount() {
////			return (photoInfoList.size() + 1);
////		}
////
////		public Object getItem(int arg0) {
////
////			return null;
////		}
////
////		public long getItemId(int arg0) {
////
////			return 0;
////		}
////
////		public void setSelectedPosition(int position) {
////			selectedPosition = position;
////		}
////
////		public int getSelectedPosition() {
////			return selectedPosition;
////		}
////
////		/**
////		 * ListView Item设置
////		 */
////		public View getView(int position, View convertView, ViewGroup parent) {
////			final int coord = position;
////			ViewHolder holder = null;
////			if (convertView == null) {
////
////				convertView = inflater.inflate(R.layout.item_published_grida,
////						parent, false);
////				holder = new ViewHolder();
////				holder.image = (ImageView) convertView
////						.findViewById(R.id.item_grida_image);
////				convertView.setTag(holder);
////			} else {
////				holder = (ViewHolder) convertView.getTag();
////			}
////
//////			if (position == photoInfoList.size()) {
//////				holder.image.setImageResource(R.drawable.bt_add_pic);
//////				if (position == 6) {
//////					holder.image.setVisibility(View.GONE);
//////				}
//////			} else {
//////				ImageLoader.getInstance().displayImage(
//////						"file:/" + photoInfoList.get(position).getPhotoPath(),
//////						holder.image,
//////						AppMain.initImageOptions(R.drawable.default_pic, true));
//////			}
////
////			return convertView;
////		}
////
////		public class ViewHolder {
////			public ImageView image;
////		}
////
////	}
//
//	/**
//	 * 判断需要将资格证还是证件照给加入到图片数组中
//	 */
//	private void addToMapAndSetPic(String path) {
//		switch (clickPicType) {
//		case IDENTIFY_PHOTO:
//			ImageLoader.getInstance().displayImage("file:/" + path, iv_photo1);
//			picAddr.put("zgzsImgUrl", path);
//			break;
//		case CREDIT_PHOTO:
////			ImageLoader.getInstance().displayImage("file:/" + path, iv_photo2);
////			picAddr.put("zjz", path);
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
//			if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
//				BeanPhotoInfo photoInfo = data
//						.getParcelableExtra(GalleryHelper.RESULT_DATA);
////				if (clickPicType == NORMAL_PHOTO) {
////					photoInfoList = (List<BeanPhotoInfo>) data
////							.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);
////				}
//				if (photoInfo != null) {
//					// 将照片加入到map中。并让其显示出来。
//					addToMapAndSetPic(photoInfo.getPhotoPath());
//				}
//
////				if (photoInfoList != null) {
////					// 多选的情况
////					adapter.notifyDataSetChanged();
////				}
//			}
//		} else if (resultCode == RESULT_OK
//				&& requestCode == Constant.HOSPITAL_RESULT_CODE) {
//			hospital = data.getStringExtra(Constant.HOSPITAL_NAME);
//			etHospital.setText(hospital);
//			String hospitalId = data.getStringExtra(Constant.HOSPITAL_ID);
//			System.out.println("=-=-=RESULT_CODE_SUCCESS=-=-=" + hospital
//					+ ": " + hospitalId);
//		}
//
//	}
//
//}
