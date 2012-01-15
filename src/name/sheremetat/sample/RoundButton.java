package name.sheremetat.sample;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;

public class RoundButton extends Button {
	private Region touchRegion = new Region();

	public RoundButton(Context context) {
		super(context);
	}

	public RoundButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initStyleButton(attrs);
	}

	public RoundButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initStyleButton(attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!touchRegion.contains((int) event.getX(), (int) event.getY())) {
				return false;
			}
		}
		return super.onTouchEvent(event);
	}

	private void initStyleButton(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.TouchAreaButton);

		int areaId = a.getResourceId(R.styleable.TouchAreaButton_toucharea, 0);

		if (areaId == 0) {
			return;
		}

		Resources res = getResources();
		TypedArray areas = res.obtainTypedArray(areaId);

		for (int i = 0; i < areas.length(); i++) {
			String touchArea = areas.getString(i);
			String[] data = touchArea.split(" ");
			if (data.length < 4)
				continue;
			
			// koef for convert px to dp
			float pxToDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics());
			
			int x = (int)(Integer.parseInt(data[0]) * pxToDp);
			int y = (int)(Integer.parseInt(data[1]) * pxToDp);
			int width = (int)(Integer.parseInt(data[2]) * pxToDp);
			int height = (int)(Integer.parseInt(data[3]) * pxToDp);
			Rect rect = new Rect(x, y, x + width, y + height);
			if (i == 0) {
				touchRegion.set(rect);
			} else {
				touchRegion.op(rect, Op.UNION);
			}
		}
	}
}
