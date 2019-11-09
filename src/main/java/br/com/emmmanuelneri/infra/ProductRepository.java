package br.com.emmmanuelneri.infra;

import br.com.emmmanuelneri.model.Product;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductRepository {

    private final Connection connection;

    public void save(final Product product) {
        this.connection.transact(Util.list(
                Util.map(
                        ":product/code", product.getCode(),
                        ":product/name", product.getName(),
                        ":product/description", product.getDescription()
                )
        ));
    }

    public List<Product> list() {
        return Peer.q(
                "[:find ?code ?name ?description" +
                        " :where " +
                        "   [?product :product/code ?code]" +
                        "   [?product :product/name ?name]" +
                        "   [?product :product/description ?description]" +
                        "]",
                this.connection.db())
                .stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    private Product toProduct(final List<Object> fields) {
        return new Product((String) fields.get(0), (String) fields.get(1), (String) fields.get(2));
    }

}
