package com.mxk.baseframe.util.toast.frenchtoast;

public interface ToastQueue {
  void enqueue(Mixture mixture, long durationMs);

  boolean cancel(Mixture canceledMixture);

  void clear();
}
