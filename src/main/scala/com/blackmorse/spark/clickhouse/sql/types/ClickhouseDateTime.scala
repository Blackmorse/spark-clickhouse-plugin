package com.blackmorse.spark.clickhouse.sql.types

import com.blackmorse.spark.clickhouse.sql.types.extractors.{TimestampArrayRSExtractor, TimestampRSExtractor}
import com.blackmorse.spark.clickhouse.utils.ClickhouseTimeZoneInfo
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types.{DataType, TimestampType}

import java.sql.{PreparedStatement, Timestamp}

case class ClickhouseDateTime(nullable: Boolean, lowCardinality: Boolean)
    extends ClickhouseType
    with TimestampRSExtractor
    with TimestampArrayRSExtractor {
  override type T = Timestamp
  override lazy val defaultValue = new Timestamp(0)

  override def toSparkType(): DataType = TimestampType

  override protected def setValueToStatement(i: Int, value: Timestamp, statement: PreparedStatement)(clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Unit =
    statement.setTimestamp(i, value, clickhouseTimeZoneInfo.calendar)

  override def clickhouseDataTypeString: String = "DateTime"

  override def convertInternalValue(value: Any): Timestamp = new Timestamp(value.asInstanceOf[Long] / 1000)

  override def convertInternalArrayValue(value: ArrayData): Seq[T] = value.toSeq[Long](toSparkType()).map(l => new Timestamp(l / 1000))
}

case class ClickhouseDateTime64(p: Int, nullable: Boolean)
    extends ClickhouseType
    with TimestampRSExtractor
    with TimestampArrayRSExtractor {
  override type T = Timestamp
  override lazy val defaultValue: Timestamp = new Timestamp(0)
  override def toSparkType(): DataType = TimestampType

  protected override def setValueToStatement(i: Int, value: Timestamp, statement: PreparedStatement)
                                            (clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Unit = {
    statement.setTimestamp(i, value, clickhouseTimeZoneInfo.calendar)
  }

  override def clickhouseDataTypeString: String = s"DateTime64($p)"

  override def convertInternalValue(value: Any): Timestamp = new Timestamp(value.asInstanceOf[Long] / 1000)

  override def convertInternalArrayValue(value: ArrayData): Seq[T] = value.toSeq[Long](toSparkType()).map(l => new Timestamp(l / 1000))
}