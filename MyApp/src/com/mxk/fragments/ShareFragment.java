package com.mxk.fragments;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.moments.WechatMoments.ShareParams;

import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ShareFragment extends MyBaseFragment {
	private Button button1, button2, button3, button4, button5, button6,
			button7, button8, button9;
	private MyOnClickListener clickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.share_fragment, container,
				false);
		// txtCopyRight = (TextView) rootView.findViewById(R.id.txt_copy_right);
		button1 = (Button) rootView.findViewById(R.id.btn1);
		button2 = (Button) rootView.findViewById(R.id.btn2);
		button3 = (Button) rootView.findViewById(R.id.btn3);
		button4 = (Button) rootView.findViewById(R.id.btn4);
		button5 = (Button) rootView.findViewById(R.id.btn5);
		button6 = (Button) rootView.findViewById(R.id.btn6);
		button7 = (Button) rootView.findViewById(R.id.btn7);
		button8 = (Button) rootView.findViewById(R.id.btn8);
		button9 = (Button) rootView.findViewById(R.id.btn9);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ShareSDK.initSDK(getActivity());
		clickListener = new MyOnClickListener();
		button1.setOnClickListener(clickListener);
		button2.setOnClickListener(clickListener);
		button3.setOnClickListener(clickListener);
		button4.setOnClickListener(clickListener);
		button5.setOnClickListener(clickListener);
		button6.setOnClickListener(clickListener);
		button7.setOnClickListener(clickListener);
		button8.setOnClickListener(clickListener);
		button9.setOnClickListener(clickListener);
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			OnekeyShare oks = new OnekeyShare();
			switch (v.getId()) {
			case R.id.btn1:
				// 关闭sso授权
				oks.disableSSOWhenAuthorize();

				// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				oks.setTitle(getString(R.string.share));
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				oks.setTitleUrl("http://sharesdk.cn");
				// text是分享文本，所有平台都需要这个字段
				oks.setText("我是分享文本");
				// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
				oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
				// url仅在微信（包括好友和朋友圈）中使用
				oks.setUrl("http://sharesdk.cn");
				// comment是我对这条分享的评论，仅在人人网和QQ空间使用
				oks.setComment("我是测试评论文本");
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite(getString(R.string.app_name));
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setSiteUrl("http://sharesdk.cn");
				oks.setSilent(true);
				// 启动分享GUI
				oks.show(getActivity());

				break;
			case R.id.btn2:
				ShareParams sp = new ShareParams();
				sp.setTitle("测试分享的标题");
				sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
				sp.setText("测试分享的文本");
				oks.setImagePath("/sdcard/test.jpg");
				oks.setUrl("http://sharesdk.cn");
				sp.setSite("发布分享的网站名称");
				sp.setSiteUrl("发布分享网站的地址");

				Platform qzone = ShareSDK.getPlatform (WechatMoments.NAME);
//				qzone. setPlatformActionListener (paListener); // 设置分享事件回调
				// 执行图文分享
				qzone.share(sp);
				break;
			case R.id.btn3:
				// 关闭sso授权
				oks.disableSSOWhenAuthorize();

				// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				oks.setTitle(getString(R.string.share));
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				oks.setTitleUrl("http://sharesdk.cn");
				// text是分享文本，所有平台都需要这个字段
				oks.setText("我是分享文本");
				// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
				oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
				// url仅在微信（包括好友和朋友圈）中使用
				oks.setUrl("http://sharesdk.cn");
				// comment是我对这条分享的评论，仅在人人网和QQ空间使用
				oks.setComment("我是测试评论文本");
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite(getString(R.string.app_name));
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setSiteUrl("http://sharesdk.cn");
				oks.setSilent(true);
				oks.setPlatform(WechatMoments.NAME);
				// 启动分享GUI
				oks.show(getActivity());

				break;
			case R.id.btn4:
				// 关闭sso授权
				oks.disableSSOWhenAuthorize();

				// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				oks.setTitle(getString(R.string.share));
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				oks.setTitleUrl("http://sharesdk.cn");
				// text是分享文本，所有平台都需要这个字段
				oks.setText("我是分享文本");
				// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
				oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
				// url仅在微信（包括好友和朋友圈）中使用
				oks.setUrl("http://sharesdk.cn");
				// comment是我对这条分享的评论，仅在人人网和QQ空间使用
				oks.setComment("我是测试评论文本");
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite(getString(R.string.app_name));
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setSiteUrl("http://sharesdk.cn");
				oks.setSilent(true);
				oks.setPlatform(Twitter.NAME);
				// 启动分享GUI
				oks.show(getActivity());
				break;
			case R.id.btn5:
				// 关闭sso授权
				oks.disableSSOWhenAuthorize();

				// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				oks.setTitle(getString(R.string.share));
				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				oks.setTitleUrl("http://sharesdk.cn");
				// text是分享文本，所有平台都需要这个字段
				oks.setText("我是分享文本");
				// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
				oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
				// url仅在微信（包括好友和朋友圈）中使用
				oks.setUrl("http://sharesdk.cn");
				// comment是我对这条分享的评论，仅在人人网和QQ空间使用
				oks.setComment("我是测试评论文本");
				// site是分享此内容的网站名称，仅在QQ空间使用
				oks.setSite(getString(R.string.app_name));
				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
				oks.setSiteUrl("http://sharesdk.cn");
				oks.setSilent(true);
				oks.setPlatform(Facebook.NAME);
				// 启动分享GUI
				oks.show(getActivity());
				break;
			case R.id.btn6:

				break;
			case R.id.btn7:

				break;
			case R.id.btn8:

				break;
			case R.id.btn9:

				break;
			default:
				break;
			}
		}

	}
}