package com.egg.biblioteca.controladores;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.exepciones.MyException;
import com.egg.biblioteca.servicios.EditorialServicios;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    EditorialServicios editorialServicios;

    @GetMapping("/registrar")
    public String registrar(){
        return "editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
            try {
                editorialServicios.crearEditorial(nombre);
                modelo.put("exito", "la editorial fue cargada correctamente.");
            } catch (MyException ex) {
                modelo.put("error", ex.getMessage());
                return "editorial_form.html";
            }
        return "index.html";
    
    }
        @GetMapping("/lista")
    public String listar(ModelMap modelo) {


        List<Editorial> editoriales = editorialServicios.listarEditoriales();
        modelo.addAttribute("editoriales", editoriales);
        return "editorial_list.html";
    }
     @GetMapping("/modificar/{id}")
public String modificar(@PathVariable String id, ModelMap model) {
    model.put("editorial", editorialServicios.getOne(id));
    return "editorial_modificar.html";
}

@PostMapping("/modificar/{id}")
public String modificar(@PathVariable String id, String nombre, ModelMap model) {
    try {
        editorialServicios.modificarEditorial(id, nombre);  // Aquí pasamos el id como String
        return "redirect:../lista";
    } catch (MyException ex) {
        model.put("error", ex.getMessage());
        return "editorial_modificar.html";
    }
}

    }

