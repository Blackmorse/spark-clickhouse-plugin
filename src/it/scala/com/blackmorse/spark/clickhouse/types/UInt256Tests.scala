package com.blackmorse.spark.clickhouse.types

import com.blackmorse.spark.clickhouse.sql.types.primitives.ClickhouseUInt256
import com.blackmorse.spark.clickhouse.types.BaseTestCases.testPrimitiveAndArray
import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.scalatest.flatspec.AnyFlatSpec

class UInt256Tests extends AnyFlatSpec with DataFrameSuiteBase {
  import sqlContext.implicits._

  "UInt256" should "be supported" in {
    testPrimitiveAndArray(ClickhouseUInt256(nullable = false, lowCardinality = false))(
      seq = Seq((1 to 100) map (i => new java.math.BigDecimal(i))),
      rowConverter = row => row.getDecimal(0)
    )
  }
}
