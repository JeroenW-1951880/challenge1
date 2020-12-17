/***
 * controllerklasse voor de lift
 *
 * @author Jeroen Weltens
 */
public class LiftController {
    private LiftModel mModel;

    /**
     * constructor of the controller to establish connection between model and view
     * @param model the liftmodel of this lift
     * @param iv the internam view of this lift
     * @param ev the external view of this lift
     * @post the model of this controller is set and the controller is added to the viewclasses
     */
    public LiftController(LiftModel model, InternalLiftView iv, ExternalLiftView ev){
        mModel = model;
        ev.setmLiftcontroller(this);
        iv.setmLiftcontroller(this);
    }

    /***
     * all next methods connect buttonlisteners of the view to their according methods in the model
     */
    public void on_floorsubmit_internal(int floor){ mModel.add_inside_call(floor); }

    public void on_floorsubmit_external(int floor){ mModel.add_outside_call(floor); }

    public void on_maintenance(){mModel.toggle_maintenance();}

    public void on_open(){mModel.open_liftdoors();}

    public void on_close(){mModel.close_liftdoors();}

    public void on_alarm(){mModel.set_alarm();}
}
