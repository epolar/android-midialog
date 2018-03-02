package li.xiangyang.android.midialog;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bac on 16/5/11.
 */
public class LinearNumberSelect2Dialog extends Select2Dialog {

    private IListener listener;
    private List<String> items = new ArrayList<>();
    private List<String> itemsRight = new ArrayList<>();

    private boolean floatValue = false;
    private boolean floatValueRight = false;

    /**
     * @param context
     * @param lis
     * @param title
     */
    public LinearNumberSelect2Dialog(Context context, final IListener lis, String title) {
        super(context, null, title, null, null);
        this.listener = lis;
        super.setListener(new Select2Dialog.IListener() {
            @Override
            public void onCancel() {
                if (LinearNumberSelect2Dialog.this.listener != null) {
                    LinearNumberSelect2Dialog.this.listener.onCancel();
                }
            }

            @Override
            public void onChange(int index, int selection) {
                if (listener != null) {
                    listener.onChange(index == 0, selection);
                }
            }

            @Override
            public void onDone(int left, int right) {
                if (listener != null) {
                    Number leftValue = floatValue ? parseFloat(items.get(left)) : Integer.parseInt(items.get(left));
                    Number rightValue = floatValueRight ? parseFloat(itemsRight.get(right)) : Integer.parseInt(itemsRight.get(right));
                    listener.onDone(leftValue, rightValue);
                }
            }
        });
    }

    /**
     * 西班牙语等的浮点型是 ", " 分割的，需要另外处理
     * @param str
     * @return
     */
    private float parseFloat(String str) {
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
        try {
            return nf.parse(str).floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public void setLeft(String unit, int start, int end, int step, int selelction) {
        setLeft(unit, "%.0f", start, end, step, selelction);
        floatValue = false;
    }

    public void setRight(String unit, int start, int end, int step, int selection) {
        setRight(unit, "%.0f", start, end, step, selection);
        floatValueRight = false;
    }

    public void setLeft(String unit, String format, float start, float end, float step, int selelction) {
        items.clear();
        for (float i = start; i < end; i += step) {
            items.add(String.format(format, i));
        }
        setUints(unit, null, null);
        super.setItems(items, selelction);
        floatValue = true;
    }

    public void setRight(String unit, String format, float start, float end, float step, int selection) {
        itemsRight.clear();
        for (float i = start; i < end; i += step) {
            itemsRight.add(String.format(format, i));
        }
        setUints(null, unit, null);
        super.setItems2(itemsRight, selection);
        floatValueRight = true;
    }

    public static interface IListener {
        void onCancel();

        void onChange(boolean left, int selection);

        void onDone(Number number, Number numberRight);
    }
}
