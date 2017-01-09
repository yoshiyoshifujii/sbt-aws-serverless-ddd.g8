package $package$.infrastructure.lambda.consumer.model

import java.util.Base64

import scala.collection.JavaConverters._
import scala.util.Try

object KinesisEvent {
  val Base64Decoder = Base64.getDecoder

  private lazy val instanceOf: Object => java.util.LinkedHashMap[String, Object] =
    obj => obj.asInstanceOf[java.util.LinkedHashMap[String, Object]]

  private lazy val getObject = (obj: Object) => (fieldName: String) =>
    instanceOf(obj).get(fieldName)

  def apply(records: Object): KinesisEvent = KinesisEvent(
    for {
      record <- records.asInstanceOf[java.util.List[Object]].asScala
    } yield KinesisEventRecord(record))

  def parse(event: Object): Try[KinesisEvent] = Try {
    val records = getObject(event)("Records")
    KinesisEvent(records)
  }

  case class RecordData(encodedValue: String) {
    lazy val value = String.valueOf(Base64Decoder.decode(encodedValue).map(_.toChar))
    def array: Array[Byte] = value.getBytes("utf-8")
  }

  case class Record(sequenceNumber: String,
                    approximateArrivalTimestamp: Object,
                    data: RecordData,
                    partitionKey: String,
                    kinesisSchemaVersion: String)

  object Record {
    def apply(record: Object): Record = {
      val getter = getObject(record)
      val keys = Seq(
        "sequenceNumber",
        "approximateArrivalTimestamp",
        "data",
        "partitionKey",
        "kinesisSchemaVersion")
      keys.map(getter(_)) match {
        case Seq(
        sequenceNumber: String,
        approximateArrivalTimestamp: Object,
        data: String,
        partitionKey: String,
        kinesisSchemaVersion: String) =>
          new Record(
            sequenceNumber,
            approximateArrivalTimestamp,
            RecordData(data),
            partitionKey,
            kinesisSchemaVersion)
        case _ =>
          throw new RuntimeException(s"failed parse Record. [\${keys.mkString(",")}]")
      }
    }
  }

  case class KinesisEventRecord(eventSource: String,
                                kinesis: Record,
                                eventID: String,
                                invokeIdentityArn: String,
                                eventName: String,
                                eventVersion: String,
                                eventSourceARN: String,
                                awsRegion: String)

  object KinesisEventRecord {
    def apply(record: Object): KinesisEventRecord = {
      val getter = getObject(record)
      val keys = Seq(
        "eventSource",
        "kinesis",
        "eventID",
        "invokeIdentityArn",
        "eventName",
        "eventVersion",
        "eventSourceARN",
        "awsRegion")
      keys.map(getter(_)) match {
        case Seq(
        eventSource: String,
        kinesis: Object,
        eventID: String,
        invokeIdentityArn: String,
        eventName: String,
        eventVersion: String,
        eventSourceARN: String,
        awsRegion: String) =>
          new KinesisEventRecord(
            eventSource,
            Record(kinesis),
            eventID,
            invokeIdentityArn,
            eventName,
            eventVersion,
            eventSourceARN,
            awsRegion)
        case _ =>
          throw new RuntimeException(s"failed parse KinesisEventRecord. [\${keys.mkString(",")}]")
      }
    }
  }

}

import KinesisEvent._
case class KinesisEvent(records: Seq[KinesisEventRecord])

