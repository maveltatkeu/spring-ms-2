package com.luwings.producer;

import lombok.Builder;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 20 / Sep / 2025
 * Time: 13 : 17
 */
@Data
@Builder
public class RiderLocation {
  private String riderId;
  private double latitude;
  private double longitude;
}
