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
}
