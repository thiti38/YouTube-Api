package com.youtube.api.data;

import com.youtube.api.validation.ListValidate;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

public class Search {

  public void list(Router router, WebClient client, String API_URL, String API_KEY){
    router.get("/search/list/").handler(routingContext -> {
      HttpRequest<Buffer> request = client.getAbs(API_URL + "search");
      new ListValidate().validate(request, routingContext);
      new Response(request, routingContext.response(), API_KEY);
    });
  }
}
