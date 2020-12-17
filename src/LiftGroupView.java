import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/***
 * Container class for all liftviews, main window
 *
 * @author Jeroen Weltens
 */
public class LiftGroupView extends JFrame implements Observer {
    private ArrayList<LiftModel> mLifts = new ArrayList<>();
    private JPanel mAlarmFix;
    /**
     * constructor that creates all liftmodels and views, binds InternalLiftView and ExternalLiftView together
     * @param numOfLifts the amount of lifts the view frame must contain
     */
    LiftGroupView(int numOfLifts)
    {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel liftpanel = new JPanel();
        liftpanel.setLayout(new BoxLayout(liftpanel, BoxLayout.X_AXIS));
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
            lift.addObserver(this);
            mLifts.add(lift);

            JPanel liftView = new JPanel();
            liftView.setLayout(new BoxLayout(liftView, BoxLayout.X_AXIS));
            liftView.add(new JLabel("lift " + i + ": "));
            liftView.add(internalpanel);
            liftView.add(Box.createHorizontalStrut(10));
            liftView.add(externalpanel);
            liftpanel.add(liftView);
            liftpanel.add(Box.createHorizontalStrut(10));
        }
        getContentPane().add(liftpanel);
        getContentPane().add(Box.createVerticalStrut(10));
        mAlarmFix = new JPanel();
        mAlarmFix.setLayout(new BoxLayout(mAlarmFix, BoxLayout.X_AXIS));
        JLabel alarmlabel = new JLabel("disable alarm: ");
        JButton fixalarm = new JButton("disable");
        fixalarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(LiftModel l : mLifts){
                    l.disable_alarm();
                }
                mAlarmFix.setVisible(false);
                pack();
            }
        });
        mAlarmFix.add(alarmlabel);
        mAlarmFix.add(Box.createHorizontalStrut(20));
        mAlarmFix.add(fixalarm);
        getContentPane().add(mAlarmFix);
        mAlarmFix.setVisible(false);
        pack();
        setVisible(true);
    }

    /**
     * update for when an alarm in a lift is called
     * @post triggers all alarms from other lifts and gives a button to disable the alarms
     * @param o null
     * @param arg null
     */
    @Override
    public void update(Observable o, Object arg) {
        for (LiftModel lift : mLifts) {
            if (lift.getState() == LiftModel.LiftState.ALARM) {
                for (LiftModel l : mLifts) {
                    l.set_alarm();
                }
                mAlarmFix.setVisible(true);
                pack();
            }
        }
    }
}
