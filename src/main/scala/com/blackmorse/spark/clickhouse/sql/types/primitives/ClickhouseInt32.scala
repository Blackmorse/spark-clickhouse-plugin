package com.blackmorse.spark.clickhouse.sql.types.primitives

import com.blackmorse.spark.clickhouse.sql.types.ClickhousePrimitive
import com.blackmorse.spark.clickhouse.writer.ClickhouseTimeZoneInfo
import com.clickhouse.client.ClickHouseDataType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataType, IntegerType}

import java.sql.{PreparedStatement, ResultSet}

case class ClickhouseInt32(nullable: Boolean, lowCardinality: Boolean) extends ClickhousePrimitive {
  override type T = Int
  override val defaultValue: Int = 0

  override def toSparkType(): DataType = IntegerType

  override def extractFromRsByName(name: String, resultSet: ResultSet)(clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Any =
    resultSet.getInt(name)


  override def clickhouseDataType: ClickHouseDataType = ClickHouseDataType.Int32

  override protected def setValueToStatement(i: Int, value: Int, statement: PreparedStatement)(clickhouseTimeZoneInfo: ClickhouseTimeZoneInfo): Unit =
    statement.setInt(i, value)
}

object ClickhouseInt32 {
  def mapRowExtractor(sparkType: DataType): (Row, Int) => Any = sparkType match {
    case IntegerType => (row, index) => row.getInt(index)
  }
}