package com.blackmorse.spark.clickhouse.reader.single

import com.blackmorse.spark.clickhouse.reader.{ClickhouseReaderBase, ClickhouseReaderConfiguration}
import com.clickhouse.jdbc.ClickHouseDriver
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.{InputPartition, PartitionReader, PartitionReaderFactory}

class ClickhouseSinglePartitionReaderFactory(chReaderConf: ClickhouseReaderConfiguration) extends PartitionReaderFactory {
  override def createReader(partition: InputPartition): PartitionReader[InternalRow] = {
    val fields = chReaderConf.schema.fields.map(f => s"`${f.name}`").mkString(", ")
    new ClickhouseReaderBase(
      chReaderConf = chReaderConf,
      connectionProvider = () => new ClickHouseDriver().connect(chReaderConf.url, chReaderConf.connectionProperties),
      sql = s"SELECT $fields FROM ${chReaderConf.tableInfo.name}"
    )
  }
}