package com.blackmorse.spark.clickhouse.sql.types.primitives

import com.blackmorse.spark.clickhouse.sql.types.ClickhousePrimitive
import com.blackmorse.spark.clickhouse.writer.ClickhouseTimeZoneInfo
import com.clickhouse.client.ClickHouseDataType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataType, DecimalType}

import java.sql.{PreparedStatement, ResultSet}

case class ClickhouseUInt64(nullable: Boolean, lowCardinality: Boolean) extends ClickhousePrimitive {
  override def toSparkType(): DataType = DecimalType(38, 0)

  override def extractFromRs(name: String, resultSet: ResultSet): Any = resultSet.getBigDecimal(name).setScale(0)

  override def extractFromRowAndSetToStatement(i: Int, row: Row, statement: PreparedStatement)
                                              (clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Unit =
    statement.setBigDecimal(i + 1, row.getDecimal(i))

  override def clickhouseDataType: ClickHouseDataType = ClickHouseDataType.UInt64

  override def extractArray(name: String, resultSet: ResultSet): AnyRef = {
    val array = resultSet.getArray(name).getArray
    array.asInstanceOf[Array[Long]].map(l => new java.math.BigDecimal(l.toString))
  }
}