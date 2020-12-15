import javax.swing.*;
import java.util.Observable;

/***
 * abstract class for the LiftViews
 *
 * @author Jeroen Weltens
 */
public abstract class AbstractLiftView extends JPanel implements LiftView {
    private LiftModel mLiftmodel;
    private LiftController mLiftcontroller;


    /** getter and setter for the liftmodel*/
    public void setLiftmodel(LiftModel model){mLiftmodel = model;}
    public LiftModel getmLiftmodel() { return mLiftmodel; }

    public void setmLiftcontroller(LiftController mLiftcontroller) {
        this.mLiftcontroller = mLiftcontroller;
    }

    public LiftController getmLiftcontroller() {
        return mLiftcontroller;
    }
}
