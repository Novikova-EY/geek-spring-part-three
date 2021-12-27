package ru.geekbrains.persist;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final Map<Long, Product> productMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        this.save(new Product(1L, "Product 1", "Description 1", new BigDecimal(111111)));
        this.save(new Product(2L, "Product 2", "Description 2", new BigDecimal(222222)));
        this.save(new Product(3L, "Product 3", "Description 3", new BigDecimal(333333)));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            long id = maxId() + 1;
            product.setId(id);
        }
        productMap.put(product.getId(), product);
    }

    @Override
    public void delete(long id) {
        productMap.remove(id);
    }

    public Long maxId() {
        ArrayList<Product> products = new ArrayList<>(productMap.values());
        long maxId = 0;
        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }
        return maxId;
    }
}
