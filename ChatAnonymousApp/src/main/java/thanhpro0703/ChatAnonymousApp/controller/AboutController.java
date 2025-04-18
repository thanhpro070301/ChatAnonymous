package thanhpro0703.ChatAnonymousApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import thanhpro0703.ChatAnonymousApp.service.RoomManager;

@Controller
public class AboutController {
    
    private final RoomManager roomManager;
    
    public AboutController(RoomManager roomManager) {
        this.roomManager = roomManager;
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("waitingUsers", roomManager.getWaitingCount());
        model.addAttribute("activePairs", roomManager.getPairedCount());
        model.addAttribute("currentPage", "about");
        return "about";
    }
} 