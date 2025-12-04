package com.partenairedecode.orderservice.client;

import com.partenairedecode.grpc.product.Product;
import com.partenairedecode.grpc.product.ProductIdRequest;
import com.partenairedecode.grpc.product.ProductServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
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
        } catch (StatusRuntimeException e) {
            System.err.println("Erreur gRPC lors de la récupération du produit " + productId + ": " + e.getMessage());
            // Dans un cas réel, vous lèveriez une exception métier ici.
            return null;

//          if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
//            // Example: Handle a business logic error
//            System.err.println("Requested product does not exist. Cannot create order.");
//            // Throw a custom business exception for the Order Service to handle
//            throw new ProductNotFoundException("Product ID: " + productId + " not found.");
//
//          } else if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
//            // Example: Handle a service infrastructure error
//            System.err.println("Product Service is currently down. Try again later.");
//            // Implement circuit breaker logic or fallbacks
//            return null;
//          } else {
//            // Re-throw any unexpected error
//            throw e;
//          }

        }
    }
}