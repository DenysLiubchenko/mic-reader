package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.generated.api.ProductApi;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController implements ProductApi {

}
