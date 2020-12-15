import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/***
 * class to represent the interface outside of the lift
 *
 * @author Jeroen Weltens
 */
public class ExternalLiftView extends AbstractLiftView{
    private LiftDisplay mDisplay;
    private JSpinner mFloorSelector;
    private JButton mSubmitbutton;

    /**
     * updates the display of the lift according to the data of the liftmodel
     * @param o the object of the liftmodel
     * @param arg null in this case
     * @post the interface of the lift is up to date
     */
    @Override
    public void update(Observable o, Object arg) {
        mDisplay.update_floor(getmLiftmodel().getLiftPos());
        mDisplay.update_state(getmLiftmodel().getState());
    }

    /**
     * constructor for the view outside of the lift
     * @param model the liftmodel
     * @post the view will be completely initialized and set up for display
     */
    public ExternalLiftView(LiftModel model)
    {
        setLiftmodel(model);
        mDisplay = new LiftDisplay(model.getState(), model.getLiftPos());
        JSpinner floorselector = new JSpinner();
        SpinnerNumberModel spinnermodel = new SpinnerNumberModel(LiftModel.LOWEST_FLOOR, LiftModel.LOWEST_FLOOR, LiftModel.HIGHEST_FLOOR, 1);
        floorselector.setModel(spinnermodel);
        mFloorSelector = floorselector;
        prepare_view();
    }

    /***
     * method to add all components of the view together
     * @pre mDisplay, mFloorSelector and the liftmodel must be initialized
     */
    private void prepare_view()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(mDisplay);

        add(Box.createVerticalStrut(10));

        JPanel selectorpanel = new JPanel();
        selectorpanel.setLayout(new BoxLayout(selectorpanel, BoxLayout.X_AXIS));
        selectorpanel.add(new JLabel("call from floor: "));
        selectorpanel.add(mFloorSelector);
        mSubmitbutton = new JButton("submit");
        selectorpanel.add(mSubmitbutton);
        add(selectorpanel);

        //add button functionality
        mSubmitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_floorsubmit_external((int)mFloorSelector.getValue());
            }
        });
    }
}
