
import io.gatling.commons.shared.unstable.util.PathHelper._
import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.Predef.->
import scala._
import scala.language.postfixOps

class ChallengeFloodTest3 extends Simulation {

  val time_min = 3
  val time_max = 8

  private val httpProtocol = http
    .baseUrl("https://challenge.flood.io")
    .disableFollowRedirect
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("uk-UA,uk;q=0.9,en-GB;q=0.8,en;q=0.7,en-US;q=0.6")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")

  private val scn = scenario("ChallengeFloodTest3")
//-------------------------------------------------------------------
    .exec(
      http("Get Home page")
        .get("/")
        .check(status.is(200))
        .check(substring("Welcome to our Script Challenge"))
        .check(css("input[name='authenticity_token']", "value").find.saveAs("token"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .exec(getCookieValue(CookieKey("largest_order")))
    .exec(sessionFunction = session => {
      CookieKey("largest_order").saveAs("largest_order")
      val dd = session("largest_order").toString.indexOf("->",0) + 3
      val aa = session("largest_order").toString.substring(dd,dd+67).toLowerCase()
      session.set("largest_order2", aa)
    })
    .exec(sessionFunction = session => {
      val dd = session("largest_order").toString.indexOf("->",0) + 3
      val bb = session("largest_order").toString.substring(dd,dd+67).concat("=").concat(session("largest_order").toString.substring(dd+66+4, dd+66+4+42))
      session.set("largest_order1", bb)
    })
    .exec(session => {
      System.out.println("Parameters for Step 2")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("commit").as[String])
      session;
        })
    .pause(time_min,time_max)
//------------------------------------------------------------------------
    .exec(
      http("Send parameters for Step 2")
        .post("/start")
        .formParam("utf8", "✓")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to step 2")
      System.out.println(session("redirection").as[String])
      session;
    })

//--------------------------------------------------------
    .exec(
      http("Step 2 page (Select age)")
        .get("/step/2")
        .check(status.is(200))
        .check(substring("Step 2"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css("input[name='commit']", "value").find.saveAs("commit"))
        .check(css("option:not(:first-child)").findRandom.saveAs("age")))
    .exec(session => {
      System.out.println("Parameters for Step 3")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("commit").as[String])
      System.out.println(session("age").as[String])
      session;
    })
    .pause(time_min,time_max)
//----------------------------------------------------------
    .exec(
      http("Send parameters for Step 3")
        .post("/start")
        .formParam("utf8", "✓")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("challenger[age]", "${age}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to step 3")
      System.out.println(session("redirection").as[String])
      session;
    })

//--------------------------------------------------------
    .exec(
      http("Step 3 page (Max order)")
        .get("${redirection}")
        .check(status.is(200))
        .check(substring("Step 3"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css("input[name='commit']", "value").find.saveAs("commit"))
        .check(css("label[class='collection_radio_buttons']").findAll.saveAs("orders_values"))
        .check(regex(".*"+"${largest_order2}"+".*>(.*?)</label").find.saveAs("order_selected")))
    .exec(session => {
      System.out.println("Parameters for Step 4")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("largest_order").as[String])
      System.out.println(session("largest_order1").as[String])
      System.out.println(session("largest_order2").as[String])
      System.out.println(session("order_selected").as[String])
      System.out.println(session("commit").as[String])
      session;
    })
    .pause(time_min,time_max)

//----------------------------------------------------------
    .exec(
      http("Send parameters for Step 4")
        .post("/start")
        .formParam("utf8", "✓")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("challenger[largest_order]", "${order_selected}")
        .formParam("challenger[order_selected]", "${largest_order1}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to step 4")
      System.out.println(session("redirection").as[String])
      session;
    })

//--------------------------------------------------------

    .exec(
      http("Step 4 page (Order number and orders)")
        .get("${redirection}")
        .check(status.is(200))
        .check(substring("Step 4"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(regex(".*\"(\\d{10})\".*").findRandom.saveAs("order_num"))
        .check(regex(".*name=\"(.*?)\".*"+"${order_num}").findAll.saveAs("parameters"))
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .foreach("${parameters}", "param", "count") {
      exec(session => {
        val pp = session("parameters").as[List[String]].apply(session("count").as[Int])
        val nn = session("count").as[Int]
        session.set(nn.toString,pp)
      })
    }
    .repeat(10,"counter") {
      exec(session => {
        System.out.println(session("${counter}").as[String])
        session
      })
    }
    .exec(session => {
      System.out.println("Parameters for Step 5")
      System.out.println(session("order_num").as[String])
      System.out.println(session("0").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("commit").as[String])
      session;
    })
    .pause(time_min,time_max)
//----------------------------------------------------------
    .exec(
      http("Send parameters for Step 5")
        .post("/start")
        .formParam("utf8", "✓")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("${0}", "${order_num}")
        .formParam("${1}", "${order_num}")
        .formParam("${2}", "${order_num}")
        .formParam("${3}", "${order_num}")
        .formParam("${4}", "${order_num}")
        .formParam("${5}", "${order_num}")
        .formParam("${6}", "${order_num}")
        .formParam("${7}", "${order_num}")
        .formParam("${8}", "${order_num}")
        .formParam("${9}", "${order_num}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to step 5")
      System.out.println(session("redirection").as[String])
      session;
    })

//--------------------------------------------------------

    .exec(
      http("Step 5 page (One time token)")
        .get("${redirection}")
        .check(status.is(200))
        .check(substring("Step 5"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .exec(
      http("Get one time token")
        .get("/code")
        .check(regex("(\\d{10})").find.saveAs("one_time_token")))
    .exec(session => {
      System.out.println("Parameters for Step Done")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("one_time_token").as[String])
      System.out.println(session("commit").as[String])
      session;
    })
    .pause(time_min,time_max)

//---------------------------------------------------------------
    .exec(
      http("Send parameters for Step Done")
        .post("/start")
        .formParam("utf8", "✓")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("challenger[one_time_token]", "${one_time_token}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
//--------------------------------------------------------------

    .exec(
      http("Get Done page")
        .get("${redirection}")
        .check(status.is(200))
        .check(substring("You're Done!")))
    .pause(time_min,time_max)

//--------------------------------------------------------------
 // setUp(scn.inject(atOnceUsers(2))).protocols(httpProtocol)
	setUp(scn.inject(incrementConcurrentUsers(5)

    .times(2)
    .eachLevelLasting(5)
    .separatedByRampsLasting(5)
    .startingFrom(5)))
    .assertions(
      global.responseTime.max.lt(50),
      global.successfulRequests.percent.gt(95.0)
    )

    .protocols(httpProtocol)

}
