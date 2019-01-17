package com.jack.app.test.textview;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jack.app.R;

/**
 * Created by liuyang on 2018/2/2.
 */

public class TestHtml2StringActivity extends Activity{

    String currentStr = "<h1 class=\"ql-align-center\" style=\"margin-top: 0px; margin-bottom: 0px; cursor: text; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; text-align: center; color: rgba(220, 80, 12, 0.6); letter-spacing: 1px; white-space: pre-wrap;\"><span style=\"font-weight: bolder;\">fsdafasdfads</span><em> fgarfadsfadf a </em><a href=\"http://pre-cms.test.meipu.cn/vinci/editor/www.baidu.com\" target=\"_blank\" style=\"color: rgb(255, 102, 204); text-decoration-line: underline;\"><em>www.baidu.com</em></a><em> </em><br><img src= \"http://img2.niutuku.com/desk/130126/50/50-niutuku.com-3495.jpg\"/><tb> <a href=\"https://www.sina.cn\"><img src=\"http://img2.niutuku.com/1312/0827/0827-niutuku.com-13638.jpg\" width=\"900\" height=\"900\"/></a></tb></h1><div align=\"center\">woyaojuzhong</div> <br><div align=\"right\">woyaojuzhong</div><br><div>woyaojuzhong</div><br><center>woyajuzhong</center><br><p class=\"ql-align-center\">-热卖商品-</p>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_string_layout);
        preDealString();
    }

    private void preDealString() {
        String str = new PreDealHtml().setHtml(currentStr).preDealHtml();

        Log.d("test_PreDealHtml", "after deal :" + str);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static final String ALIGN_TEXT_CENTER = "ql-align-center";
    private static final String TEXT_ALIGN_STYLE_PRE = "text-align:";
    private static final String TEXT_ALIGN_STYLE = "\"text-align:center\"";
    private static final String TEXT_STYLE = "style=";
    private static final String TEXT_STYLE_ = "style =";

    private static class PreDealHtml{
        private static final String tag = "test_PreDealHtml";
        private String mHtml;
        int stackIndex = 0;
//        int flag = 0;
        String[] endTags;

        public PreDealHtml setHtml(String rawHtml) {
            mHtml = rawHtml;
            endTags = new String[100];
            return this;
        }

        private String preDealHtml() {
            if (TextUtils.isEmpty(mHtml)) {
                return mHtml;
            }
            StringBuilder result = new StringBuilder();
            Log.d(tag, mHtml);
            Log.d(tag,"--------------------");
            String[] splitedStr = mHtml.split("</");

//            if (!CollectionUtils.isEmpty(splitedStr)) {
            if (splitedStr != null && splitedStr.length > 0 ){
                for (int i = 1; i < splitedStr.length; i++) {
                    splitedStr[i] = "</" + splitedStr[i];
                }
                for (int i = 0; i < splitedStr.length; i++) {
                    String itemString = splitedStr[i];
                    String[] itemSubStrs = itemString.split(">");
//                    if (CollectionUtils.isEmpty(itemSubStrs)) {
                    if (itemSubStrs == null || itemSubStrs.length <= 0) {
                        continue;
                    }
                    for (int j = 0; j < itemSubStrs.length;j++) {
                        String cellStr = itemSubStrs[j];
                        if (!TextUtils.isEmpty(cellStr)) {
                            boolean needCenterTagStart = false;
                            if (cellStr.contains(ALIGN_TEXT_CENTER) /*&& !cellStr.contains(TEXT_ALIGN_STYLE_PRE)*/) {
//                                if (cellStr.contains(TEXT_STYLE_) ) {
//                                    cellStr = cellStr.replace(TEXT_STYLE," style=" + TEXT_ALIGN_STYLE);
//                                } else if (cellStr.contains(TEXT_STYLE)){
//                                    cellStr = cellStr.replace(TEXT_STYLE_," style=" + TEXT_ALIGN_STYLE);
//                                } else {
//                                    cellStr = cellStr + " style=" + TEXT_ALIGN_STYLE;
//                                }
                                needCenterTagStart = true;
                                itemSubStrs[j] = cellStr.contains("<") ? cellStr + ">" + (needCenterTagStart ? "<center>" : "" ): cellStr;
                                result.append(itemSubStrs[j]);

                                if (stackIndex < 100) {
                                    endTags[stackIndex] = findEndTag(itemSubStrs[j]);
                                    stackIndex++;
                                }
                            } else {

                                cellStr = cellStr.contains("<") ? cellStr + ">" : cellStr;
                                if (stackIndex > 0) {
                                    if (cellStr.contains(endTags[stackIndex - 1])) {
                                        cellStr = cellStr.replace(endTags[stackIndex - 1], "</center>" + endTags[stackIndex - 1]);
                                        stackIndex --;
                                    }
                                }
                                itemSubStrs[j] = cellStr;
                                result.append(itemSubStrs[j]);
                            }
//                            itemSubStrs[j] = cellStr.contains("<") ? cellStr + ">" + (needCenterTagStart ? "<center>" : "" ): cellStr;
//                            result.append(itemSubStrs[j]);
//                            endTags[stackIndex] = findEndTag(itemSubStrs[j]);
//                            stackIndex++;
//                            itemSubStrs[j] = cellStr;
                        } else {
                            continue;
                        }
                    }
                    displayInfo(itemSubStrs);
                }
            }

            Log.d(tag, "===================================================");
            Log.d(tag, result.toString());
            return result.toString();
        }

        private String findEndTag(String rawHtml) {
            if (TextUtils.isEmpty(rawHtml)) {
                return "";
            }

            String startTag = "";
            int spaceIndex = rawHtml.indexOf(" ");
            if (spaceIndex <= 0) {
                return "";
            }
            startTag = rawHtml.substring(0, spaceIndex);

            String endTag = startTag;
            if (!TextUtils.isEmpty(endTag)) {
                endTag = endTag.replace("<", "</") + ">";
            }

//            Log.d(tag, " the target tag is :" + startTag + "  " + endTag);

            return endTag;
        }


        private void displayInfo(String[] strings) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : strings) {
                Log.d(tag, str);
            }
        }
    }

}
