package com.youtube.api.validation;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;

@SuppressWarnings("Duplicates")
public class MostPopularValidate {
  public void validate(HttpRequest request, RoutingContext rc){
    request.addQueryParam("prettyPrint", "false");
    String part = rc.request().getParam("part");
    if (part != null){
      request.addQueryParam("part", part);
    }
    String maxResults = rc.request().getParam("maxResults");
    if (maxResults != null){
      request.addQueryParam("maxResults", maxResults);
    }
    String pageToken = rc.request().getParam("pageToken");
    if (pageToken != null){
      request.addQueryParam("pageToken", pageToken);
    }
    String regionCode = rc.request().getParam("regionCode");
    if (regionCode != null){
      request.addQueryParam("regionCode", regionCode);
    }
    String videoCategoryId = rc.request().getParam("videoCategoryId");
    if (videoCategoryId != null){
      request.addQueryParam("videoCategoryId", videoCategoryId);
    }
  }
}
