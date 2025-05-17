package todolistweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class DebugController {

    @GetMapping("/session-debug")
    @ResponseBody
    public String debugSession(HttpSession session) {
        return "Session timeout (sek): " + session.getMaxInactiveInterval();
    }
}
