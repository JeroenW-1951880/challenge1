import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * modelklasse voor de lift
 *
 * @author Jeroen Weltens
 * tijd te kort voor contracten
 */
public class LiftModel extends Observable implements Runnable{
    public static int LOWEST_FLOOR = -1;
    public static int HIGHEST_FLOOR = 13;

    enum LiftState { MAINTENANCE, ALARM, STILL, UP, DOWN };

    private boolean mDoorsopen;
    private int mLiftPos;
    private Integer mCurrentCall;
    private LiftState mState;
    private ArrayBlockingQueue<Integer> mCallQueue;

    public LiftModel()
    {
        mLiftPos = 0;
        mCurrentCall = null;
        mState = LiftState.STILL;
        mCallQueue = new ArrayBlockingQueue<Integer>(5);
        mDoorsopen = false;
    }

    public int getLiftPos() {return mLiftPos;}

    public LiftState getState() {return mState;}

    public void add_inside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        } else{
            try{
                mCallQueue.add(floor);
            } catch (IllegalStateException e){
                System.err.println("callqueue is full");
            }
        }
    }

    public void add_outside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        } else{
            try{
                mCallQueue.add(floor);
            } catch (IllegalStateException e){
                System.err.println("callqueue is full");
            }
        }
    }

    private synchronized void next_call(){
        if(mCallQueue.isEmpty()){
            mCurrentCall = null;
        } else {
            mCurrentCall = mCallQueue.poll();
            try{Thread.sleep(3000);}catch (InterruptedException e){}
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        }
    }

    public void toggle_maintenance(){
        if(mState == LiftState.ALARM){
            System.err.println("maintenance ignored");
        } else if(mState != LiftState.MAINTENANCE){
            mState = LiftState.MAINTENANCE;
            setChanged();
            notifyObservers();
        } else{
            mState = LiftState.STILL;
            setChanged();
            notifyObservers();
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        }
    }

    public void open_liftdoors(){
        if (mDoorsopen || mState != LiftState.STILL){
            System.err.println("doors open ignored");
        } else{
            mDoorsopen = true;
        }
    }

    public void close_liftdoors(){
        if (!mDoorsopen || mState != LiftState.STILL || mState == LiftState.ALARM){
            System.err.println("doors closed ignored");
        } else{
            mDoorsopen = false;
        }
    }

    public void set_alarm(){
        mState = LiftState.ALARM;
    }

    @Override
    public void run() {
        if(mState != LiftState.MAINTENANCE && mState != LiftState.ALARM){
            if (mLiftPos > mCurrentCall){
                mDoorsopen = false;
                mState = LiftState.DOWN;
                while (mLiftPos != mCurrentCall){
                    try{Thread.sleep(3000);}catch (InterruptedException e){}
                    mLiftPos--;
                    setChanged();
                    notifyObservers();
                }
                mDoorsopen = true;
            } else if(mLiftPos < mCurrentCall){
                mDoorsopen = false;
                mState = LiftState.UP;
                while (mLiftPos != mCurrentCall){
                    try{Thread.sleep(3000);}catch (InterruptedException e){}
                    mLiftPos++;
                    setChanged();
                    notifyObservers();
                }
                mDoorsopen = true;
            } else{
                mDoorsopen = true;
            }
            mState = LiftState.STILL;
            setChanged();
            notifyObservers();
            next_call();
        } else if (mState == LiftState.MAINTENANCE){
            System.err.println("lift is under maintenance");
        } else if (mState == LiftState.ALARM){
            System.err.println("cant take the lift while emergency");
        }
    }
}
