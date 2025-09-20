package com.luwings;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: korawings : Mavel
 * Date: Sat 20 / Sep / 2025
 * Time: 04 : 05
 *///TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
// Get the current instant (a point on the timeline)
    Instant instant = Instant.now();

// Use an ISO 8601 formatter
    String timestamp = DateTimeFormatter.ISO_INSTANT.format(instant);
    System.out.println(timestamp);
// Example Output: 2025-09-20T03:03:42.123Z
  }
}