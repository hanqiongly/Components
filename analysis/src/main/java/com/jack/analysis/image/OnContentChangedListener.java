package com.jack.analysis.image;

import com.jack.analysis.image.canvas.CanvasAPI;

public interface OnContentChangedListener{
    void onContentChanged(@CanvasAPI.CanvasDrawType int contentType);
}
