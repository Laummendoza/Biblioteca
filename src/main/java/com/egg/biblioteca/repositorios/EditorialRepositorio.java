package com.egg.biblioteca.repositorios;

import java.util.UUID;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.egg.biblioteca.entidades.Editorial;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, UUID> {

    //Optional<Editorial> findById(UUID idEditorial);

}
