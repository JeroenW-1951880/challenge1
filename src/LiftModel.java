import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * modelclass for a lift
 *
 * @author Jeroen Weltens
 */
public class LiftModel extends Observable implements Runnable{
    public static int LOWEST_FLOOR = -1;
    public static int HIGHEST_FLOOR = 13;

    enum LiftState { MAINTENANCE, ALARM, STILL, UP, DOWN };

    private boolean mDoorsopen;
    private int mLiftPos;
    private Integer mCurrentCall;
    private LiftState mState;
    private ArrayList<Integer> mCallQueue;

    /**
     * construcror --> initialises all member variables
     */
    public LiftModel()
    {
        mLiftPos = 0;
        mCurrentCall = null;
        mState = LiftState.STILL;
        mCallQueue = new ArrayList<>();
        mDoorsopen = false;
    }

    public int getLiftPos() {return mLiftPos;}

    public LiftState getState() {return mState;}

    /**
     * method to add a call to the callqueue from the inside of the lift
     * @param floor the floornumber the user of the lift wants to travel to
     * @pre LOWEST_FLOOR <= floor <= HIGHEST_FLOOR
     * @post call is added to mCallQueue or executed instantly
     */
    public void add_inside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        } else{
            mCallQueue.add(floor);
        }
    }

    /**
     * method to add a call to the callqueue from outside of the lift
     * @param floor the floornumber the user called the lift from
     * @pre LOWEST_FLOOR <= floor <= HIGHEST_FLOOR
     * @post call is added to mCallQueue or executed instantly
     */
    public void add_outside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        } else{
             mCallQueue.add(floor);
        }
    }

    /**
     * method to fetch the next liftcall out of the callqueue and makes a thread that executes it
     * @post mCurrentCall = the first element of mCallQueue or null
     */
    private synchronized void next_call(){
        if(mCallQueue.isEmpty()){
            mCurrentCall = null;
        } else {
            mCurrentCall = mCallQueue.remove(0);
            try{Thread.sleep(3000);}catch (InterruptedException e){}
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(this);
        }
    }

    /**
     * method called when the maintenance button in the lift is called
     * @post if the current state is still, the lift will enter maintenance state
     * @post if the current state is maintenance, the lift will enter still state and all calls are deleted to avoid unexpected behaviour
     * @post if the current state is neither of those, the lift can't go into maintenance
     */
    public void toggle_maintenance(){
        if(mState != LiftState.STILL && mState != LiftState.MAINTENANCE){
            System.err.println("maintenance ignored");
        } else if(mState == LiftState.STILL){
            mState = LiftState.MAINTENANCE;
            setChanged();
            notifyObservers();
        } else{
            mCurrentCall = null;
            mCallQueue.clear();
            mState = LiftState.STILL;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * method to open the liftdoors if possible
     */
    public void open_liftdoors(){
        if (mDoorsopen || (mState != LiftState.STILL && mState != LiftState.MAINTENANCE)){
            System.err.println("doors open ignored");
        } else{
            mDoorsopen = true;
        }
    }

    /**
     * method to close the liftdoors if possible
     */
    public void close_liftdoors(){
        if (!mDoorsopen || (mState != LiftState.STILL && mState != LiftState.MAINTENANCE) || mState == LiftState.ALARM){
            System.err.println("doors closed ignored");
        } else{
            mDoorsopen = false;
        }
    }

    /**
     * method called when the alarm button of the lift is called
     * @post alarm is set and all observers are notified
     */
    public void set_alarm(){
        if (mState != LiftState.ALARM) {
            mState = LiftState.ALARM;
            mDoorsopen = true;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * method called when the alarm diable button of the controlroom is called
     * @post mState = still and all observers are notified
     */
    public void disable_alarm() {
        mState = LiftState.STILL;
        setChanged();
        notifyObservers();
    }

    /***
     * method that moves the lift and opens / closes doors accordingly
     */
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
