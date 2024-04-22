package com.adminzoggy.adminzoggy.controllers;



import com.adminzoggy.adminzoggy.model.AdminFood;
import com.adminzoggy.adminzoggy.model.AdminFoodDto;
import com.adminzoggy.adminzoggy.services.AdminFoodRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/adminfoodss")
public class AdminFoodController {
    @Autowired
    private AdminFoodRepository repo;

    @GetMapping({"", "/"})
    public String showAdminFoodList(Model model) {
        List<AdminFood> adminfoodss = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("adminfoodss", adminfoodss);
        return "adminfoodss/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        AdminFoodDto adminfoodDto = new AdminFoodDto();
        model.addAttribute("adminfoodDto", adminfoodDto);
        return "adminfoodss/CreateAdminFood";
    }

    @PostMapping("/create")
    public String createAdminFood(
            @Valid @ModelAttribute AdminFoodDto adminfoodDto,
            BindingResult result
    ) {
        if (adminfoodDto.getImageFilename().isEmpty()) {
            result.addError(new FieldError("adminfoodDto", "image_filename", "The image file is required"));
        }

        if (result.hasErrors()) {
            return "adminfoodss/CreateAdminFood";
        }

        //save image file
        MultipartFile image = adminfoodDto.getImageFilename();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception" + ex.getMessage());
        }

        AdminFood adminfood = new AdminFood();
        adminfood.setName(adminfoodDto.getName());
        adminfood.setType(adminfoodDto.getType());
        adminfood.setCategory(adminfoodDto.getCategory());
        adminfood.setPrice(adminfoodDto.getPrice());
        adminfood.setDescription(adminfoodDto.getDescription());
        adminfood.setCreatedAt(createdAt);
        adminfood.setImageFilename(storageFileName);

        repo.save(adminfood);


        return "redirect:/adminfoodss";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model,
                               @RequestParam int id) {
        try {
            AdminFood adminfood = repo.findById(id).get();
            model.addAttribute("adminfood", adminfood);

            AdminFoodDto adminfoodDto = new AdminFoodDto();
            adminfood.setName(adminfood.getName());
            adminfood.setType(adminfood.getType());
            adminfood.setCategory(adminfood.getCategory());
            adminfood.setPrice(adminfood.getPrice());
            adminfood.setDescription(adminfood.getDescription());

            model.addAttribute("adminfoodDto", adminfoodDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/adminfoodss";
        }
        return "adminfoodss/EditAdminFood";
    }

    @PostMapping("/edit")
    public String updateAdminFood(
            Model model, @RequestParam int id,
            @Valid @ModelAttribute AdminFoodDto adminfoodDto,
            BindingResult result
    ) {
        try {
            AdminFood adminfood = repo.findById(id).get();
            model.addAttribute("adminfood", adminfood);

            if (result.hasErrors()) {
                return "adminfoodss/EditAdminFood";
            }

            String uploadDir = "public/images/"; // Declare uploadDir here

            if (!adminfoodDto.getImageFilename().isEmpty()) {
                Path OldImagePath = Paths.get(uploadDir + adminfood.getImageFilename());

                try {
                    Files.delete(OldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                //save new image file
                MultipartFile image = adminfoodDto.getImageFilename();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                adminfood.setImageFilename(storageFileName);
            }

            // Set other properties outside the if block
            adminfood.setName(adminfoodDto.getName());
            adminfood.setType(adminfoodDto.getType());
            adminfood.setCategory(adminfoodDto.getCategory());
            adminfood.setPrice(adminfoodDto.getPrice());
            adminfood.setDescription(adminfoodDto.getDescription());

            repo.save(adminfood);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        // Move this block inside the try-catch block
        return "redirect:/adminfoodss"; // Move this line inside the try-catch block
    }

    @GetMapping("/delete")
    public String deleteAdminFood(@RequestParam int id) {
        try {
            // Retrieve the AdminFood entity by its id
            AdminFood adminfood = repo.findById(id).orElse(null);

            if (adminfood != null) {
                // Delete the food image
                Path imagePath = Paths.get("public/images/" + adminfood.getImageFilename());
                try {
                    Files.delete(imagePath);
                } catch (IOException ex) {
                    System.out.println("Exception while deleting image: " + ex.getMessage());
                }

                // Delete the AdminFood entity from the repository
                repo.delete(adminfood);
            } else {
                System.out.println("AdminFood with id " + id + " not found.");
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/adminfoodss";
    }

}

