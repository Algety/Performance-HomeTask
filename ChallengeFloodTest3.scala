import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.Predef.->
import scala._

class ChallengeFloodTest3 extends Simulation {

  private val httpProtocol = http
    .baseUrl("https://challenge.flood.io")
    .disableFollowRedirect
 //   .inferHtmlResources(AllowList(), DenyList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*""", """.*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""))
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
        .check(css("input[name='authenticity_token']", "value").find.saveAs("token"))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .exec(session => {
      System.out.println("Parameters for Step 2")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("commit").as[String])
      session;
        })
    .pause(3)
//------------------------------------------------------------------------
    .exec(
      http("Redirections to Step 2")
        .post("/start")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
   //    .formParam("challenger[one_time_token]", "2506172872")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to")
      System.out.println(session("redirection").as[String])
      session;
    })
    .pause(2)
//--------------------------------------------------------
    .exec(
      http("Step 2 page")
        .get("${redirection}")
        .check(status.is(200))
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
    .pause(1)
//----------------------------------------------------------
    .exec(
      http("Redirection to Step 3")
        .post("/start")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("challenger[age]", "${age}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to")
      System.out.println(session("redirection").as[String])
      session;
    })
    .pause(2)
//--------------------------------------------------------
    .exec(
      http("Step 3 page")
        .get("${redirection}")
        .check(status.is(200))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
        .check(css(".collection_radio_buttons").findAll.transform(list=>list.map(_.toInt).max).saveAs("largest_orders"))

   //     .check(css("label[class='collection_radio_buttons']").findAll.transform(list => list.map(_.toInt).max).saveAs("largest_order"))
   //     .check(regex(".*value=\"(.*?)\".*${largest_order}").find.saveAs("order_selected"))
       // .check(css("input[class='radio_buttons optional']/text("${largest_order}")","value")
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .exec(session => {
      System.out.println("Parameters for Step 4")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
      System.out.println(session("largest_order").as[String])
      System.out.println(session("order_selected").as[String])
      System.out.println(session("commit").as[String])
      session;
    })
//----------------------------------------------------------
    .exec(
      http("Redirection to Step 4")
        .post("/start")
        .formParam("authenticity_token", "${token}")
        .formParam("challenger[step_id]", "${step_id}")
        .formParam("challenger[step_number]", "${step_number}")
        .formParam("challenger[largest_order]", "${largest_order}")
      //  .formParam("challenger[order_selected]","${order_selected}")
        .formParam("commit", "${commit}")
        .check(status.is(302))
        .check(header("Location").saveAs("redirection")))
    .exec(session => {
      System.out.println("Redirection to")
      System.out.println(session("redirection").as[String])
      session;
    })
    .pause(2)
//--------------------------------------------------------
    .exec(
      http("Step 4 page")
        .get("${redirection}")
        .check(status.is(200))
        .check(css("input[name='challenger[step_id]']", "value").find.saveAs("step_id"))
        .check(css("input[name='challenger[step_number]']", "value").find.saveAs("step_number"))
    //    .check(css("label[class='collection_radio_buttons']").findAll.saveAs("order_values"))
        .check(css("input[name='commit']", "value").find.saveAs("commit")))
    .exec(session => {
      System.out.println("Parameters for Step 5")
      System.out.println(session("token").as[String])
      System.out.println(session("step_id").as[String])
      System.out.println(session("step_number").as[String])
  //    System.out.println(session("order_value").as[String])
      System.out.println(session("commit").as[String])
      session;
    })

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
  //    .formParam("challenger[one_time_token]", "2506172872")
}
