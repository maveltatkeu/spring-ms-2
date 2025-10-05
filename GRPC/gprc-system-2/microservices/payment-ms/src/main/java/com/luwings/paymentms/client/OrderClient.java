package com.luwings.paymentms.client;

import com.partenairedecode.grpc.order.Order;
import com.partenairedecode.grpc.order.OrderIdRequest;
import com.partenairedecode.grpc.order.OrderServiceGrpc;
import com.partenairedecode.grpc.product.OperationStatus;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class OrderClient {

  // Injection du stub gRPC pour le service Order
  @GrpcClient("order-service")
  private OrderServiceGrpc.OrderServiceBlockingStub orderBlockingStub;

  /**
   * Appelle Order Service pour récupérer les détails d'une commande.
   * @param orderId L'ID de la commande.
   * @return Le message Order s'il est trouvé et prêt à être payé.
   */
  public Order getOrderDetails(String orderId) {
    OrderIdRequest request = OrderIdRequest.newBuilder().setOrderId(orderId).build();
    try {
      return orderBlockingStub.getOrderById(request);
    } catch (StatusRuntimeException e) {
      System.err.println("Erreur gRPC lors de la récupération de la commande " + orderId + ": " + e.getStatus());
      return null;
    }
  }

  /**
   * Met à jour le statut d'une commande dans le Order Service.
   */
  public OperationStatus updateOrderStatus(String orderId, String newStatus) {
    Order orderUpdate = Order.newBuilder()
        .setOrderId(orderId)
        .setStatus(newStatus)
        .build();
    try {
      return orderBlockingStub.updateOrderStatus(orderUpdate);
    } catch (StatusRuntimeException e) {
      System.err.println("Erreur gRPC lors de la mise à jour du statut de la commande " + orderId + ": " + e.getStatus());
      return OperationStatus.newBuilder().setSuccess(false).setMessage("Update failed").build();
    }
  }
}