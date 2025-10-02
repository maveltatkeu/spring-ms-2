package com.partenairedecode.grpctestclient.client;

import com.partenairedecode.grpc.order.Order;
import com.partenairedecode.grpc.order.OrderItem;
import com.partenairedecode.grpc.order.OrderServiceGrpc;
import com.partenairedecode.grpc.payment.Payment;
import com.partenairedecode.grpc.payment.PaymentServiceGrpc;
import com.partenairedecode.grpc.product.Empty;
import com.partenairedecode.grpc.product.Product;
import com.partenairedecode.grpc.product.ProductIdRequest;
import com.partenairedecode.grpc.product.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class TestRunner implements CommandLineRunner {

    // Injection des stubs gRPC. Tous pointent vers l'adresse globale (Envoy :8080)
    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productStub;

    @GrpcClient("order-service")
    private OrderServiceGrpc.OrderServiceBlockingStub orderStub;

    @GrpcClient("payment-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("======================================================================");
        System.out.println("🚀 Démarrage du test de la chaîne de microservices via Envoy (:8080)...");
        System.out.println("======================================================================");

        // 1. Tester le Product Service (CRUD)
        Product newProduct = testProductService();
        String createdProductId = newProduct.getProductId();

        // 2. Tester le Order Service (Dépend de Product Service)
        Order newOrder = testOrderService(createdProductId);
        String createdOrderId = newOrder.getOrderId();

        // 3. Tester le Payment Service (Dépend de Order Service)
        testPaymentService(createdOrderId, newOrder.getTotalAmount());
        
        System.out.println("======================================================================");
        System.out.println("✅ Tous les tests de la chaîne sont passés avec succès.");
        System.out.println("======================================================================");
    }
    
    //---------------------- PRODUCT SERVICE TEST ----------------------
    private Product testProductService() {
        System.out.println("\n--- 1. TEST PRODUCT SERVICE (CRUD & STREAM) ---");

        // Créer un nouveau produit
        Product productToSave = Product.newBuilder()
                .setProductId("ITEM-XYZ")
                .setName("Test Product via Envoy")
                .setPrice(500.00)
                .setQuantity(20)
                .build();

        Product savedProduct = productStub.saveProduct(productToSave);
        System.out.println("Produit créé: " + savedProduct.getName() + " (ID: " + savedProduct.getProductId() + ")");

        // Lire le produit
        ProductIdRequest request = ProductIdRequest.newBuilder().setProductId(savedProduct.getProductId()).build();
        Product retrievedProduct = productStub.getProductById(request);
        System.out.println("Produit lu: " + retrievedProduct.getName());

        // Lire tous les produits (Streaming)
        Iterator<Product> productIterator = productStub.getAllProducts(Empty.newBuilder().build());
        System.out.print("Produits en flux (streaming):");
        productIterator.forEachRemaining(p -> System.out.print(" " + p.getProductId()));
        System.out.println();
        
        return retrievedProduct;
    }

    //---------------------- ORDER SERVICE TEST ----------------------
    private Order testOrderService(String productId) {
        System.out.println("\n--- 2. TEST ORDER SERVICE (INTER-SERVICE CALL) ---");

        // Créer une commande qui utilise le produit créé ci-dessus
        OrderItem item = OrderItem.newBuilder()
                .setProductId(productId)
                .setQuantity(1)
                .build(); // Le prix unitaire sera récupéré par Order Service via gRPC.

        Order orderToCreate = Order.newBuilder()
                .setCustomerId("CLIENT-TEST")
                .addItems(item)
                .build();

        try {
            Order createdOrder = orderStub.createOrder(orderToCreate);
            System.out.println("Commande créée: " + createdOrder.getOrderId() + 
                               " | Total calculé: " + createdOrder.getTotalAmount());
            return createdOrder;
        } catch (StatusRuntimeException e) {
            System.err.println("ERREUR lors de la création de la commande: " + e.getStatus().getDescription());
            throw e;
        }
    }

    //---------------------- PAYMENT SERVICE TEST ----------------------
    private void testPaymentService(String orderId, double amount) {
        System.out.println("\n--- 3. TEST PAYMENT SERVICE (FULL CHAIN) ---");

        // Traiter le paiement
        Payment paymentToProcess = Payment.newBuilder()
                .setOrderId(orderId)
                .setAmount(amount)
                .setMethod("CREDIT_CARD")
                .build();

        Payment processedPayment = paymentStub.processPayment(paymentToProcess);
        System.out.println("Paiement traité: ID " + processedPayment.getPaymentId() + 
                           " | Statut: " + processedPayment.getStatus());

        // Vérifier le statut de la commande mise à jour (simulé)
        try {
            Order updatedOrder = orderStub.getOrderById(com.partenairedecode.grpc.order.OrderIdRequest.newBuilder().setOrderId(orderId).build());
             System.out.println("Statut de la commande après paiement: " + updatedOrder.getStatus());
        } catch (StatusRuntimeException e) {
             System.err.println("Impossible de vérifier le statut de la commande: " + e.getStatus().getDescription());
        }
    }
}