package com.youtube.api.validation;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;

@SuppressWarnings("Duplicates")
public class ListValidate {
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
    String order = rc.request().getParam("order");
    if (order != null){
      request.addQueryParam("order", order);
    }
    String pageToken = rc.request().getParam("pageToken");
    if (pageToken != null){
      request.addQueryParam("pageToken", pageToken);
    }
    String q = rc.request().getParam("q");
    if (q != null){
      request.addQueryParam("q", q);
    }
    String regionCode = rc.request().getParam("regionCode");
    if (regionCode != null){
      request.addQueryParam("regionCode", regionCode);
    }
    String relatedToVideoId = rc.request().getParam("relatedToVideoId");
    if (relatedToVideoId != null){
      request.addQueryParam("relatedToVideoId", relatedToVideoId);
    }
    String type = rc.request().getParam("type");
    if (type != null){
      request.addQueryParam("type", type);
    }
  }
}
