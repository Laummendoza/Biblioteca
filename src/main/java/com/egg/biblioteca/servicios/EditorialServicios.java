package com.egg.biblioteca.servicios;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.exepciones.MyException;
import com.egg.biblioteca.repositorios.EditorialRepositorio;



@Service
public class EditorialServicios {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearEditorial(String nombre) throws MyException {

        validar(nombre);
        Editorial editorial =new Editorial();// Instancio un objeto del tipo Editorial
        editorial.setNombre(nombre);// Seteo el atributo, con el valor recibido como parámetro
        editorialRepositorio.save(editorial);// Persisto el dato en mi BBDD
    }
@Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        
        List<Editorial> editoriales = new ArrayList<>();


        editoriales = editorialRepositorio.findAll();
        return editoriales;
    }

    @Transactional
    public void modificarEditorial(UUID id, String nombre) throws MyException{
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        } else {
            throw new MyException("No se encontró una editorial con el ID especificado");
        }
    }

    @Transactional
    public void eliminar(UUID id) throws MyException{
        Optional<Editorial> editorialOpt = editorialRepositorio.findById(id);
        if (editorialOpt.isPresent()) {
            editorialRepositorio.delete(editorialOpt.get());
        } else {
            throw new MyException("La editorial con el ID especificado no existe");
        }
    }

    private void validar(String nombre) throws MyException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MyException("El nombre de la editorial no puede ser nulo o estar vacío");
        }
    }

}
