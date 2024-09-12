package com.example.demo.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageFormController {

    private static final String UPLOADED_FOLDER = "uploaded/";

    @GetMapping("/")
    public String showIndex(){
        return "index";
    }

    @PostMapping("/upload")
    public String uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("brightness") int brightness,
            Model model
            ){
        if(file.isEmpty()){
            return "index";
        }

        try{
            File directory = new File(UPLOADED_FOLDER);
            if(!directory.exists()){
                directory.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, file.getBytes());

            BufferedImage originalImage = ImageIO.read(path.toFile());
            BufferedImage brightenedImage = adjustImageBrightness(originalImage, brightness);
            String encodedImage = encodeImageToBase64(brightenedImage);

            model.addAttribute("imageBase64", encodedImage);
            model.addAttribute("imageName", fileName);
            return "image";


        } catch(IOException e){
            e.printStackTrace();
            return "index";
        }
    }

    private BufferedImage adjustImageBrightness(BufferedImage image, int brightness) {
        float scaleFactor = 1.0f + (brightness / 100.0f);
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        BufferedImage brightenedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = brightenedImage.createGraphics();
        g2d.drawImage(image, op, 0, 0);
        g2d.dispose();
        return brightenedImage;
    }

    private String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeBase64String(imageBytes);
    }
}
