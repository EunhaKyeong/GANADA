package com.pProject.ganada;

import java.util.HashMap;

public interface ExamParsingView {
    void onParsingLoading();
    void onParsingSuccess(HashMap hm);
    void onParsingFail();
}
