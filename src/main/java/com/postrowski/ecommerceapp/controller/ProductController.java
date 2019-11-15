package com.postrowski.ecommerceapp.controller;

import com.postrowski.ecommerceapp.config.SwaggerConstants;
import com.postrowski.ecommerceapp.controller.exception.ProductNotFoundException;
import com.postrowski.ecommerceapp.dto.ProductCreateDto;
import com.postrowski.ecommerceapp.dto.ProductUpdateDto;
import com.postrowski.ecommerceapp.model.Product;
import com.postrowski.ecommerceapp.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Api(tags = SwaggerConstants.SERVICE_NAME)
public class ProductController {


    class UriConstants {

        static final String ID = "productId";

        static final String BASE_PATH = "/products";
        static final String BASE_PATH_WITH_ID = BASE_PATH + "/{" + ID + "}";
    }

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation("Find all Products")
    @GetMapping(UriConstants.BASE_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

//    @ApiOperation("Find a Product")
//    @GetMapping(UriConstants.BASE_PATH_WITH_ID)
//    @ResponseStatus(value = HttpStatus.OK)
//    public Product getProduct(@NotNull @PathVariable(UriConstants.ID) Long productId) {
//        return productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException(productId));
//    }

    @ApiOperation("Create a Product")
    @PostMapping(UriConstants.BASE_PATH)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody ProductCreateDto createDto) {

        Product product = Product.builder()
                .name(createDto.getName())
                .price(createDto.getPrice())
                .build();

        return productRepository.save(product);
    }

    @ApiOperation("Update a Product")
    @PatchMapping(UriConstants.BASE_PATH_WITH_ID)
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public Product updateProduct(@NotNull @PathVariable(UriConstants.ID) Long productId,
                                 @RequestBody @Valid ProductUpdateDto updateDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (updateDto.getName() != null) {
            product.setName(updateDto.getName());
        }

        if (updateDto.getPrice() != null) {
            product.setPrice(updateDto.getPrice());
        }

        return productRepository.save(product);
    }

}
