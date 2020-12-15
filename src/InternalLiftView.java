import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

/***
 * class to represent the interface inside a lift
 *
 * @author Jeroen Weltens
 */
public class InternalLiftView extends AbstractLiftView {
    private LiftDisplay mDisplay;
    private JSpinner mFloorSelector;
    private JButton mAlarm;
    private JButton mMaintenance;
    private JButton mDoorsOpen;
    private JButton mDoorsClosed;
    private JButton mSubmitbutton;

    /**
     * updates the display of the lift according to the data of the liftmodel
     * @param o the object of the liftmodel
     * @param arg null in this case
     * @post the interface of the lift is up to date
     */
    @Override
    public void update(Observable o, Object arg) {
        mDisplay.update_state(getmLiftmodel().getState());
        mDisplay.update_floor(getmLiftmodel().getLiftPos());
    }

    /**
     * constructor for the view inside the lift
     * @param model the liftmodel
     * @post the view will be completely initialized and set up for display
     */
    public InternalLiftView(LiftModel model)
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

        mSubmitbutton = new JButton("submit");

        JPanel selectorpanel = new JPanel();
        selectorpanel.setLayout(new BoxLayout(selectorpanel, BoxLayout.X_AXIS));
        selectorpanel.add(new JLabel("go to floor: "));
        selectorpanel.add(mFloorSelector);
        selectorpanel.add(mSubmitbutton);
        add(selectorpanel);

        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new GridLayout(2, 2));

        mDoorsClosed = new JButton("close doors");
        mDoorsOpen = new JButton("open doors");
        mAlarm = new JButton("alarm");
        mMaintenance = new JButton("maintenance");

        buttonpanel.add(mDoorsClosed);
        buttonpanel.add(mDoorsOpen);
        buttonpanel.add(mAlarm);
        buttonpanel.add(mMaintenance);

        add(Box.createVerticalStrut(10));
        add(buttonpanel);

        //add button functionality
        mSubmitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_floorsubmit_internal((int)mFloorSelector.getValue());
            }
        });

        mMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_maintenance();
            }
        });

        mDoorsOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_open();
            }
        });

        mDoorsClosed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_close();
            }
        });

        mAlarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmLiftcontroller().on_alarm();
            }
        });
    }

}
