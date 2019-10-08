package com.wzb.ratioseekbartest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 用于存放UI数据防止用于采取一些行动导致activity或者fragment被重置导致数据丢失
 * 不应该将Activity Fragment View context等存入ViewModel
 * ViewModel如果存放了activity当旋转屏幕时Activity将被销毁
 * ViewModel还存在着已被销毁的Activity的引用这种情况是一种内存泄漏
 */

public class HomeViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public HomeViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is home fragment");
	}

	public LiveData<String> getText() {
		return mText;
	}
}