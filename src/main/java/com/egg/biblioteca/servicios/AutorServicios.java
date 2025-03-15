package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.exepciones.MyException;
import com.egg.biblioteca.repositorios.AutorRepositorio;

@Service
public class AutorServicios {

    @Autowired
    private AutorRepositorio autorRepositorio;


    @Transactional
    public void crearAutor( String nombre) throws MyException{
        
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);

        autorRepositorio.save(autor);
    }
    
    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {

        List<Autor> autores = new ArrayList<>();

        autores = autorRepositorio.findAll();
        return autores;
    }
    
    @Transactional
    public void modificarAutor(String nombre, UUID id) throws MyException{
        
        validar(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);

        } else {
            throw new MyException("No se encontró un autor con el ID especificado");
        }
    }
    
    @Transactional
    public void eliminar(UUID id) throws MyException{
        Optional<Autor> autorOpt = autorRepositorio.findById(id);
        if (autorOpt.isPresent()) {
            autorRepositorio.delete(autorOpt.get());
        } else {
            throw new MyException("El autor con el ID especificado no existe");
        }

    }
    @Transactional(readOnly = true)
    public Autor getOne(UUID id) {
        return autorRepositorio.findById(id).orElse(null);
    }
    
    private void validar(String nombre) throws MyException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MyException("El nombre no puede ser nulo o estar vacío");
        }
    }
    
}