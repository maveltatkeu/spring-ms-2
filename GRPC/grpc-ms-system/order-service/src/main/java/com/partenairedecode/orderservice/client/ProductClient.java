package com.partenairedecode.orderservice.client;

import com.partenairedecode.grpc.product.Product;
import com.partenairedecode.grpc.product.ProductIdRequest;
import com.partenairedecode.grpc.product.ProductServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class ProductClient {

    // 🔑 Annotation pour injecter le stub gRPC du service Product
    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productBlockingStub;

    /**
     * Appelle le Product Service pour récupérer les détails d'un produit.
     * @param productId L'ID du produit à rechercher.
     * @return Le message Product s'il est trouvé.
     */
    public Product getProductDetails(String productId) {
        ProductIdRequest request = ProductIdRequest.newBuilder()
                .setProductId(productId)
                .build();
        
        try {
            // Appel synchrone bloquant (BlockingStub)
            return productBlockingStub.getProductById(request);
        } catch (Exception e) {
            System.err.println("Erreur gRPC lors de la récupération du produit " + productId + ": " + e.getMessage());
            // Dans un cas réel, vous lèveriez une exception métier ici.
            return null; 
        }
    }
}