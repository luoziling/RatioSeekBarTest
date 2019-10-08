package com.wzb.ratioseekbartest.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.wzb.ratioseekbartest.R;

public class HomeFragment extends Fragment {

	private HomeViewModel homeViewModel;

	@BindView(R.id.ratioSeekBar)
	SeekBar mRatioSeekBar;

	@BindView(R.id.ratioSeekBarWrap)
	LinearLayout mRatioSeekBarWrap;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		homeViewModel =
				ViewModelProviders.of(this).get(HomeViewModel.class);
		View root = inflater.inflate(R.layout.fragment_home, container, false);


		ButterKnife.bind(this,root);

		final TextView textView = root.findViewById(R.id.text_home);
		homeViewModel.getText().observe(this, new Observer<String>() {
			@Override
			public void onChanged(@Nullable String s) {
				textView.setText(s);
			}
		});
		init();
		return root;
	}

	private void init(){
		// 根据比例，在两个color值之间计算出一个color值
		final int fromColor = ContextCompat.getColor(getContext(),R.color.colorHelper_square_from_ratio_background);
		final int toColor = ContextCompat.getColor(getContext(),R.color.colorHelper_square_to_ratio_background);

		// 设置SeekBar监听事件
		mRatioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				// 进度条改变时
				// 计算颜色中间值
				// i代表progress
				int ratioColor = QMUIColorHelper.computeColor(fromColor,toColor,(float) i / 100);
				// 设置背景linearLayout的颜色
				mRatioSeekBarWrap.setBackgroundColor(ratioColor);
				Log.e("now:",String.valueOf(i));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// 刚刚触碰进度条
				Toast.makeText(getContext(),"当前的进度：" + seekBar.getProgress(),Toast.LENGTH_LONG).show();
//				Toast.makeText(getActivity(),"当前的进度：" + seekBar.getProgress(),Toast.LENGTH_LONG).show();



			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 松开进度条

				//以下是业务逻辑
				// 后续可以在这里计算用户在两个准则之间偏好哪个
				int progress = seekBar.getProgress();

				Toast.makeText(getContext(),"当前的进度：" + seekBar.getProgress(),Toast.LENGTH_LONG).show();
				// 判断矩阵的输入值
				Double judge = 0.0;
				// 因为设置了进度是0-100
				// 初始值是在中间
				// 向左为正，向右为负
				// 其实没有正负
				// 0-10 1为分界线
				if (progress<50){
					// 0-50是偏好左边准则数字越小代表向左滑动越大，此时数字越小
					// 判断矩阵中数值的计算应该是[(50-progress)/50]
					judge = ((50-progress)/50.0);
					//左边准则优先于右边准则，此时需要*10
					judge = judge*10;
				}else if (progress>50){
					// 50-100是偏好右边准则数字越大代表向左滑动越大，此时数字越大
					// 判断矩阵中数值的计算应该是[(progress-50)/50]
					// 右侧准则大于左侧准则此时小于1也要改变
					judge = ((progress-50)/50.0);
					// 小于一是因为倒数
					judge *= 10;
					judge = 1.0/judge;

				}else if (progress==50){
					judge = 0.0;
				}

				Toast.makeText(getContext(),"当前的judge：" + judge,Toast.LENGTH_LONG).show();


				//之后需要一个button来保存数据存入数据库
			}
		});
		mRatioSeekBar.setProgress(50);
	}
}