package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping("/adjust-brigthness")
    public String adjustBrightness(@RequestParam String imageBase64, @RequestParam int brightness) throws IOException{
        BufferedImage bi = decodeBase64ToImage(imageBase64);
        BufferedImage brightened = adjustImageBrightness(bi, brightness);
        String brightenedEncoded = encodeImageToBase64(brightened);
        return brightenedEncoded;
    }

    @GetMapping("/adjust-brigthness/image")
    public void adjustBrightness(@RequestParam String imageBase64, @RequestParam int brightness, HttpServletResponse response) throws IOException{
        BufferedImage bi = decodeBase64ToImage(imageBase64);
        BufferedImage brightened = adjustImageBrightness(bi, brightness);
        response.setContentType("image/png");

        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(brightened, "png", out);
            out.flush();
        }

    }

    private BufferedImage decodeBase64ToImage(String base64String) throws IOException {
        byte[] imageBytes = Base64.decodeBase64(base64String);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bais);
    }

    private String encodeImageToBase64(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeBase64String(imageBytes);
    }

    private BufferedImage adjustImageBrightness(BufferedImage bufferedImage , int brightness){
        float scaleFactor = 1.0f + (brightness / 100.0f);
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        BufferedImage brightenedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        Graphics2D g2d = brightenedImage.createGraphics();
        g2d.drawImage(bufferedImage, op, 0,0);
        g2d.dispose();
        return brightenedImage;
    }
}
