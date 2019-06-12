package com.youtube.api.data;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

class Response {
  private static Logger logger = LoggerFactory.getLogger(Response.class.getName());
  Response(HttpRequest<Buffer> request, HttpServerResponse responses, String API_KEY){
    request.addQueryParam("key", API_KEY);
    request.send(ar ->{
      if (ar.succeeded()){
        HttpResponse<Buffer> response = ar.result();
        responses.putHeader("content-type", "application/json")
          .end(response.bodyAsString());
      } else {
        logger.error("Error := " + ar.cause().getMessage());
        responses.putHeader("content-type", "application/json")
          .end(ar.cause().getMessage());
      }
    });
  }

  Response(HttpRequest<Buffer> request, HttpServerResponse responses, String API_KEY, String id
    , SQLClient mySQLClient, HttpRequest<Buffer> requestRelate){
    request.addQueryParam("key", API_KEY);
    request.send(ar ->{
      if (ar.succeeded()){
        HttpResponse<Buffer> response = ar.result();
        responses.putHeader("content-type", "application/json")
          .end(response.bodyAsString());
        String data = response.bodyAsString();
        requestRelate.addQueryParam("part", "snippet")
          .addQueryParam("relatedToVideoId", id)
          .addQueryParam("type", "video")
          .addQueryParam("prettyPrint", "false")
          .addQueryParam("key", API_KEY);
        requestRelate.send(res -> {
          HttpResponse<Buffer> responseRelate = ar.result();
          if (res.succeeded()){
            String query = "INSERT INTO videos_list (id, data, relate) VALUES (?,?,?)";
            JsonArray params = new JsonArray().add(id).add(data).add(responseRelate.bodyAsString());
            mySQLClient.updateWithParams(query, params, rs -> {
              if (rs.succeeded()){
                System.out.println("Succeed to INSERT INTO := " + query + params.toString());
              } else {
                System.out.println("Error to INSERT INTO := " + query + params.toString());
                System.out.println("Err := " + rs.cause().getMessage());
              }
            });
          } else {
            logger.error("Error := " + res.cause().getMessage());
          }
        });
      } else {
        logger.error("Error := " + ar.cause().getMessage());
        responses.putHeader("content-type", "application/json")
          .end(ar.cause().getMessage());
      }
    });
  }
}
