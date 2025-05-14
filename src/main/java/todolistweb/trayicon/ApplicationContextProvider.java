package todolistweb.trayicon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** ApplicationContextProvider für TrayIcon
 *  * @author Michael Schmid
 *  * @version 1.0
 *  *  Diese Klasse implementiert die ApplicationContextAware-Schnittstelle,
 *  *  um den ApplicationContext zu erhalten und ihn in einer statischen Variable zu speichern.
 *  *  Dadurch kann der ApplicationContext von anderen Klassen aus abgerufen werden.
 *  *  * @see ApplicationContextAware
 *  *  * @see ApplicationContext
 *  *  * @see Component
 *  *  * @see TrayIcon
 *  *  * @see java.awt.SystemTray
 *  *  * @see java.awt.TrayIcon
 *  *  * @see java.awt.event.ActionListener
 *  *  * @see java.awt.event.MouseAdapter
 *  *  * @see java.awt.event.MouseEvent
 *  *  * @see java.awt.event.MouseListener
 *  *  * @see java.awt.event.MouseMotionAdapter
 *  *  * @see java.awt.event.MouseMotionListener
 *  *  * @see java.awt.event.MouseWheelEvent
 *  *  * @see java.awt.event.MouseWheelListener
 *  *  * @see java.awt.event.WindowAdapter
 *  *  * @see java.awt.event.WindowEvent
 *  *  * @see java.awt.event.WindowListener
 *  *  * @see java.awt.event.WindowStateListener
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
