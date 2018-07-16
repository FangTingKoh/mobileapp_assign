package sg.edu.np.s10182489.game;

import android.graphics.Canvas;
import android.transition.Scene;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(){
        ACTIVE_SCENE = 0;
        scenes.add(new PlayScene());
    }


    public void receiveTouch(MotionEvent event){

        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update(){
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);

    }





}
