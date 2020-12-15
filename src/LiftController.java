/***
 * controllerklasse voor de lift
 *
 * @author Jeroen Weltens
 * tijd te kort voor contracten
 */
public class LiftController {
    private LiftModel mModel;
    private InternalLiftView mInternal;
    private ExternalLiftView mExternal;

    public LiftController(LiftModel model, InternalLiftView iv, ExternalLiftView ev){
        mModel = model;
        mExternal = ev;
        ev.setmLiftcontroller(this);
        mInternal = iv;
        iv.setmLiftcontroller(this);
    }

    public void on_floorsubmit_internal(int floor){
        mModel.add_inside_call(floor);
    }

    public void on_floorsubmit_external(int floor){
        mModel.add_outside_call(floor);
    }

    public void on_maintenance(){mModel.toggle_maintenance();}

    public void on_open(){mModel.open_liftdoors();}

    public void on_close(){mModel.close_liftdoors();}

    public void on_alarm(){mModel.set_alarm();}
}
