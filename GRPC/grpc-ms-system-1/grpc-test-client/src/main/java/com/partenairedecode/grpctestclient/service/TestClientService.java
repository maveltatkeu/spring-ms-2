package com.partenairedecode.grpctestclient.service;

import com.partenairedecode.grpc.product.Product;
import com.partenairedecode.grpc.product.ProductIdRequest;
import com.partenairedecode.grpc.product.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestClientService {

  // 1. gRPC Client Stub (for internal calls)
  @GrpcClient("product-service")
  private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

  private final RestTemplate restTemplate = new RestTemplate();
//  private static final String ENVOY_BASE_URL = "http://localhost:9090/v1/products";
  private static final String ENVOY_BASE_URL = "http://localhost:8080/v1/products";

  /**
   * Tests the internal gRPC connection directly to the product-service.
   */
  public void testInternalGrpc(String productId) {
    System.out.println("\n--- Testing Internal gRPC Call (Bypassing Envoy) ---");
    try {
      ProductIdRequest request = ProductIdRequest.newBuilder().setProductId(productId).build();
      Product response = productServiceStub.getProductById(request);

      System.out.println("✅ gRPC Success: Retrieved Product:");
      System.out.println("   ID: " + response.getProductId());
      System.out.println("   Name: " + response.getName());

    } catch (StatusRuntimeException e) {
      System.err.println("❌ gRPC Error: Status Code: " + e.getStatus().getCode());
      System.err.println("   Details: " + e.getStatus().getDescription());
    }
  }

  /**
   * Tests the external HTTP connection via the Envoy API Gateway.
   */
  public void testExternalHttp(String productId) {
    System.out.println("\n--- Testing External HTTP Call (Via Envoy Transcoder) ---");
    String url = ENVOY_BASE_URL + "/" + productId;

    try {
      // Note: We use String.class as we expect a JSON string back from Envoy
      String response = restTemplate.getForObject(url, String.class);

      System.out.println("✅ HTTP Success (via Envoy): Received JSON Response:");
      System.out.println(response);

    } catch (Exception e) {
      System.err.println("❌ HTTP Error (via Envoy): Could not retrieve product.");
      System.err.println("   Details: " + e.getMessage());
    }
  }
}