package com.pProject.ganada;

import android.net.Uri;

public interface CaptionView {
    void onCaptionLoading();
    void onCaptionSuccess(Uri uri, Caption caption);
}
