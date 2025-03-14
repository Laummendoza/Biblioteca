package com.egg.biblioteca.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.biblioteca.exepciones.MyException;
import com.egg.biblioteca.servicios.AutorServicios;
import com.egg.biblioteca.servicios.EditorialServicios;
import com.egg.biblioteca.servicios.LibroServicios;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicios libroServicios;
    @Autowired
    private AutorServicios autorServicios;
    @Autowired
    private EditorialServicios editorialServicios;

    @GetMapping("/registrar")
    public String registrar() {
        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam(required=false) String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {
        try {

            UUID autorUUID = (idAutor != null && !idAutor.isEmpty()) ? UUID.fromString(idAutor) : null;
            UUID editorialUUID = (idEditorial != null && !idEditorial.isEmpty() && idEditorial.matches("^[0-9a-fA-F-]{36}$")) ? UUID.fromString(idEditorial) : null;

            if (autorUUID == null || editorialUUID == null) {
                throw new MyException("Debe seleccionar un autor y una editorial válidos.");
            }

            libroServicios.crearLibro(isbn, titulo, ejemplares, autorUUID, editorialUUID);
            modelo.put("exito", "El libro fue cargado correctamente.");

        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "libro_form.html"; // volvemos a cargar el formulario.
        }
        return "index.html";
    }
}
