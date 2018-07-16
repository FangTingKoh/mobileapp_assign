package sg.edu.np.s10182489.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
