package com.seti.pruebaTecnicaSeti.repository;

import com.seti.pruebaTecnicaSeti.entity.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
}
