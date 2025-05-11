package todolistweb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import todolistweb.trayicon.TrayIconApp;

@SpringBootApplication
@ComponentScan(basePackages = {"todolistweb", "todolistweb.trayicon"})
@EnableJpaRepositories(basePackages = "todolistweb.repository")
public class ToDoListWeb implements CommandLineRunner {

	static {
	    System.setProperty("java.awt.headless", "false");
	}
	
    private final TrayIconApp trayIconApp;

    public ToDoListWeb(TrayIconApp trayIconApp) {
        this.trayIconApp = trayIconApp;
    }

    public static void main(String[] args) {
        SpringApplication.run(ToDoListWeb.class, args);
    }

    @Override
    public void run(String... args) {
        trayIconApp.setupTrayIcon();
    }
}

