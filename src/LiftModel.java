import java.util.Observable;
import java.util.Queue;

/**
 * modelklasse voor de lift
 *
 * @author Jeroen Weltens
 * tijd te kort voor contracten
 */
public class LiftModel extends Observable {
    public static int LOWEST_FLOOR = -1;
    public static int HIGHEST_FLOOR = 13;

    enum LiftState { MAINTENANCE, ALARM, STILL, UP, DOWN };

    private int mLiftPos;
    private Integer mCurrentCall;
    private LiftState mState;
    private Queue<Integer> mCallQueue;

    public LiftModel()
    {
        mLiftPos = 0;
        mCurrentCall = null;
        mState = LiftState.STILL;
        mCallQueue = null;
    }

    public int getLiftPos() {return mLiftPos;}

    public LiftState getState() {return mState;}

    public void add_inside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            do_move();
        } else{
            mCallQueue.add(floor);
        }
    }

    public void add_outside_call(int floor){
        if (mCurrentCall == null){
            mCurrentCall = floor;
            do_move();
        } else{
            mCallQueue.add(floor);
        }
    }

    private void do_move()
    {
        if (mLiftPos > mCurrentCall){
            mState = LiftState.DOWN;
        } else if(mLiftPos < mCurrentCall){
            mState = LiftState.DOWN;
        } else{
            //open doors
        }
        setChanged();
        notifyObservers();
        //move lift
        //currentcall is volgende in queue of null
    }
}
