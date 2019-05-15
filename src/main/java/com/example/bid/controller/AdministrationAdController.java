package com.example.bid.controller;

import com.example.bid.domain.Ad;
import com.example.bid.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/ad")
public class AdministrationAdController {
    private final AdRepository adRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public AdministrationAdController(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @GetMapping("")
    public String getAdManagePage(
            Model model,
            @RequestParam(required = false) String[] messages
    ) {
        List<Ad> ads = adRepository.findAll();
        model.addAttribute("ads", ads.isEmpty() ? null : ads);
        model.addAttribute("messages", (messages == null) ? new ArrayList<>() : Arrays.asList(messages));
        return "administration/ad";
    }

    @PostMapping("/delete")
    public RedirectView deleteAd(
            @RequestParam(name = "pk")Long pk,
            RedirectAttributes attributes
    ) {
        List<String> messages = new ArrayList<>();
        Ad ad = adRepository.getById(pk);
        adRepository.delete(ad);
        File dir = new File(uploadPath + "/" + ad.getAdvertisement());
        if (dir.exists()) {
            dir.delete();
        }
        messages.add("Ad with id " + pk + " deleted!");
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/ad");
    }

    @PostMapping("/new")
    public RedirectView createAd(
            @RequestParam(name = "image")MultipartFile image,
            RedirectAttributes attributes
            ) throws IOException {
        List<String> messages = new ArrayList<>();
        if (image != null) {
            Ad ad = new Ad();
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String uniqueName = UUID.randomUUID().toString();
            String resultName =uniqueName + "." + image.getOriginalFilename();
            image.transferTo(new File(uploadPath + "/" + resultName));
            ad.setAdvertisement(resultName);
            adRepository.save(ad);
            messages.add("Advertisement successfully added!");
        } else {
            messages.add("Cannot add the advertisement!");
        }
        attributes.addAttribute("messages", toStringArray(messages));
        return new RedirectView("/admin/ad");
    }

    private static String[] toStringArray(List<String> arrayList) {
        String[] str = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++)
            str[i] = arrayList.get(i);
        return str;
    }
}
