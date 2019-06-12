package com.youtube.api;

import com.youtube.api.data.Videos;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;

import com.youtube.api.data.Search;
import io.vertx.ext.web.client.WebClient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class MainVerticle extends AbstractVerticle {

  private static Logger logger = LoggerFactory.getLogger(MainVerticle.class.getName());

  @Override
  public void start(Future<Void> startFuture) {
    HttpServer server = vertx.createHttpServer();
    WebClient client = WebClient.create(vertx);
    Router router = Router.router(vertx);
    Properties config = new Properties();
    try {
      InputStream inputStream = new FileInputStream("src/main/conf/config.properties");
      config.load(inputStream);
      String API_URL = config.getProperty("API_URL");
      String API_KEY = config.getProperty("API_KEY");
      SQLClient mySQLClient = MySQLClient.createNonShared(vertx, new JsonObject()
        .put("username", config.getProperty("username"))
        .put("password", config.getProperty("password"))
        .put("database", config.getProperty("database")));
      router.get("/").handler(routingContext -> {
        HttpServerResponse responses = routingContext.response();
        responses.putHeader("content-type", "text/plain");
        responses.end("Index");
      });
      new Search().list(router, client, API_URL, API_KEY);
      new Videos().video(router, client, API_URL, API_KEY, mySQLClient);
    } catch (Exception e){
      System.out.println("Error := " + e.getMessage());
      logger.error("Error := " + e.getMessage());
    }

    server.requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        startFuture.complete();
        System.out.println("HTTP server started on port 8080");
        logger.info("Server listening on port 8080");
      } else {
        startFuture.fail(http.cause());
        logger.error("Error := " + http.cause());
      }
    });
  }
}
