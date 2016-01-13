package com.bigkoo.pickerview.view;

import java.util.ArrayList;

import android.util.SparseArray;
import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

public class WheelOptions {
	private static final int DEFAULT_WHEEL_NUM = 1;
	private static final int DEFAULT_TEXT_SIZE = 25;

	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;
	private WheelView wv_option3;

	private int textSize = DEFAULT_TEXT_SIZE;

	private int i_wheelNum;

	private boolean linkage = false;

	private OnItemSelectedListener wheelListener_option1;
	private OnItemSelectedListener wheelListener_option2;

	private WheelOptionsAdapter wheelOptionsAdapter;
	private ArrayWheelAdapter adapter1,adapter2,adapter3;

	public WheelOptions(View view) {
		this(view, DEFAULT_WHEEL_NUM);
	}

	public WheelOptions(View view, int wheelNum) {
		super();
		this.view = view;
		i_wheelNum = wheelNum;
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setTextSize(textSize);

		if (i_wheelNum > 1) {
			wv_option2 = (WheelView) view.findViewById(R.id.options2);
			wv_option2.setTextSize(textSize);

		}
		if (i_wheelNum > 2) {
			wv_option3 = (WheelView) view.findViewById(R.id.options3);
			wv_option3.setTextSize(textSize);
		}
	}

	public void setLinkage(boolean linkage) {
		this.linkage = linkage;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public void setAdapter(final WheelOptionsAdapter wheelOptionsAdapter) {
		this.wheelOptionsAdapter = wheelOptionsAdapter;
		if (null != wv_option1) {
			adapter1 = new ArrayWheelAdapter(wheelOptionsAdapter
					.getFirstWheelData());
			wv_option1.setAdapter(adapter1);
			wv_option1.setCurrentItem(0);
		}
		if (null != wv_option2) {
			adapter2 = new ArrayWheelAdapter(wheelOptionsAdapter
					.getSecondWheelData(adapter1.getSortId(0)));
			wv_option2.setAdapter(adapter2);
			wv_option2.setCurrentItem(0);
			// 添加联动监听
			if (linkage) {
				// 联动监听器
				wheelListener_option1 = new OnItemSelectedListener() {

					@Override
					public void onItemSelected(int index) {
						if (null != wheelOptionsAdapter) {
							adapter2.setWheelData(wheelOptionsAdapter
											.getSecondWheelData(adapter1.getSortId(index)));
						}

						if (null != wheelListener_option2) {
							wheelListener_option2.onItemSelected(0);
						}
					}
				};
				wv_option1.setOnItemSelectedListener(wheelListener_option1);
			}
		}
		if (null != wv_option3) {
			adapter3 = new ArrayWheelAdapter(wheelOptionsAdapter
					.getThirdWheelData(adapter2.getSortId(0)));
			wv_option3.setAdapter(adapter3);
			wv_option3.setCurrentItem(0);
			if (linkage) {
				wheelListener_option2 = new OnItemSelectedListener() {

					@Override
					public void onItemSelected(int index) {
						if (null != wheelOptionsAdapter) {
							adapter3.setWheelData(wheelOptionsAdapter
									.getThirdWheelData(adapter2.getSortId(index)));
						}
					}
				};
				wv_option2.setOnItemSelectedListener(wheelListener_option2);
			}

		}

	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
		if (label3 != null)
			wv_option3.setLabel(label3);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 分别设置第一二三级是否循环滚动
	 * 
	 * @param cyclic1
	 *            ,cyclic2,cyclic3
	 */
	public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
		wv_option1.setCyclic(cyclic1);
		wv_option2.setCyclic(cyclic2);
		wv_option3.setCyclic(cyclic3);
	}

	/**
	 * 设置第二级是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setOption2Cyclic(boolean cyclic) {
		wv_option2.setCyclic(cyclic);
	}

	/**
	 * 设置第三级是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setOption3Cyclic(boolean cyclic) {
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
	 * 
	 * @return
	 */
//	public int[] getCurrentItems() {
//		int[] currentItems = new int[3];
//		currentItems[0] = wv_option1.getCurrentItem();
//		currentItems[1] = wv_option2.getCurrentItem();
//		currentItems[2] = wv_option3.getCurrentItem();
//		return currentItems;
//	}
	
	public ArrayList<String> getCurrentItemsData() {
		ArrayList<String> currentItemsData = new ArrayList<String>();
		if (null != adapter1) {
			currentItemsData.add(adapter1.getItem(wv_option1.getCurrentItem()));
		}
		if (null != adapter2) {
			currentItemsData.add(adapter2.getItem(wv_option2.getCurrentItem()));
		}
		if (null != adapter3) {
			currentItemsData.add(adapter3.getItem(wv_option3.getCurrentItem()));
		}
		return currentItemsData;
	}

	public void setCurrentItems(int option1, int option2, int option3) {
		if (linkage && wv_option1.getCurrentItem() != option1) {
			wheelListener_option1.onItemSelected(option1);
		} else if (linkage && wv_option2.getCurrentItem() != option2) {
			wheelListener_option2.onItemSelected(option2);
		}
		wv_option1.setCurrentItem(option1);
		wv_option2.setCurrentItem(option2);
		wv_option3.setCurrentItem(option3);
	}

	public interface WheelOptionsAdapter {
		public SparseArray<ArrayList<String>> getFirstWheelData();

		public SparseArray<ArrayList<String>> getSecondWheelData(int parentIndex);

		public SparseArray<ArrayList<String>> getThirdWheelData(int parentIndex);
	}
}
