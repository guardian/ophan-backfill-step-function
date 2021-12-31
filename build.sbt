name := "ophan-backfill"

organization := "com.gu"

description:= "ophan backfill step function lambdas"

version := "1.0"

scalaVersion := "2.13.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-target:jvm-1.8",
  "-Ywarn-dead-code",
  "-Wunused:imports"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",

  "net.logstash.logback" % "logstash-logback-encoder" % "7.0.1",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.32", //  log4j-over-slf4j provides `org.apache.log4j.MDC`, which is dynamically loaded by the Lambda runtime
  "ch.qos.logback" % "logback-classic" % "1.2.7",

  "com.amazonaws" % "aws-java-sdk-ssm" % "1.12.122",
  "com.lihaoyi" %% "upickle" % "1.4.2",
  "com.google.cloud" % "google-cloud-bigquery" % "2.4.1",
  "com.google.cloud" % "google-cloud-storage" % "2.2.2",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)

enablePlugins(RiffRaffArtifact, BuildInfoPlugin)

assemblyJarName := s"${name.value}.jar"
riffRaffPackageType := assembly.value
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")
riffRaffArtifactResources += (file("cloudformation/cloudformation.yaml"), "cloudformation/cfn.yaml")

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

buildInfoPackage := "com.gu.ophan.backfill"
buildInfoKeys := Seq[BuildInfoKey](
  "buildNumber" -> Option(System.getenv("BUILD_NUMBER")).getOrElse("DEV"),
  // so this next one is constant to avoid it always recompiling on dev machines.
  // we only really care about build time on teamcity, when a constant based on when
  // it was loaded is just fine
  "buildTime" -> System.currentTimeMillis,
  "gitCommitId"-> Option(System.getenv("BUILD_VCS_NUMBER")).getOrElse("DEV")
)
