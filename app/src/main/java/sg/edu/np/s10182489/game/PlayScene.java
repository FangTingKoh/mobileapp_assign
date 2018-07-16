package sg.edu.np.s10182489.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;

import static sg.edu.np.s10182489.game.SceneManager.ACTIVE_SCENE;

public class PlayScene implements scene {

    private Rect r = new Rect();

    private player playerr;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrienData orienData;
    private long frameTime;

    public PlayScene(){
        playerr = new player(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        playerr.update(playerPoint);

        obstacleManager = new ObstacleManager(200,350,75,Color.BLACK);

        orienData = new OrienData();
        orienData.register();
        frameTime = System.currentTimeMillis();
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        playerr.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }
    /*
    private SceneManager manager;

    public PlayScene(SceneManager manager){
        this.manager = manager;
    }
    */

    public void terminate() {
        ACTIVE_SCENE = 0;
    }


    public void receiveTouch(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && playerr.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis()-gameOverTime>=2000)
                {
                    reset();
                    gameOver = false;
                    orienData.nwGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
            //playerPoint.set((int)event.getX(), (int)event.getY());
        }


    }


    public void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        playerr.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    public void update(){

        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orienData.getOrientation() !=null && orienData.getStartOrientation() !=null) {
                float pitch = orienData.getOrientation()[1] - orienData.getStartOrientation()[1];
                float roll = orienData.getOrientation()[2] - orienData.getStartOrientation()[2];
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            if(playerPoint.x < 0)
                playerPoint.x =0;

            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;

            if(playerPoint.y < 0)
                playerPoint.y =0;

            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            playerr.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollide(playerr)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }

    private void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width()/2f-r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
