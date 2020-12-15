import javax.swing.*;

/***
 * class to represent a display for the lift
 *
 * @author Jeroen Weltens
 */
public class LiftDisplay extends JPanel {
    private JLabel mStateLabel;
    private JLabel mFloorLabel;

    /**
     * constructor for the display
     * @post the panel is initialized, the labels for floor and statedisplay are added to the panel
     */
    public LiftDisplay(LiftModel.LiftState state, int floor)
    {
        mStateLabel = new JLabel();
        mFloorLabel = new JLabel();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(mStateLabel);
        add(Box.createHorizontalStrut(10));
        add(mFloorLabel);
        update_state(state);
        update_floor(floor);
    }

    /***
     * method to set the state of the lift for the display
     * @pre state != null
     * @param state the state to change the display to
     */
    public void update_state(LiftModel.LiftState state)
    {
        String buttontext = "state: ";
        switch(state)
        {
            case UP:
                buttontext += "up";
                break;
            case DOWN:
                buttontext += "down";
                break;
            case ALARM:
                buttontext += "alarm";
                break;
            case STILL:
                buttontext += "Still";
                break;
            case MAINTENANCE:
                buttontext += "maintenance";
                break;
            default:
                System.err.println("Invalid liftstate");
        }
        mStateLabel.setText(buttontext);
    }

    /***
     * method to set the floor of the lift for the display
     * @param floor the floor to set
     */
    public void update_floor(int floor)
    {
        if (floor == 13){
            mFloorLabel.setText("floor: 12B");
        }else {
            mFloorLabel.setText("floor: " + Integer.toString(floor));
        }
    }
}
