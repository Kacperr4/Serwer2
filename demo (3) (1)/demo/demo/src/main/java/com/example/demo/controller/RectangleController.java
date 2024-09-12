package com.example.demo.controller;

import com.example.demo.model.Rectangle;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/rectangle")
public class RectangleController {

    private ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();

    @GetMapping
    public Rectangle rectangle(){
        Rectangle rectangle = new Rectangle(10,10,20,20,"Blue");
        return rectangle;
    }

    @PostMapping("/add")
    public void addRectangle(@RequestBody Rectangle rectangle){
        rectangleArrayList.add(rectangle);
    }

    @GetMapping("/all")
    public ArrayList<Rectangle> getRectangles(){
        return rectangleArrayList;
    }
    @GetMapping("/svg")
    public String getSvg(){
        StringBuilder svgBuilder = new StringBuilder();
        svgBuilder.append("<svg xmlns=\"http://www.w3.org/2000/svg\">\n");

        for(Rectangle rectangle : rectangleArrayList){
            svgBuilder.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" />\n",
                    rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), rectangle.getColor()
            ));
        }
        svgBuilder.append("</svg>");
        return svgBuilder.toString();
    }

}
