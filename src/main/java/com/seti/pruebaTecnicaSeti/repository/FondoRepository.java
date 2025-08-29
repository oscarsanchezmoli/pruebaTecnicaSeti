package com.seti.pruebaTecnicaSeti.repository;

import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.entity.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FondoRepository extends MongoRepository<Fondo, String>  {
}
