package com.blackmorse.spark.clickhouse.sql.types.primitives

import com.blackmorse.spark.clickhouse.sql.types.ClickhousePrimitive
import com.blackmorse.spark.clickhouse.sql.types.extractors.{ShortRSExtractor, SimpleArrayRSExtractor}
import com.blackmorse.spark.clickhouse.utils.ClickhouseTimeZoneInfo
import com.clickhouse.client.ClickHouseDataType
import org.apache.spark.sql.types.{DataType, ShortType}

import java.sql.PreparedStatement

case class ClickhouseInt16(nullable: Boolean, lowCardinality: Boolean)
    extends ClickhousePrimitive
    with ShortRSExtractor
    with SimpleArrayRSExtractor {
  override type T = Short
  override lazy val defaultValue: Short = 0

  override def toSparkType(): DataType = ShortType

  override def clickhouseDataType: ClickHouseDataType = ClickHouseDataType.Int16

  override protected def setValueToStatement(i: Int, value: Short, statement: PreparedStatement)(clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Unit =
    statement.setShort(i, value)
}