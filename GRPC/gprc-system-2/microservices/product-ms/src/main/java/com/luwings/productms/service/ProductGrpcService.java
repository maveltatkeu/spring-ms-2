package com.luwings.productms.service;

import com.luwings.productms.domain.ProductEntity;
import com.luwings.productms.repository.ProductRepository;
import com.partenairedecode.grpc.product.Empty;
import com.partenairedecode.grpc.product.OperationStatus;
import com.partenairedecode.grpc.product.Product;
import com.partenairedecode.grpc.product.ProductIdRequest;
import com.partenairedecode.grpc.product.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

  private final ProductRepository productRepository;

  /**
   * Méthode d'implémentation gRPC pour créer ou mettre à jour un produit.
   *
   * @param request          Le message Product reçu du client.
   * @param responseObserver L'observateur pour renvoyer la réponse.
   */
  @Override
  @Transactional
  public void saveProduct(Product request, StreamObserver<Product> responseObserver) {
    // 1. Mapper le message gRPC à l'Entité JPA
    ProductEntity productEntity = ProductEntity.builder()
        .productId(request.getProductId())
        .name(request.getName())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .build();

    // 2. Sauvegarder dans la base de données
    ProductEntity savedProduct = productRepository.save(productEntity);
    log.info("Product Created: {}", savedProduct);
    // 3. Mapper l'Entité JPA au message gRPC de réponse
    Product response = Product.newBuilder()
        .setProductId(savedProduct.getProductId())
        .setName(savedProduct.getName())
        .setPrice(savedProduct.getPrice())
        .setQuantity(savedProduct.getQuantity())
        .build();

    // 4. Envoyer la réponse et terminer
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  /**
   * Méthode gRPC pour récupérer un produit par ID.
   */
  @Override
  public void getProductById(ProductIdRequest request, StreamObserver<Product> responseObserver) {
    Optional<ProductEntity> productOptional =
        productRepository.findById(request.getProductId());

    if (productOptional.isPresent()) {
      ProductEntity entity = productOptional.get();
      Product response = Product.newBuilder()
          .setProductId(entity.getProductId())
          .setName(entity.getName())
          .setPrice(entity.getPrice())
          .setQuantity(entity.getQuantity())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } else {
      // Gérer le cas où le produit n'est pas trouvé
      // En gRPC, cela se fait en appelant onError
      responseObserver.onError(
          io.grpc.Status.NOT_FOUND
              .withDescription("Product with ID " + request.getProductId() + " not found")
              .asRuntimeException()
      );
    }
  }

  /**
   * Méthode gRPC pour supprimer un produit.
   */
  @Override
  @Transactional
  public void deleteProduct(ProductIdRequest request, StreamObserver<OperationStatus> responseObserver) {
    if (productRepository.existsById(request.getProductId())) {
      productRepository.deleteById(request.getProductId());
      OperationStatus status = OperationStatus.newBuilder()
          .setSuccess(true)
          .setMessage("Product " + request.getProductId() + " deleted successfully.")
          .build();
      responseObserver.onNext(status);
      responseObserver.onCompleted();
    } else {
      OperationStatus status = OperationStatus.newBuilder()
          .setSuccess(false)
          .setMessage("Product with ID " + request.getProductId() + " not found for deletion.")
          .build();
      responseObserver.onNext(status);
      responseObserver.onCompleted();
    }
  }

  /**
   * Méthode gRPC pour lire tous les produits en utilisant le streaming.
   *
   * @param request          Le message Empty (vide).
   * @param responseObserver L'observateur pour envoyer le flux de réponses.
   */
  @Override
  public void getAllProducts(Empty request, StreamObserver<Product> responseObserver) {
    // 1. Récupérer tous les produits de la base de données
    List<ProductEntity> products = productRepository.findAll();

    // 2. Parcourir la liste et envoyer chaque produit un par un
    for (ProductEntity entity : products) {
      Product response = Product.newBuilder()
          .setProductId(entity.getProductId())
          .setName(entity.getName())
          .setPrice(entity.getPrice())
          .setQuantity(entity.getQuantity())
          .build();

      //  Envoie l'objet Product au client via le flux
      responseObserver.onNext(response);
    }

    // 3. Terminer le flux pour indiquer qu'il n'y a plus de données
    responseObserver.onCompleted();
  }
}