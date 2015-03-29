package com.example.almin.fragment.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.almin.MyApplication;
import com.example.almin.R;
import com.example.almin.activity.MainActivity;
import com.example.almin.dialog.MyAlertDialog;
import com.example.almin.fragment.AbstractFragment;
import com.example.almin.library.model.User;
import com.example.almin.listener.UpdateLocalUserListener;
import com.example.almin.utils.ImageTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonalSettingFragment extends AbstractFragment implements UpdateLocalUserListener{
	private final static String MY_SETTING_TAG = "��������";
	private final static String USER_TYPR_ADMIN = "����Ա";
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private static final int SCALE = 5;//��Ƭ��С����
	private String[] mDepartments;
	private ViewGroup mRootView;
//	private Bitmap mBitmap;
	private RelativeLayout mRlAvatar,mRlUpdateMyInfo,mRlLogOut,mRlAdminAssets;
	private ImageView mIvAvatar;
	private TextView mTvName,mTvDepartment,mTvPosition,mTvUserType;
	
	public PersonalSettingFragment getPersonalSettingFragment(){
		return new PersonalSettingFragment();
	}

	@Override
	public String getFragmentTag() {
		return "PersonalSettingFragment";
	}
	
	@Override
	protected String getActionBarTitle() {
		return MY_SETTING_TAG;
	}
	
	@Override
	protected View onTheAppCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {
		mRootView = (ViewGroup)layoutInflater.inflate(R.layout.fragment_personal_setting, container, false);
		mDepartments = getResources().getStringArray(R.array.spinner_department);
		
		User user = MyApplication.getInstance().getUser();
		mRlAvatar = (RelativeLayout) mRootView.findViewById(R.id.rl_my_avatar);
		mRlUpdateMyInfo = (RelativeLayout) mRootView.findViewById(R.id.rl_update_my_info);
		mRlLogOut = (RelativeLayout) mRootView.findViewById(R.id.rl_login_out);
		mRlAdminAssets = (RelativeLayout) mRootView.findViewById(R.id.rl_admin_my_assets);
		mTvName = (TextView) mRootView.findViewById(R.id.tv_name);
		mTvDepartment = (TextView) mRootView.findViewById(R.id.tv_department);
		mTvPosition = (TextView) mRootView.findViewById(R.id.tv_position);
		mTvUserType = (TextView) mRootView.findViewById(R.id.tv_user_type);
		mRlAdminAssets.setVisibility(user.getUsertype().equalsIgnoreCase(USER_TYPR_ADMIN)?View.VISIBLE:View.GONE);
		mRlAdminAssets.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.navigateToPersonalAssetsFragment();
			}
		});
		updateLocalUser();
		
		mIvAvatar = (ImageView) mRootView.findViewById(R.id.iv_my_avatar);
		displayAvatarFromCache();
		
		mIvAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPicturePicker(getActivity());
			}
		});
		mRlAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPicturePicker(getActivity());
			}
		});
		mRlUpdateMyInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.navigateToUpdateMyInfoFragment(PersonalSettingFragment.this);
			}
		});
		mRlLogOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getActivity()!=null){
					new MyAlertDialog(getActivity(), "�Ƿ��˳�", "ȷ��", "ȡ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getActivity().finish();
						}
					}, null);
				}
			}
		});
		return mRootView;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SdCardPath")
	private void displayAvatarFromCache() {
		ImageLoader.getInstance().displayImage("file:///mnt/"+(MyApplication.isHaveSDcard()?"/sdcard":"")+MyApplication.AVATAR_PATH, 
				mIvAvatar, new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true).build());		
	}

	public void showPicturePicker(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ͼƬ��Դ");
		builder.setNegativeButton("ȡ��", null);
		builder.setItems(new String[]{"����","���"}, new DialogInterface.OnClickListener() {
			//������
			int REQUEST_CODE;
			
			@SuppressWarnings("deprecation")
			@SuppressLint("WorldWriteableFiles")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					REQUEST_CODE = CROP;
					//ɾ����һ�ν�ͼ����ʱ�ļ�
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
					ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

					//���汾�ν�ͼ��ʱ�ļ�����
					fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
					Editor editor = sharedPreferences.edit();
					editor.putString("tempName", fileName);
					editor.commit();
					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					//ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;

				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					REQUEST_CODE = CROP;
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(openAlbumIntent, REQUEST_CODE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}

	//��ȡͼƬ
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
		Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", outputX);   
        intent.putExtra("outputY", outputY); 
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);  
	    startActivityForResult(intent, requestCode);
	}
	
	@SuppressLint("WorldWriteableFiles")
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(getActivity()!=null){
			if (resultCode == getActivity().RESULT_OK) {
				switch (requestCode) {
				case TAKE_PICTURE:
					//�������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
					//����Bitmap�ڴ�ռ�ýϴ�������Ҫ�����ڴ棬����ᱨout of memory�쳣
					bitmap.recycle();
					
					//���������ͼƬ��ʾ�ڽ����ϣ������浽����
//					mIvAvatar.setImageBitmap(newBitmap);
					updateAvatar(newBitmap);
//					ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
					break;

				case CHOOSE_PICTURE:
					ContentResolver resolver = getActivity().getContentResolver();
					//��Ƭ��ԭʼ��Դ��ַ
					Uri originalUri = data.getData(); 
		            try {
		            	//ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						if (photo != null) {
							//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
							Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
							//�ͷ�ԭʼͼƬռ�õ��ڴ棬��ֹout of memory�쳣����
							photo.recycle();
							
//							mIvAvatar.setImageBitmap(smallBitmap);
							updateAvatar(smallBitmap);
						}
					} catch (FileNotFoundException e) {
					    e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
				case CROP:
					Uri uri = null;
					if (data != null) {
						uri = data.getData();
						System.out.println("Data");
					}else {
						System.out.println("File");
						String fileName = getActivity().getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
						uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					}
					cropImage(uri, 500, 500, CROP_PICTURE);
					break;
				
				case CROP_PICTURE:
					Bitmap photo = null;
					Uri photoUri = data.getData();
					if (photoUri != null) {
						photo = BitmapFactory.decodeFile(photoUri.getPath());
					}
					if (photo == null) {
						Bundle extra = data.getExtras();
						if (extra != null) {
			                photo = (Bitmap)extra.get("data");  
			                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
			                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			            }  
					}
//					mIvAvatar.setImageBitmap(photo);
					updateAvatar(photo);
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void updateLocalUser() {
		User user = MyApplication.getInstance().getUser();
		mTvName.setText(user.getName());
		mTvDepartment.setText(mDepartments[Integer.parseInt(user.getDepartment())]);
		mTvPosition.setText(user.getPosition());
		mTvUserType.setText(user.getUsertype());
	}

	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		for (int i = 0; i < menu.size(); i++) {
//			menu.removeGroup(i);
//		}
//	}
	
	
	
	
	//����ͷ����º��ٰ�ͷ���bitmap�ó��������ļ� 
	@SuppressLint("SdCardPath")
	public void updateAvatar(Bitmap photo){
		String fileName = "myavatar.jpg";
		String avatarPath="";
		mIvAvatar.setImageBitmap(photo);
		//�ж�SD���Ƿ����
		if(MyApplication.isHaveSDcard()) {
			avatarPath = MyApplication.saveImage("/sdcard"+MyApplication.AVATAR_DIR_PATH,fileName,photo,null,75,true);
		}else{
			avatarPath = MyApplication.saveImage(MyApplication.AVATAR_DIR_PATH,fileName,photo,null,75,true);
		}
		photo=null;
		System.out.println(avatarPath);
	}

//	public void saveMyBitmap(String bitName,Bitmap bitmap,Boolean sdcardExit) throws IOException {          
//		String saveDirPath = (sdcardExit?"/sdcard/AssetsService/AvatarTemp/":"/AssetsService/AvatarTemp/");
//		File pic = new File(saveDirPath + bitName + ".png");  
//		if(pic.exists()){        //���F������ͬ���ļ����־Ͱ���ɾ����û�еĻ����½���
//			pic.delete();
//        	System.out.println("Delete pic temp");
//        }else{
//        	pic.createNewFile();  
//        }
//        
//		FileOutputStream fOut = null;  
//		try {  
//			fOut = new FileOutputStream(pic);  
//		} catch (FileNotFoundException e) {  
//			e.printStackTrace();  
//		}  
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);  
//		try {  
//			fOut.flush();  
//		} catch (IOException e) {  
//			e.printStackTrace();  
//		}  
//		try {  
//			fOut.close();  
//		} catch (IOException e) {  
//			e.printStackTrace();  
//		}  
//	}  
	
	@Override
	public void onDestroyView() {
//		mBitmap = null;
		super.onDestroyView();
	}
}
