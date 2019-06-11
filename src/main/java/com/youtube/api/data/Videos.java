package com.youtube.api.data;

import com.youtube.api.validation.MostPopularValidate;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.util.List;
import java.util.Locale;

public class Videos {
  public void video(Router router, WebClient client, String API_URL, String API_KEY, SQLClient mySQLClient){

    router.get("/videos/:id").handler(routingContext -> {
      HttpServerResponse responses = routingContext.response();
      String id = routingContext.request().getParam("id");
      String query = "SELECT data FROM videos_list WHERE id=?";
      mySQLClient.queryWithParams(query, new JsonArray().add(id), ar -> {
        if (ar.succeeded() && !ar.result().getRows().isEmpty()) {
          String res = "";
          List<JsonArray> results = ar.result().getResults();
          for (JsonArray row : results){
            res = row.getString(0);
          }
          System.out.println("Succeed to SELECT := " + id);
          responses.putHeader("content-type", "application/json");
          responses.end(res);
        } else {
          HttpRequest<Buffer> request = client.getAbs(API_URL + "videos")
            .addQueryParam("id", id)
            .addQueryParam("prettyPrint", "false");
          String part = routingContext.request().getParam("part");
          if (part != null){
            request.addQueryParam("part", part);
          } else {
            request.addQueryParam("part", "snippet");
          }
          new Response(request, responses, API_KEY, id, mySQLClient, client.getAbs(API_URL + "search"));
        }
      });
    });

    router.get("/videos/mostPopular/").handler(routingContext -> {
      HttpRequest<Buffer> request = client.getAbs(API_URL + "videos")
        .addQueryParam("chart", "mostPopular");
      HttpServerResponse responses = routingContext.response();
      /*String cCode = Locale.getDefault().getLanguage();
      System.out.println(cCode);*/
      new MostPopularValidate().validate(request, routingContext);
      new Response(request, responses, API_KEY);
    });
  }
}
