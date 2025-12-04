package com.batch.batchalibou.model;

public enum Result{
  GOLD,     //  Sal >= 500k
  SILVER,   //  300k <= Sal < 500k
  BRONZE,   //  200k <= Sal < 300k
  NOURISH,   //  0 <= Sal < 200k
}
