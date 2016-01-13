package com.example.adapter;

import java.util.ArrayList;
import java.util.Random;

import com.example.mymdapp.R;
import com.mxk.baseframe.baseadapter.BaseRecylerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyRecyclerAdapter extends
		BaseRecylerViewAdapter<MyRecyclerAdapter.ViewHolder> {
	private ArrayList<String> mDataset;

	// Provide a reference to the type of views that you are using
	// (custom viewholder)
	public static class ViewHolder extends BaseRecylerViewAdapter.ViewHolder {
		public TextView mTextView;

		public ViewHolder(View view, OnItemClickListener onItemClickListener,
				OnItemLongClickListener onItemLongClickListener) {
			super(view, onItemClickListener, onItemLongClickListener);
			mTextView = (TextView) view.findViewById(R.id.text);
			// TODO Auto-generated constructor stub
		}

	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyRecyclerAdapter(ArrayList<String> myDataset) {
		mDataset = myDataset;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.recycler_item, parent, false);
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
		// TODO Auto-generated method stub
		viewHolder.mTextView.setText(mDataset.get(position));
	}
}