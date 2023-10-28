package com.service.profile.controller;

import com.service.profile.service.Impl.SystemServiceImpl;
import com.service.profile.systemDTOs.CardDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemController {
    final SystemServiceImpl systemService;
    @Autowired
    public SystemController(SystemServiceImpl systemService) {
        this.systemService = systemService;
    }

    @PostMapping("/update-card")
    public String updateCard(@RequestBody CardDataTransfer cardDataTransfer) throws RuntimeException{
        return systemService.updateCard(cardDataTransfer);
    }
}
