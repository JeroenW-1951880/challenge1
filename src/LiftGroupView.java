import javax.swing.*;

/***
 * Container class for all liftviews, main window
 *
 * @author Jeroen Weltens
 */
public class LiftGroupView extends JFrame {
    /**
     * constructor that creates all liftmodels and views, binds InternalLiftView and ExternalLiftView together
     * @param numOfLifts the amount of lifts the view frame must contain
     */
    LiftGroupView(int numOfLifts)
    {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        for(int i = 1; i < numOfLifts + 1; ++i)
        {
            LiftModel lift = new LiftModel();
            InternalLiftView internal = new InternalLiftView(lift);
            ExternalLiftView external = new ExternalLiftView(lift);

            LiftController controller = new LiftController(lift, internal, external);

            JPanel internalpanel = new JPanel();
            internalpanel.setLayout(new BoxLayout(internalpanel, BoxLayout.Y_AXIS));
            internalpanel.add(new JLabel("in lift:"));
            internalpanel.add(Box.createVerticalStrut(10));
            internalpanel.add(internal);

            JPanel externalpanel = new JPanel();
            externalpanel.setLayout(new BoxLayout(externalpanel, BoxLayout.Y_AXIS));
            externalpanel.add(new JLabel("outside:"));
            externalpanel.add(Box.createVerticalStrut(10));
            externalpanel.add(external);

            lift.addObserver(internal);
            lift.addObserver(external);

            JPanel liftView = new JPanel();
            liftView.setLayout(new BoxLayout(liftView, BoxLayout.X_AXIS));
            liftView.add(new JLabel("lift " + i + ": "));
            liftView.add(internalpanel);
            liftView.add(Box.createHorizontalStrut(10));
            liftView.add(externalpanel);
            getContentPane().add(liftView);
            getContentPane().add(Box.createHorizontalStrut(10));
        }
        pack();
        setVisible(true);
    }
}
