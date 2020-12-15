import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/***
 * interface to add general methods of LiftViews
 *
 * @author Jeroen Weltens
 */
public interface LiftView extends Observer {
    @Override
    void update(Observable o, Object arg);


}
