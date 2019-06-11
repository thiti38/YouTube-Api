package com.youtube.api;

import com.youtube.api.data.Videos;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;

import com.youtube.api.data.Search;
import io.vertx.ext.web.client.WebClient;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) {
    HttpServer server = vertx.createHttpServer();
    WebClient client = WebClient.create(vertx);
    Router router = Router.router(vertx);
    String API_URL = "https://www.googleapis.com/youtube/v3/";
    String API_KEY = "YOUR_API_HERE";
    SQLClient mySQLClient = JDBCClient.createNonShared(vertx, new JsonObject().put("url", "jdbc:mysql://localhost:3306/youtube_api")
      .put("user", "root")
      .put("password", "password")
      .put("driver_class", "org.hsqldb.jdbcDriver"));

    router.get("/").handler(routingContext -> {
      HttpServerResponse responses = routingContext.response();
      responses.putHeader("content-type", "text/plain");
      responses.end("Index");
    });

    new Search().list(router, client, API_URL, API_KEY);
    new Videos().video(router, client, API_URL, API_KEY, mySQLClient);

    server.requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        startFuture.complete();
        System.out.println("HTTP server started on port 8080");
      } else {
        startFuture.fail(http.cause());
      }
    });
  }
}
