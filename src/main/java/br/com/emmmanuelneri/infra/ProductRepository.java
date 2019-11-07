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
                        ":product/name", product.getName()
                )
        ));
    }

    public List<Product> list() {
        return Peer.q(
                "[:find ?code ?name" +
                        " :where " +
                        "   [?product :product/code ?code]" +
                        "   [?product :product/name ?name]" +
                        "]",
                this.connection.db())
                .stream()
                .map(fields -> new Product((String) fields.get(0), (String) fields.get(1)))
                .collect(Collectors.toList());
    }

}
