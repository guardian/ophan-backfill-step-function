package com.gu.ophan.backfill

object PartitionStep extends JsonHandler[JobConfig, Seq[JobConfig]] {

  def process(cfg: JobConfig)(implicit env: Env): Seq[JobConfig] = cfg.asDayJobs
}
