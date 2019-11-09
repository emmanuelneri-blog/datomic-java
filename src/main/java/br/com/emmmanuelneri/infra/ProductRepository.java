package br.com.emmmanuelneri.infra;

import br.com.emmmanuelneri.model.Product;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductRepository {

    private final Connection connection;

    public void save(final Product product) {
        try {
            final Map map = this.connection.transact(Util.list(
                    Util.map(
                            ":product/code", product.getCode(),
                            ":product/name", product.getName(),
                            ":product/description", Objects.isNull(product.getDescription()) ? "" : product.getDescription()
                    )
            )).get();
            System.out.println(map);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
        final String code = (String) fields.get(0);
        final String name = (String) fields.get(1);
        final String description = (String) fields.get(2);
        return new Product(code, name, description);
    }

}
