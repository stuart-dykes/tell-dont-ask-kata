package it.gabrieletondi.telldontaskkata.doubles;

import java.util.List;
import java.util.Optional;

import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

public class InMemoryProductCatalog implements ProductCatalog {
	private final List<Product> products;

	public InMemoryProductCatalog( List<Product> products ) {
		this.products = products;
	}

	@Override
	public Optional<Product> getByName( final String name ) {
		return products.stream().filter( p -> p.getName().equals( name ) ).findFirst();
	}
}
