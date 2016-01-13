package com.bigkoo.pickerview;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelOptions;
import com.bigkoo.pickerview.view.WheelOptions.WheelOptionsAdapter;

/**
 * Created by Sai on 15/11/22.
 */
public class OptionsPickerView extends BasePickerView implements
		View.OnClickListener {
	WheelOptions wheelOptions;
	private View btnSubmit;
	private OnOptionsSelectListener optionsSelectListener;
	private static final String TAG_SUBMIT = "submit";

	public OptionsPickerView(Context context,int wheelNum) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.pickerview_options,
				contentContainer);
		// -----确定和取消按钮
		btnSubmit = findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnSubmit.setOnClickListener(this);
		// ----转轮
		final View optionspicker = findViewById(R.id.optionspicker);
		wheelOptions = new WheelOptions(optionspicker,wheelNum);
	}

	public void setAdapter(WheelOptionsAdapter wheelOptionsAdapter) {
		wheelOptions.setAdapter(wheelOptionsAdapter);
	}
	
	public void setLinkage(boolean linkage) {
		wheelOptions.setLinkage(linkage);
	}

	public void setTextSize(int textSize) {
		wheelOptions.setTextSize(textSize);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 */
	public void setSelectOptions(int option1) {
		wheelOptions.setCurrentItems(option1, 0, 0);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 * @param option2
	 */
	public void setSelectOptions(int option1, int option2) {
		wheelOptions.setCurrentItems(option1, option2, 0);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 * @param option2
	 * @param option3
	 */
	public void setSelectOptions(int option1, int option2, int option3) {
		wheelOptions.setCurrentItems(option1, option2, option3);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 */
	public void setLabels(String label1) {
		wheelOptions.setLabels(label1, null, null);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 */
	public void setLabels(String label1, String label2) {
		wheelOptions.setLabels(label1, label2, null);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		wheelOptions.setLabels(label1, label2, label3);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelOptions.setCyclic(cyclic);
	}

	public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
		wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
	}

	@Override
	public void onClick(View v) {

		if (optionsSelectListener != null) {
			ArrayList<String> currentItems = wheelOptions.getCurrentItemsData();
			optionsSelectListener.onOptionsSelect(currentItems);
		}
		dismiss();

	}

	public interface OnOptionsSelectListener {
		public void onOptionsSelect(ArrayList<String> arrayList);
	}

	public void setOnoptionsSelectListener(
			OnOptionsSelectListener optionsSelectListener) {
		this.optionsSelectListener = optionsSelectListener;
	}

}
