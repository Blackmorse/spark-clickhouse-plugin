package com.blackmorse.spark.clickhouse.reader

import com.blackmorse.spark.clickhouse.sql.types.{ClickhouseArray, ClickhouseDateTime, ClickhouseDateTime64, ClickhouseDecimal, ClickhouseType, PrimitiveClickhouseType}
import com.clickhouse.client.ClickHouseDataType


object ClickhouseTypesParser {
  private val arrayPrefix = "Array("
  private val lowCardinalityPrefix = "LowCardinality("
  private val nullablePrefix = "Nullable("
  private val decimalPrefix = "Decimal("
  private val dateTime = "DateTime"
  private val dateTime64 = "DateTime64("

  def parseType(typ: String, lowCardinality: Boolean = false, nullable: Boolean = false): ClickhouseType = {

    if (typ.startsWith(arrayPrefix)) {
      ClickhouseArray(parseType(typ.substring(arrayPrefix.length, typ.length - 1)))
    } else if (typ.startsWith(lowCardinalityPrefix)) {
      parseType(typ.substring(lowCardinalityPrefix.length, typ.length - 1), lowCardinality = true, nullable = nullable)
    } else if (typ.startsWith(nullablePrefix)) {
      parseType(typ.substring(nullablePrefix.length, typ.length - 1), lowCardinality = lowCardinality, nullable = true)
    } else if (typ.startsWith(dateTime64)) {
      val p = typ.substring(dateTime64.length, typ.length - 1).trim.toInt
      ClickhouseDateTime64(p, nullable = nullable)
    } else if (typ.startsWith(dateTime)) {
      ClickhouseDateTime(nullable = nullable, lowCardinality = lowCardinality)
    } else if (typ.startsWith(decimalPrefix)) {
      val rest = typ.substring(decimalPrefix.length, typ.length - 1)
      val split = rest.split(",")
      ClickhouseDecimal(split.head.trim.toInt, split(1).trim.toInt, nullable = nullable)
    } else {
      PrimitiveClickhouseType(ClickHouseDataType.valueOf(typ), nullable = nullable, lowCardinality = lowCardinality)
    }
  }
}