package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.exepciones.MyException;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

@Service
public class LibroServicios {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
public void crearLibro(Long isbn, String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial) throws MyException {

    validar(isbn, titulo, ejemplares, idAutor, idEditorial);

    // Convertir UUID a String antes de buscar en los repositorios
    Autor autor = autorRepositorio.findById(idAutor.toString()).orElse(null);
    Editorial editorial = editorialRepositorio.findById(idEditorial.toString()).orElse(null);

    if (autor == null) {
        throw new MyException("El autor especificado no existe.");
    }

    if (editorial == null) {
        throw new MyException("La editorial especificada no existe.");
    }

    Libro libro = new Libro();
    libro.setIsbn(isbn);
    libro.setTitulo(titulo);
    libro.setEjemplares(ejemplares);
    libro.setAlta(new Date());
    libro.setAutor(autor);
    libro.setEditorial(editorial);

    libroRepositorio.save(libro);
}


    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {

        List<Libro> libros = new ArrayList<>();

        libros = libroRepositorio.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial)
            throws MyException {
    
        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
    
        // Convertir UUID a String antes de buscar en los repositorios
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor.toString());
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial.toString());
    
        if (respuesta.isEmpty()) {
            throw new MyException("El libro especificado no existe.");
        }
    
        if (respuestaAutor.isEmpty()) {
            throw new MyException("El autor especificado no existe.");
        }
    
        if (respuestaEditorial.isEmpty()) {
            throw new MyException("La editorial especificada no existe.");
        }
    
        Libro libro = respuesta.get();
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAutor(respuestaAutor.get());
        libro.setEditorial(respuestaEditorial.get());
    
        libroRepositorio.save(libro);
    }
    

    private void validar(Long isbn, String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial)
            throws MyException {

        if (isbn == null) {
            throw new MyException("El ISBN no puede ser nulo.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MyException("El título no puede ser nulo o estar vacío.");
        }
        if (ejemplares == null) {
            throw new MyException("La cantidad de ejemplares no puede ser nula.");
        }
        if (idAutor == null) {
            throw new MyException("El ID del autor no puede ser nulo o estar vacío.");
        }
        if (idEditorial == null) {
            throw new MyException("El ID de la editorial no puede ser nulo o estar vacío.");
        }
    }

}
