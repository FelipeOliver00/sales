package com.sales.din.service;

import com.sales.din.dto.ProductSaleDTO;
import com.sales.din.dto.ProductInfoDTO;
import com.sales.din.dto.SaleDTO;
import com.sales.din.dto.SaleInfoDTO;
import com.sales.din.entity.ItemSale;
import com.sales.din.entity.Product;
import com.sales.din.entity.Sale;
import com.sales.din.entity.User;
import com.sales.din.exceptions.InvalidOperationException;
import com.sales.din.exceptions.NoItemException;
import com.sales.din.repository.ItemSaleRepository;
import com.sales.din.repository.ProductRepository;
import com.sales.din.repository.SaleRepository;
import com.sales.din.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ItemSaleRepository itemSaleRepository;

    /*
        {
            "user": "Fulano",
            "data": "03/07/2023",
            "products":[
                {
                    "description": "Notebook dell",
                    "quantity": 1
                }
            ]
        }
     */

    public List<SaleInfoDTO> findAll() {
        return saleRepository.findAll().stream().map(sale -> getSaleInfo(sale)).collect(Collectors.toList());
    }

    private SaleInfoDTO getSaleInfo(Sale sale) {

        var products = getProductInfo(sale.getItems());
        BigDecimal total = getTotal(products);

        return SaleInfoDTO.builder()
                .user(sale.getUser().getName())
                .date(sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .products(products)
                .total(total)
                .build();
    }

    private BigDecimal getTotal(List<ProductInfoDTO> products) {
        BigDecimal total = new BigDecimal(0);
        for (int i =0; i<products.size(); i++){
            ProductInfoDTO currentProduct = products.get(1);
            total = total.add(products.get(i).getPrice()
                    .multiply(new BigDecimal(currentProduct.getQuantity())));
        }
        return total;
    }

    private List<ProductInfoDTO> getProductInfo(List<ItemSale> items) {

        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        return items.stream().map(
                item -> ProductInfoDTO
                        .builder()
                        .id(item.getId())
                        .price(item.getProduct().getPrice())
                        .description(item.getProduct()
                        .getDescription())
                .quantity(item.getQuantity()).build()
        ).collect(Collectors.toList());
    }
    @Transactional
    public long save(SaleDTO sale) {

        User user = userRepository.findById(sale.getUserId())
                .orElseThrow(() -> new NoItemException("Usuário não encontrado!"));

        Sale newSale = new Sale();
        newSale.setUser(user);
        newSale.setDate(LocalDate.now());
        List<ItemSale> items = getItemSale(sale.getItems());

        newSale = saleRepository.save(newSale);
        saveItemSale(items, newSale);

        return newSale.getId();

    }

    private void saveItemSale(List<ItemSale> items, Sale newSale) {
        for (ItemSale item: items) {
            item.setSale(newSale);
            itemSaleRepository.save(item);
        }
    }

    private List<ItemSale> getItemSale(List<ProductSaleDTO> products) {

        if (products.isEmpty()) {
            throw new InvalidOperationException("Não é possível adicionar a venda sem itens!");
        }

        return products.stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NoItemException("Item da venda não encontrado"));

            ItemSale itemSale = new ItemSale();
            itemSale.setProduct(product);
            itemSale.setQuantity(item.getQuantity());

            if(product.getQuantity() == 0) {
                throw new IllegalArgumentException("Produto sem estoque");
            } else if (product.getQuantity() < item.getQuantity()){
                throw new IllegalArgumentException(String.format("A quantidade de itens da venda (%s) " +
                        "é maior do que quantidade disponível no estoque (%s)", item.getQuantity(), product.getQuantity()));
            }

            int total = product.getQuantity() - item.getQuantity();
            product.setQuantity(total);
            productRepository.save(product);

            return itemSale;
        }).collect(Collectors.toList());
    }

    public SaleInfoDTO getById(long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new NoItemException("Venda não encontrada!"));
        return getSaleInfo(sale);
    }
}
