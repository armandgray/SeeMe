package com.armandgray.seeme.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

public class StringHelper {

    public static SpannableStringBuilder getBoldStringBuilder(String header, String content) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(header + "\n\n" + content);
        StyleSpan boldStyleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        stringBuilder.setSpan(boldStyleSpan, 0, header.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return stringBuilder;
    }
}
