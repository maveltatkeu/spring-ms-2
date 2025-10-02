package com.partenairedecode.paymentservice.service;

import com.partenairedecode.grpc.order.Order;
import com.partenairedecode.grpc.payment.Payment;
import com.partenairedecode.grpc.payment.PaymentIdRequest;
import com.partenairedecode.grpc.payment.PaymentServiceGrpc;
import com.partenairedecode.paymentservice.client.OrderClient;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

  // Injection du client pour le service de commandes
  private final OrderClient orderClient;

  /**
   * Traite un paiement en le validant contre la commande.
   */
  @Override
  public void processPayment(Payment request, StreamObserver<Payment> responseObserver) {
    String orderId = request.getOrderId();

    // 1. 🔑 Appel gRPC au Order Service pour récupérer la commande
    Order orderDetails = orderClient.getOrderDetails(orderId);

    if (orderDetails == null) {
      responseObserver.onError(Status.NOT_FOUND
          .withDescription("Commande non trouvée ou invalide: " + orderId)
          .asRuntimeException());
      return;
    }

    // 2. Logique de vérification (simplifiée)
    if (orderDetails.getStatus().equals("PAID")) {
      responseObserver.onError(Status.FAILED_PRECONDITION
          .withDescription("La commande " + orderId + " est déjà payée.")
          .asRuntimeException());
      return;
    }

    // 3. Simuler le traitement du paiement (statut APPROVED si montant valide)
    String paymentStatus = "REJECTED";
    if (request.getAmount() >= orderDetails.getTotalAmount()) {
      paymentStatus = "APPROVED";
    }

    String newPaymentId = UUID.randomUUID().toString();

    // 4. Mettre à jour le statut de la commande (appel gRPC)
    if ("APPROVED".equals(paymentStatus)) {
      orderClient.updateOrderStatus(orderId, "PAID");
    }

    // 5. Construire la réponse de paiement
    Payment response = request.toBuilder()
        .setPaymentId(newPaymentId)
        .setStatus(paymentStatus)
        .build();

    // 6. Envoi de la réponse
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  // Implémentation de getPaymentById (simplifiée et omise ici pour la concision)
  @Override
  public void getPaymentById(PaymentIdRequest request, StreamObserver<Payment> responseObserver) {
    // ... Logique de récupération depuis H2 ...
    responseObserver.onError(Status.UNIMPLEMENTED.withDescription("Not Implemented").asRuntimeException());
  }
}