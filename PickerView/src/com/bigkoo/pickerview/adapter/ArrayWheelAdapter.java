package com.bigkoo.pickerview.adapter;

import java.util.ArrayList;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.SparseArray;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter implements WheelAdapter<String> {
	
	private final DataSetObservable mDataSetObservable = new DataSetObservable();
	
	// items
	private ArrayList<String> items;
	//ids
	private ArrayList<String> ids;
	
	/**
	 * Contructor
	 * @param items the items
	 */
	public ArrayWheelAdapter(SparseArray<ArrayList<String>> datas) {
		this.items = datas.get(0);
		this.ids = datas.get(1);
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }
	
	@Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
    
    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int indexOf(String o){
		return items.indexOf(o);
	}
	
	public int getSortId(int index) {
		String sortId = "";
		if (index >= 0 && index < ids.size()) {
			sortId = ids.get(index);
		}
		try {
			return Integer.parseInt(sortId);
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}
	
	public void setWheelData(SparseArray<ArrayList<String>> datas)
	{
		this.items = datas.get(0);
		this.ids = datas.get(1);
		notifyDataSetChanged();
	}

}
