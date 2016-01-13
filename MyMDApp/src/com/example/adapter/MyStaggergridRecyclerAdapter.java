package com.example.adapter;

import java.util.ArrayList;
import java.util.Random;

import com.example.mymdapp.R;
import com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyStaggergridRecyclerAdapter extends
		BaseRecylerViewAdapter<MyStaggergridRecyclerAdapter.ViewHolder> {
	private ArrayList<String> mDataset;

	// Provide a reference to the type of views that you are using
	// (custom viewholder)
	public static class ViewHolder extends BaseRecylerViewAdapter.ViewHolder {
		public TextView mTextView;
		public FrameLayout flItem;

		public ViewHolder(View view, OnItemClickListener onItemClickListener,
				OnItemLongClickListener onItemLongClickListener) {
			super(view, onItemClickListener, onItemLongClickListener);
			flItem = (FrameLayout) view.findViewById(R.id.item);
			mTextView = (TextView) view.findViewById(R.id.text);
			// TODO Auto-generated constructor stub
		}

	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyStaggergridRecyclerAdapter(ArrayList<String> myDataset) {
		mDataset = myDataset;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.staggergrid_item, parent, false);
		ViewHolder vh = new ViewHolder(v, getOnItemClickListener(),
				getOnItemLongClickListener());
		return vh;
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	public String getData(int position) {
		return mDataset.get(position);
	}

	public void addRandomData() {
		int position = new Random().nextInt(mDataset.size());
		mDataset.add(position, "added item " + position);
		notifyItemInserted(position);
	}

	public void removeRandomData() {
		int position = new Random().nextInt(mDataset.size());
		mDataset.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public void onBindViewHolder(
			com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter.ViewHolder holder,
			int position) {
		ViewHolder viewHolder = (ViewHolder) holder;
		viewHolder.flItem.getLayoutParams().height = 100 + new Random()
				.nextInt(100);
		// TODO Auto-generated method stub
		viewHolder.mTextView.setText(mDataset.get(position));
	}
}