package com.example.backend.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/uploads")
public class ResourcesServer {

    // Product images server
    @GetMapping("/products/{fileName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) {
        Path filePath = Paths.get("uploads/products").resolve(fileName).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }

    //TODO : Profile pictures server
}
