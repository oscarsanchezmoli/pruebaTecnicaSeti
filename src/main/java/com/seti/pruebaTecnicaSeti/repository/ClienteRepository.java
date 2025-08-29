package com.seti.pruebaTecnicaSeti.repository;

import com.seti.pruebaTecnicaSeti.entity.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Optional<Cliente> findByEmail(String email);
}
