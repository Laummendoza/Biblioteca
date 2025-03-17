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

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
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
    public String registrar(ModelMap model) {
        List<Autor> autores = autorServicios.listarAutores();
        List<Editorial> editoriales = editorialServicios.listarEditoriales();
        model.addAttribute("autores", autores);
        model.addAttribute("editoriales", editoriales);
        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {
        try {

            UUID autorUUID = (idAutor != null && !idAutor.isEmpty()) ? UUID.fromString(idAutor) : null;
            UUID editorialUUID = (idEditorial != null && !idEditorial.isEmpty()
                    && idEditorial.matches("^[0-9a-fA-F-]{36}$")) ? UUID.fromString(idEditorial) : null;

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

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        // List<Libro> libros = libroServicio.listarLibros();
        // model.addAttribute("libros", libros);
        modelo.addAttribute("libros", libroServicios.listarLibros()); // es lo mismo pero en una sola línea

        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap model) {
        Libro libro = libroServicios.getOne(isbn);
        model.put("libro", libroServicios.getOne(isbn));
        model.addAttribute("autores", autorServicios.listarAutores()); // Agregar autores para que nos muestre la lista
                                                                       // de autores
        model.addAttribute("editoriales", editorialServicios.listarEditoriales()); // Agregar editoriales para que nos
                                                                                   // muestre la lista de autores

        model.addAttribute("autorSeleccionado", libro.getAutor().getId()); // UUID del autor actual para que ya aparezca
                                                                           // seleccionado
        model.addAttribute("editorialSeleccionada", libro.getEditorial().getId()); // UUID de la editorial actual para
                                                                                   // que ya aparezca seleccionad
        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable @RequestParam(required = false) Long isbn, @RequestParam String titulo, 
    @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial, ModelMap model) {
        try {
            UUID autorUUID = (idAutor != null && !idAutor.isEmpty()) ? UUID.fromString(idAutor) : null;
            UUID editorialUUID = (idEditorial != null && !idEditorial.isEmpty()) ? UUID.fromString(idEditorial) : null;

            if (autorUUID == null || editorialUUID == null) {
                throw new MyException("Debe seleccionar un autor y una editorial válidos.");
            }

            libroServicios.modificarLibro(isbn, titulo, ejemplares, autorUUID, editorialUUID);
            return "redirect:../lista";

        } catch (MyException ex) {
            model.addAttribute("autores", autorServicios.listarAutores());
            model.addAttribute("editoriales", editorialServicios.listarEditoriales());
            model.put("error", ex.getMessage());
            return "libro_modificar.html";
        }
    } 

}
