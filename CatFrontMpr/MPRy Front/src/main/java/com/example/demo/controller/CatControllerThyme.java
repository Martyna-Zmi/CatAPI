package com.example.demo.controller;

import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CatControllerThyme {
    private final CatService service;
    @Autowired
    public CatControllerThyme(CatService service){
        this.service = service;
    }
    @GetMapping(value = "/viewAll")
    public String viewAll(Model model, RedirectAttributes redirectAttributes){
        model.addAttribute("cats", service.findAll());
        return "viewAll";
    }
    @GetMapping(value = "/addCat")
    public String addView(Model model){
        Cat cat = new Cat("",0, "czarny");
        model.addAttribute("cat", cat);
        return "addCat";
    }
    @RequestMapping (value = "/addCat", method = RequestMethod.POST)
    public String addCat(@ModelAttribute Cat cat, Model model){
        service.saveCat(cat);
        return "redirect:/viewAll";
    }
    @GetMapping(value = "/deleteView/{id}")
    public String getDeleteCat(Model model, @PathVariable("id") long id){
        model.addAttribute("cat", service.findById(id));
        return "deleteView";
    }
    @RequestMapping(value = "/deleteView/{id}", method = RequestMethod.POST)
    public String deleteCat(Model model, @PathVariable("id") long id){
        service.deleteById(id);
        return "redirect:/viewAll";
    }
    @GetMapping(value = "/editView/{id}")
    public String getUpdateCat(Model model, @PathVariable("id") long id){
        model.addAttribute("cat", service.findById(id));
        return "editView";
    }
    @RequestMapping(value = "/editView/{id}", method = RequestMethod.POST)
      public String editById(@ModelAttribute Cat cat, Model model){
        service.updateCat(cat);
        return "redirect:/viewAll";
    }
}
