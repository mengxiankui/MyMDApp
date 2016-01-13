package com.example.adapter;

import com.example.mymdapp.R;
import com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideMenuAdapter extends BaseRecylerViewAdapter<SlideMenuAdapter.ViewHolder> {
	private String[] mDataset;

	// Provide a reference to the type of views that you are using
	// (custom viewholder)
	public static class ViewHolder extends BaseRecylerViewAdapter.ViewHolder {
		public TextView mTextView;

		public ViewHolder(View view, OnItemClickListener onItemClickListener,
				OnItemLongClickListener onItemLongClickListener) {
			super(view, onItemClickListener, onItemLongClickListener);
			mTextView = (TextView) view.findViewById(R.id.menu_text);
			// TODO Auto-generated constructor stub
		}

	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public SlideMenuAdapter(String[] myDataset) {
		mDataset = myDataset;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.slide_menu_item, parent, false);
		ViewHolder vh = new ViewHolder(v, getOnItemClickListener(),
				getOnItemLongClickListener());
		return vh;
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.length;
	}
	
	public String getData(int position)
	{
		return mDataset[position];
	}
	
	@Override
	public void onBindViewHolder(
			com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter.ViewHolder holder,
			int position) {
		ViewHolder viewHolder = (ViewHolder) holder;
		// TODO Auto-generated method stub
		viewHolder.mTextView.setText(mDataset[position]);
	}
}