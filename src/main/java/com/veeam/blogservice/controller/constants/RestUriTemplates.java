package com.veeam.blogservice.controller.constants;

public interface RestUriTemplates {
    String ID_VAR = "id";
    String ACCOUNT_ID_VAR = "accountId";
    String BLOGPOST_ID_VAR = "blogpostId";

    String SELF_URL_TEMPLATE = "self";
    String LOGIN_URL_TEMPLATE = "login";
    String CREATE_URL_TEMPLATE = "create";
    String UPDATE_URL_TEMPLATE = "update";
    String REMOVE_URL_TEMPLATE = "remove";
    String BLOGPOSTS_URL_TEMPLATE = "blogposts";
    String COMMENTS_URL_TEMPLATE = "comments";
    String ADD_COMMENT_URL_TEMPLATE = "addComment";

    String FULL_BLOGPOSTS_URL_TEMPLATE = "account/{" + ACCOUNT_ID_VAR + "}/" + BLOGPOSTS_URL_TEMPLATE;
    String SINGLE_BLOGPOSTS_URL_TEMPLATE = FULL_BLOGPOSTS_URL_TEMPLATE + "/{" + ID_VAR + "}";
    String FULL_COMMENTS_URL_TEMPLATE = FULL_BLOGPOSTS_URL_TEMPLATE +"/{" +BLOGPOST_ID_VAR +"}/" + COMMENTS_URL_TEMPLATE;
    String SINGLE_COMMENTS_URL_TEMPLATE = FULL_COMMENTS_URL_TEMPLATE + "/{" + ID_VAR + "}";


}
