package me.vickychijwani.spectre.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.theme.MaterialComponentsViewInflater;

import java.util.HashMap;
import java.util.Map;

import me.vickychijwani.spectre.R;

/**
 * Applies the app's custom fonts to text views while they are inflated. Replaces the unmaintained
 * Typekit library, which relied on hidden-API reflection that crashes on recent Android versions.
 * Hooked up via the "viewInflaterClass" theme attribute.
 */
public class FontViewInflater extends MaterialComponentsViewInflater {

    private static final Map<String, Typeface> sNamedFonts = new HashMap<>();
    private static Typeface sNormal, sItalic, sBold, sBoldItalic;

    public static void setFonts(Typeface normal, Typeface italic, Typeface bold, Typeface boldItalic) {
        sNormal = normal;
        sItalic = italic;
        sBold = bold;
        sBoldItalic = boldItalic;
    }

    public static void addNamedFont(String name, Typeface typeface) {
        sNamedFonts.put(name, typeface);
    }

    @NonNull
    @Override
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return applyFont(super.createTextView(context, attrs), context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return applyFont(super.createButton(context, attrs), context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return applyFont(super.createEditText(context, attrs), context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        return applyFont(super.createAutoCompleteTextView(context, attrs), context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return applyFont(super.createCheckBox(context, attrs), context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return applyFont(super.createRadioButton(context, attrs), context, attrs);
    }

    private static <T extends TextView> T applyFont(T view, Context context, AttributeSet attrs) {
        Typeface typeface = null;
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{ R.attr.fontName });
        try {
            String fontName = a.getString(0);
            if (fontName != null) {
                typeface = sNamedFonts.get(fontName);
            }
        } finally {
            a.recycle();
        }
        if (typeface == null) {
            Typeface current = view.getTypeface();
            int style = (current != null) ? current.getStyle() : Typeface.NORMAL;
            typeface = forStyle(style);
        }
        if (typeface != null) {
            view.setTypeface(typeface);
        }
        return view;
    }

    private static Typeface forStyle(int style) {
        switch (style) {
            case Typeface.BOLD: return sBold;
            case Typeface.ITALIC: return sItalic;
            case Typeface.BOLD_ITALIC: return sBoldItalic;
            default: return sNormal;
        }
    }

}
