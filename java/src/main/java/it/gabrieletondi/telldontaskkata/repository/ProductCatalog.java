package it.gabrieletondi.telldontaskkata.repository;

import java.util.Optional;

import it.gabrieletondi.telldontaskkata.domain.Product;

public interface ProductCatalog {
    Optional<Product> getByName( String name );
}
