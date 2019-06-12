package com.youtube.api.data;

import com.youtube.api.validation.ListValidate;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

public class Search {
  private static Logger logger = LoggerFactory.getLogger(Search.class.getName());
  public void list(Router router, WebClient client, String API_URL, String API_KEY){
    router.get("/search/list/").handler(routingContext -> {
      logger.info("Request Api := Search List");
      HttpRequest<Buffer> request = client.getAbs(API_URL + "search");
      new ListValidate().validate(request, routingContext);
      new Response(request, routingContext.response(), API_KEY);
    });
  }
}
