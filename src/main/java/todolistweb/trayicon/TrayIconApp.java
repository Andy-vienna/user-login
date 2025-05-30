package todolistweb.trayicon;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import todolistweb.version.VersionProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/** TrayIconApp für ToDoList-Web
 *  * @author Michael Schmid
 *  * @version 1.0
 *  *  Diese Klasse implementiert die TrayIcon-Funktionalität für die ToDoList-Web-Anwendung.
 *  *  * @see SystemTray
 *  *  * @see TrayIcon
 *  *  * @see PopupMenu
 *  *  * @see MenuItem
 *  *  * @see Image
 *  *  * @see ImageIO
 *  *  * @see IOException
 *  *  * @see AWTException
 *  *  * @see SpringApplication
 *  *  * @see ApplicationContextProvider
 */
@Component
public class TrayIconApp {
	
	private VersionProvider versionProvider = new VersionProvider();

    public TrayIconApp(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    public void setupTrayIcon() {
        if (!SystemTray.isSupported()) return;

        try {
            SystemTray tray = SystemTray.getSystemTray();

            Image image = ImageIO.read(getClass().getResourceAsStream("/static/images/logo.png"));

            PopupMenu popup = new PopupMenu();
            MenuItem exitItem = new MenuItem("Beenden");
            exitItem.addActionListener(_ -> {
                tray.remove(tray.getTrayIcons()[0]);
                SpringApplication.exit(ApplicationContextProvider.getContext(), () -> 0);
                System.exit(0);
            });
            popup.add(exitItem);

            TrayIcon trayIcon = new TrayIcon(image, "ToDoList-Web " + versionProvider.getVersion() , popup);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }
}
