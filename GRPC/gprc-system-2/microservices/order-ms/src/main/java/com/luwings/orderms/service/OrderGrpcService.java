package com.luwings.orderms.service;

import com.google.protobuf.Timestamp;
import com.luwings.orderms.client.ProductClient;
import com.partenairedecode.grpc.order.Empty;
import com.partenairedecode.grpc.order.Order;
import com.partenairedecode.grpc.order.OrderIdRequest;
import com.partenairedecode.grpc.order.OrderServiceGrpc;
import com.partenairedecode.grpc.product.OperationStatus;
import com.partenairedecode.grpc.product.Product;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    // 🔑 Injection du client pour le service produit
    private final ProductClient productClient;

    // 🔑 Création de données factices pour simuler le repository
    private final List<Order> mockOrders = initializeMockOrders();

    // Méthode utilitaire pour créer des données de test
    private List<Order> initializeMockOrders() {
        List<Order> orders = new ArrayList<>();

        Timestamp now = Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build();

        // Commande 1
        orders.add(Order.newBuilder()
            .setOrderId("ORD-001")
            .setCustomerId("CUST-A")
            .setTotalAmount(1200.00)
            .setStatus("PENDING")
            .setCreatedAt(now)
            .addItems(com.partenairedecode.grpc.order.OrderItem.newBuilder().setProductId("P001").setQuantity(1).setUnitPrice(1200.00))
            .build());

        // Commande 2
        orders.add(Order.newBuilder()
            .setOrderId("ORD-002")
            .setCustomerId("CUST-B")
            .setTotalAmount(80.00)
            .setStatus("PAID")
            .setCreatedAt(now)
            .addItems(com.partenairedecode.grpc.order.OrderItem.newBuilder().setProductId("P003").setQuantity(1).setUnitPrice(80.00))
            .build());
        return orders;
    }

    /**
     * Crée une nouvelle commande après avoir validé les produits.
     */
    @Override
    public void createOrder(Order request, StreamObserver<Order> responseObserver) {
        double calculatedTotal = 0;
        
        // 1. Validation de chaque article de la commande via gRPC
        for (com.partenairedecode.grpc.order.OrderItem item : request.getItemsList()) {
            Product productDetails = productClient.getProductDetails(item.getProductId());
            
            // Si le produit n'existe pas ou la quantité est insuffisante
            if (productDetails == null || productDetails.getQuantity() < item.getQuantity()) {
                responseObserver.onError(Status.FAILED_PRECONDITION
                        .withDescription("Produit invalide ou stock insuffisant pour ID: " + item.getProductId())
                        .asRuntimeException());
                return;
            }
            
            // Calcul du total (utilisant le prix du Product Service)
            calculatedTotal += productDetails.getPrice() * item.getQuantity();
        }

        // 2. Création et Sauvegarde de la commande (logique JPA simplifiée)
        String newOrderId = UUID.randomUUID().toString();
        
        // 3. Construction de la réponse (incluant le statut initial PENDING)
        Order response = request.toBuilder()
                .setOrderId(newOrderId)
                .setTotalAmount(calculatedTotal)
                .setStatus("PENDING")
                .setCreatedAt(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
                .build();

        log.info("Order Created: {}", response);
        // 4. Envoi de la réponse
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        // Ici, en réalité, vous sauvegarderiez l'entité Order dans H2.
    }

    /**
     * Récupère une commande par ID (logique simplifiée).
     */
    @Override
    public void getOrderById(OrderIdRequest request, StreamObserver<Order> responseObserver) {
        // Logique de récupération depuis H2 (omise)
        if (request.getOrderId().equals("ORD-123")) {
            // Exemple de commande retournée
            Order fakeOrder = Order.newBuilder()
                    .setOrderId("ORD-123")
                    .setCustomerId("CUST-001")
                    .setTotalAmount(1200.00)
                    .setStatus("PENDING")
                    .setCreatedAt(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
                    .addItems(com.partenairedecode.grpc.order.OrderItem.newBuilder().setProductId("P001").setQuantity(1).setUnitPrice(1200.00))
                    .build();
            responseObserver.onNext(fakeOrder);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Order not found").asRuntimeException());
        }
    }

    /**
     * Met à jour le statut de la commande (utilisé par Payment Service).
     */
    @Override
    public void updateOrderStatus(Order request, StreamObserver<OperationStatus> responseObserver) {
        // Logique de mise à jour dans H2 (omise)
        
        OperationStatus status = OperationStatus.newBuilder()
                .setSuccess(true)
                .setMessage("Order " + request.getOrderId() + " status updated to " + request.getStatus())
                .build();

        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }

    /**
     * Méthode gRPC pour lire toutes les commandes en utilisant le streaming.
     * @param request Le message Empty (vide).
     * @param responseObserver L'observateur pour envoyer le flux de réponses.
     */
    @Override
    public void getAllOrders(Empty request, StreamObserver<Order> responseObserver) {
        // En production, vous feriez : List<OrderEntity> entities = orderRepository.findAll();

        // 1. Parcourir les commandes (ici, les commandes factices)
        for (Order order : mockOrders) {

            // 2. Envoyer chaque objet Order au client via le flux
            responseObserver.onNext(order);
        }

        // 3. Terminer le flux pour indiquer qu'il n'y a plus de données
        responseObserver.onCompleted();
    }
}