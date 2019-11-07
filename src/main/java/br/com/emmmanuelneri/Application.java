package br.com.emmmanuelneri;

import br.com.emmmanuelneri.infra.Datomic;
import br.com.emmmanuelneri.infra.ProductRepository;
import br.com.emmmanuelneri.model.Product;
import datomic.Connection;

import java.util.List;

public class Application {

    public static void main(final String[] args) {
        final Datomic datomic = Datomic.create();
        datomic.createDatabase();

        final Connection connection = datomic.connect();
        datomic.setupSchema(connection, "product-schema.edn");

        final ProductRepository productRepository = new ProductRepository(connection);

        final Product product = new Product("123", "Product");
        productRepository.save(product);

        product.setName("Product Updated");
        productRepository.save(product);

        final List<Product> products = productRepository.list();
        System.out.println(products);
    }
}
