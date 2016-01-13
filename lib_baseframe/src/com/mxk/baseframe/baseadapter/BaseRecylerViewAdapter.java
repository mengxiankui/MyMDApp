package com.mxk.baseframe.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public abstract class BaseRecylerViewAdapter<VH extends BaseRecylerViewAdapter.ViewHolder> extends
		RecyclerView.Adapter<BaseRecylerViewAdapter.ViewHolder> {
	private OnItemClickListener onItemClickListener;
	private OnItemLongClickListener onItemLongClickListener;

	public static abstract class ViewHolder extends RecyclerView.ViewHolder
			implements OnClickListener, OnLongClickListener {
		private OnItemClickListener onItemClickListener;
		private OnItemLongClickListener onItemLongClickListener;

		public ViewHolder(View view, OnItemClickListener onItemClickListener,
				OnItemLongClickListener onItemLongClickListener) {
			super(view);
			this.onItemClickListener = onItemClickListener;
			this.onItemLongClickListener = onItemLongClickListener;
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if (null != onItemLongClickListener) {
				onItemLongClickListener
						.onItemLongClick(v, getAdapterPosition());
				return true;
			}
			return false;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (null != onItemClickListener) {
				onItemClickListener.onItemClick(v, getAdapterPosition());
			}
		}

	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public OnItemLongClickListener getOnItemLongClickListener() {
		return onItemLongClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setOnItemLongClickListener(
			OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}

	public interface OnItemClickListener {
		public void onItemClick(View v, int position);
	}

	public interface OnItemLongClickListener {
		public void onItemLongClick(View v, int position);
	}
}